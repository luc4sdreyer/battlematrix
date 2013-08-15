package za.co.entelect.competition.bots;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import za.co.entelect.competition.Bullet;
import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;
import za.co.entelect.competition.PathFind;
import za.co.entelect.competition.Tank;
import za.co.entelect.competition.Unit;
import za.co.entelect.competition.Util;
import za.co.entelect.competition.PathFind.PointS;

/**
 * The Brute is very stupid yet dangerous. It locates the closes target, and fires at it while driving towards it.
 * It will choose the first alive enemy tank as a target. If there are no enemy tanks left it will target the enemy base.
 * It cannot navigate through blocks!
 * 
 */

public class Brute extends Bot {
	
	private ArrayList<PointS> path;
	private boolean active = true;
	
	//
	// Anticipate that enemy tanks might fire (if they can) from their current position.
	// Mark this in the bulletGrid accordingly.
	//
	private boolean considerPotentialEnemyBullets = true;
	
	private static final boolean printGoalArea = false;
	private static final boolean  printBulletGrid = false;

	public Brute(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public int[] getActions(GameState originalGameState, int timeLimitMS) {
		GameState gameState = originalGameState.clone();
		int[] gameActions = new int[2];
		Arrays.fill(gameActions, -1);
		path = null;
		int meIdx = 2 * getPlayerIndex();
		int enemyIndex = (getPlayerIndex() + 1) % 2;
		HashSet<Point> goalArea = new HashSet<Point>();
		Tank me = gameState.getTanks()[meIdx];
		
		// TODO: Working only for one tank at the moment
		if (!me.isAlive()) {
			gameActions = Random.getActionsStatic();
		} else if (!gameState.isActive() || !active) {
			gameActions = Random.getActionsStatic();
			System.out.println("\tBrute using RANDOM move instead.");
		} else {
			
			Point target = null;
			int targetRadius = -1000000000;
			if (gameState.getTanks()[2 * enemyIndex].isAlive()) {
				target = new Point(gameState.getTanks()[2 * enemyIndex].getPosition());
				target.translate(2, 2);
				targetRadius = 2;
			} else if (gameState.getTanks()[2 * enemyIndex + 1].isAlive()) {
				target = new Point(gameState.getTanks()[2 * enemyIndex + 1].getPosition());
				target.translate(2, 2);
				targetRadius = 2;
			} else {
				target = gameState.getBases()[enemyIndex].getPosition();
				targetRadius = 0;
			}

			int[] ticksUntilBulletHit = new int[1];
			ticksUntilBulletHit[0] = GameState.maxNumBlocks;
			if (path == null) {
				path = null;

				//
				// Convert map to grid. 
				//
				int[][] map = gameState.getMap();
				boolean[][] grid = new boolean[map.length][map[0].length];
				for (int y = 0; y < grid.length; y++) {
					for (int x = 0; x < grid[0].length; x++) {
						if (map[y][x] == Unit.WALL || Unit.isBase(map[y][x])) {
							grid[y][x] = true;
						} else {
							grid[y][x] = false;
						}
					}
				}
				
				//
				// Generate Bullet grid for basic Bullet avoidance
				//
				int[][] bulletGrid = getBulletGrid(gameState, meIdx, enemyIndex);
				
				// TODO: Assume that base units won't be in the absolute corner and on the edge.
				//
				// Generate start and goal nodes
				//
				int numWallsAllowed = 0;
				Point startPoint = new Point();
				PointS start = null;
				path = new ArrayList<PointS>();
				
				// TODO: Here you can optimize by looking at all the paths, and then choosing the best one.
				//while(path.isEmpty() && numWallsAllowed < Math.max(map.length, map[0].length)) {
				// TODO: Ignoring walls over here!
				goalArea = getGoalArea(gameState, map, startPoint, target, numWallsAllowed, targetRadius);
				int[] totalNodesVisited = new int[1];
				start = new PointS(startPoint.x, startPoint.y, 0, 'X');
				path = PathFind.BFSFinder(start, goalArea, target, grid, totalNodesVisited, gameState,
						PathFind.GOAL_PREFERENCE_CLOSEST_TO_START, bulletGrid, ticksUntilBulletHit);
				
				numWallsAllowed++;
				//}
				
				if (path.isEmpty() && !goalArea.contains(start.p)) {
					//System.err.println("Could not find a path");
					//active = false;
					return Random.getActionsStatic();
				}
				
				path.add(0, start);
			}
			
			boolean shouldFire = false;
			//if (ticksUntilBulletHit[0] == 0) {
				//
				// Bullet impact is unavoidable. Might as well try to hit it or anything else 
				//
				
			//if (ticksUntilBulletHit[0] > 0 && !gameState.getBullets()[meIdx].isAlive()) {
			if (!gameState.getBullets()[meIdx].isAlive()) {
				//
				// If the first thing you hit is an enemy base or tank or bullet coming at you,
				// you might as well try.
				// Wall shots are not considered.
				//
				Tank t = gameState.getTanks()[meIdx];

				Point bulletPosition = new Point(t.getPosition().x + GameState.tankSize/2, t.getPosition().y + GameState.tankSize/2);
				bulletPosition = Util.movePoint(bulletPosition, t.getRotation());
				bulletPosition = Util.movePoint(bulletPosition, t.getRotation());
				//Move only twice because if there is something right in front of me it should be checked too
				//bulletPosition = Util.movePoint(bulletPosition, t.getRotation());
			
				//if (i == 2 && bullets[i].getPosition().y == 7 && (bullets[i].getRotation() % 2 == 1)) {
				int score = 0;
				int walls = 0;
				int empty = 0;
				Point start = new Point(bulletPosition);
				Point end = Util.movePointDist(bulletPosition, t.getRotation(), 1000, gameState);
				while(!start.equals(end)) {
					if (start.x > end.x) {
						start.x--;
					} else if (start.x < end.x) {
						start.x++;
					} else if (start.y > end.y) {
						start.y--;
					} else {
						start.y++;
					}
					if (gameState.getMap()[start.y][start.x] == Unit.EMPTY) {
						empty++;
					} else if (gameState.getMap()[start.y][start.x] == Unit.WALL) {
						//walls++;
						break;
					} else {
						if (Unit.isBase(gameState.getMap()[start.y][start.x])) {
							if (Unit.BASE1 + getPlayerIndex() == gameState.getMap()[start.y][start.x]) {
								if (walls == 0) {
									//
									// Firing at own base!
									//
									score = -(10000 - empty);
								}
							} else {
								score = 10000 - walls*10 - empty;
							}
							break;
						} else if (Unit.isTank(gameState.getMap()[start.y][start.x])) {
							boolean friendlyFire = false;
							if (getPlayerIndex() == 0) {
								if ((Unit.TANK1A == gameState.getMap()[start.y][start.x] 
										|| Unit.TANK1B == gameState.getMap()[start.y][start.x]) 
										&& walls == 0) {
									friendlyFire = true;
								}
							} else {
								if ((Unit.TANK2A == gameState.getMap()[start.y][start.x] 
										|| Unit.TANK2B == gameState.getMap()[start.y][start.x]) 
										&& walls == 0) {
									friendlyFire = true;
								}
							}
							if (friendlyFire) {
								//
								// Firing at own tank!
								//
								score = -(1000 - empty);
							} else {
								score = 1000 - walls*10 - empty;
							}
							break;
						} else if (Unit.isBullet(gameState.getMap()[start.y][start.x])) {
							Bullet foreignBullet = (Bullet) gameState.getUnit(gameState.getMap()[start.y][start.x]);
													
							//if (i == 2 && bullets[i].getPosition().y == 7 && (bullets[i].getRotation() % 2 == 1)) {
							Point foreignBulletStart = new Point(foreignBullet.getPosition());
							Point foreignBulletEnd = Util.movePointDist(foreignBulletStart, foreignBullet.getRotation(), 1000, gameState);

							while(!foreignBulletStart.equals(foreignBulletEnd)) {
								if (foreignBulletStart.x > foreignBulletEnd.x) {
									foreignBulletStart.x--;
								} else if (foreignBulletStart.x < foreignBulletEnd.x) {
									foreignBulletStart.x++;
								} else if (foreignBulletStart.y > foreignBulletEnd.y) {
									foreignBulletStart.y--;
								} else {
									foreignBulletStart.y++;
								}
								
								if (foreignBulletStart.equals(bulletPosition)) {
									shouldFire = true;
									break;
								} else if (gameState.getMap()[foreignBulletStart.y][foreignBulletStart.x] == Unit.EMPTY) {
									empty++;
								} else {
									break;
								}
							}
							if (shouldFire) {
								break;
							}
						}
					}
				}
				if (getPlayerIndex() == 0) {
					if (score > 0) {
						shouldFire = true;
					}
				} else {
					if (score < 0) {
						shouldFire = true;
					}
				}
			}
			
			if (shouldFire) {
				gameActions[0] = GameAction.ACTION_FIRE;
			} else {				
				Point tankCenter = new Point(me.getPosition());
				tankCenter.translate(2, 2);
				//System.out.println("tankCenter: "+tankCenter);
				if (path.size() <= 1) {				
					//
					// We should be on the goal area now.
					//
					//if ((tankCenter.x == target.x) || (tankCenter.y == target.y)) {
					if (goalArea.contains(tankCenter)) {
						int direction = Util.getDirectionUnbounded(tankCenter, target);
						//
						// If there is already a bullet fired rather just move closer to the target.
						//
						if (me.getRotation() == direction && !gameState.getBullets()[meIdx].isAlive()) {						
							gameActions[0] = GameAction.ACTION_FIRE;
						} else {
							if (me.getRotation() == direction) {
								//
								// This leads to a small improvement because it prevents the tank from being too close.
								//
								gameActions[0] = GameAction.ACTION_NONE;
							} else {
								gameActions[0] = direction;
							}
						}
					} else {
						System.out.println("FATAL ERROR: Not on the target area!");
					}
				} else {
					if (!tankCenter.equals(path.get(0).getP())) {
						System.out.println("FATAL ERROR: position does not match path!");
					} 
					int direction = Util.getDirection(path.get(0).getP(), path.get(1).getP());
					
					gameActions[0] = direction;
					
					//if (me.getRotation() == direction) {
					//
					// This will be an actual move, not a rotation
					//
					path.remove(0);
					//System.out.println("tank should move now");
					//}
				}
			}
		}
		gameActions[1] = GameAction.ACTION_NONE;
		return gameActions;
	}


	private int[][] getBulletGrid(GameState originalState, int meIdx, int enemyIndex) {
		GameState gameState = originalState.clone();
		int[][] map = gameState.getMap();
		int[][] bulletGrid = new int[map.length][map[0].length];
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				bulletGrid[y][x] = GameState.maxNumBlocks;
			}
		}
				
		Bullet b = null;
		for (int i = 0; i < gameState.getBullets().length; i++) {
			b = gameState.getBullets()[i];
			if (!b.isAlive()) {
				if (considerPotentialEnemyBullets) {
					//
					// Assume that enemy tanks might fire or rotate and fire.
					//
					if (i/2 == enemyIndex) {
						Tank t = gameState.getTanks()[i];
						if (t.isAlive()) {
							int numBulletSteps = 1;
							for (int rotation = 0; rotation < 4; rotation++) {
								Point bulletPosition = new Point(t.getPosition().x + GameState.tankSize/2, t.getPosition().y + GameState.tankSize/2);
								bulletPosition = Util.movePoint(bulletPosition, (t.getRotation() + rotation) % 4);
								bulletPosition = Util.movePoint(bulletPosition, (t.getRotation() + rotation) % 4);
								bulletPosition = Util.movePoint(bulletPosition, (t.getRotation() + rotation) % 4);
								
								int distance = 1;
								
								if (rotation != 0) {
									//
									// Add an extra movement to compensate for the fact that the tank might move when it rotates.
									//
									bulletPosition = Util.movePoint(bulletPosition, (t.getRotation() + rotation) % 4);
									
									distance = 2;
								} else {
									numBulletSteps++;
								}
	
								Point p = null;
								for (int bulletSteps = 0; bulletSteps < numBulletSteps; bulletSteps++) {
									if (!gameState.isInMap(bulletPosition)) {
										break;
									}
									for (int y2 = -2; y2 <= 2; y2++) {
										for (int x2 = -2; x2 <= 2; x2++) {
											p = new Point(bulletPosition.x + x2, bulletPosition.y + y2);
											if (gameState.isInMap(p) && (distance) < bulletGrid[p.y][p.x]) {
												bulletGrid[p.y][p.x] = distance;					
											}
										}
									}
									bulletPosition = Util.movePoint(bulletPosition, (t.getRotation() + rotation) % 4);
									bulletPosition = Util.movePoint(bulletPosition, (t.getRotation() + rotation) % 4);
									distance++;
								}
							}
						}
					}
				}
			} else {
				
				int distance = 0;
				
				Point p = null;
				for (int y2 = -2; y2 <= 2; y2++) {
					for (int x2 = -2; x2 <= 2; x2++) {
						p = new Point(b.getPosition().x + x2, b.getPosition().y + y2);
						if (gameState.isInMap(p)) {
							bulletGrid[p.y][p.x] = distance;					
						}
					}
				}
				
				//TODO: Here the distance increases by one instead of two to compensate for the assumption that if a bullet's second
				//      move moves into an area that a tank will now move out of, the tank is still killed. This seems wrong but it's safer.
				
				distance += 1;
				
				while(true) {			
					final Point nextP = Util.movePoint(b.getPosition(), b.getRotation());
		
					final Point toAdd;
					if (b.getRotation() == GameAction.ACTION_MOVE_NORTH || b.getRotation() == GameAction.ACTION_MOVE_SOUTH) {				
						if (b.getRotation() == GameAction.ACTION_MOVE_NORTH) {
							toAdd = new Point(nextP.x, nextP.y + GameState.tankSize/2);
						} else {
							toAdd = new Point(nextP.x, nextP.y - GameState.tankSize/2);
						}
						//
						// Finally move the Bullet N/S
						//
						if (!gameState.isInMap(toAdd)) {
							break;
						}
						b.setPosition(nextP);
						for (int x = -2; x <= 2; x++) {
							p = new Point(toAdd.x + x, toAdd.y);
							if (gameState.isInMap(p) && (distance/2) < bulletGrid[p.y][p.x]) {
								bulletGrid[p.y][p.x] = (distance/2);
							}
						}
					} else {				
						if (b.getRotation() == GameAction.ACTION_MOVE_EAST) {
							toAdd = new Point(nextP.x + GameState.tankSize/2, nextP.y);
						} else {
							toAdd = new Point(nextP.x - GameState.tankSize/2, nextP.y);
						}
						//
						// Finally move the Bullet E/W
						//
						if (!gameState.isInMap(toAdd)) {
							break;
						}
						b.setPosition(nextP);
						for (int y = -2; y <= 2; y++) {
							p = new Point(toAdd.x, toAdd.y + y);
							if (gameState.isInMap(p) && (distance/2) < bulletGrid[p.y][p.x]) {
								bulletGrid[p.y][p.x] = (distance/2);
							}
						}
					}
					distance++;
				}
			}
		}

		if (printBulletGrid) {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < bulletGrid.length; y++) {
				for (int x = 0; x < bulletGrid[0].length; x++) {
					if (bulletGrid[y][x] == GameState.maxNumBlocks) {
						sb.append('-');
						sb.append('-');
					} else if (bulletGrid[y][x] < 10) {
						sb.append(' ');
						sb.append(bulletGrid[y][x]);
					} else {
						sb.append(bulletGrid[y][x]);
					}
				}
				sb.append('\n');
			}
			System.out.println(sb.toString());
			System.out.println();
		}
		return bulletGrid;
	}

	private HashSet<Point> getGoalArea(GameState gameState, int[][] map, Point start, Point target, int numWallsAllowed, int targetRadius) {
		for (int i = getPlayerIndex()*2; i < (getPlayerIndex()+1)*2; i++) {
			if (gameState.getTanks()[i].isAlive()) {
				start.setLocation(	gameState.getTanks()[i].getPosition().x + 2, 
									gameState.getTanks()[i].getPosition().y + 2);
				break;
			}
		}
		
		
		HashSet<Point> goalArea = new HashSet<Point>();
		int numWalls = 0;
				
//		if (target.y == 0 || target.y == map.length - 1) {
//			int x = target.x;
//			
//			numWalls = 0;
//			for (int y = target.y + (3 + targetRadius); y < map.length; y++) {
//				if (map[y][x] == Unit.WALL) {
//					numWalls++;
//					if (numWalls > numWallsAllowed) {
//						break;
//					}
//				} else if (Unit.isBase(map[y][x])) {
//					break;
//				}
//				goalArea.add(new Point(x,y));
//			}
//			
//			numWalls = 0;
//			for (int y = target.y - (3 + targetRadius); y >=0; y--) {
//				if (map[y][x] == Unit.WALL) {
//					numWalls++;
//					if (numWalls > numWallsAllowed) {
//						break;
//					}
//					continue;
//				} else if (Unit.isBase(map[y][x])) {
//					break;
//				}
//				goalArea.add(new Point(x,y));
//			}
//		} else if (target.x == 0 || target.x == map[0].length - 1) {
//			int y = target.y;
//			
//			numWalls = 0;
//			for (int x = target.x + (3 + targetRadius); x < map[0].length; x++) {
//				if (map[y][x] == Unit.WALL) {
//					numWalls++;
//					if (numWalls > numWallsAllowed) {
//						break;
//					}
//				} else if (Unit.isBase(map[y][x])) {
//					break;
//				}
//				goalArea.add(new Point(x,y));
//			}
//			
//			numWalls = 0;
//			for (int x = target.x - (3 + targetRadius); x >=0; x--) {
//				if (map[y][x] == Unit.WALL) {
//					numWalls++;
//					if (numWalls > numWallsAllowed) {
//						break;
//					}
//					continue;
//				} else if (Unit.isBase(map[y][x])) {
//					break;
//				}
//				goalArea.add(new Point(x,y));
//			}
//		} else 
		int wideGoalArea = 0;
		if ((target.y == 0 || target.y == map.length - 1) && (target.x == 0 || target.x == map[0].length - 1)) {
			System.err.println("FATAL ERROR: Base may not be in the absolute corner!");
		} else {
			for (int r = -wideGoalArea; r <= wideGoalArea; r++) {
				int x = target.x;
				
				if (gameState.isInMap(new Point(x + r, 0))) {
					numWalls = 0;
					for (int y = target.y + (3 + targetRadius); y < map.length; y++) {
						if (map[y][x + r] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								break;
							}
						} else if (Unit.isBase(map[y][x + r])) {
							break;
						}
						goalArea.add(new Point(x + r,y));
					}
					
					numWalls = 0;
					for (int y = target.y - (3 + targetRadius); y >=0; y--) {
						if (map[y][x + r] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								break;
							}
							continue;
						} else if (Unit.isBase(map[y][x + r])) {
							break;
						}
						goalArea.add(new Point(x + r,y));
					}
				}
				
				int y = target.y;
				
				if (gameState.isInMap(new Point(0, y + r))) {
					numWalls = 0;
					for (x = target.x + (3 + targetRadius); x < map[0].length; x++) {
						if (map[y + r][x] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								break;
							}
						} else if (Unit.isBase(map[y + r][x])) {
							break;
						}
						goalArea.add(new Point(x,y + r));
					}
					
					numWalls = 0;
					for (x = target.x - (3 + targetRadius); x >=0; x--) {
						if (map[y + r][x] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								break;
							}
							continue;
						} else if (Unit.isBase(map[y + r][x])) {
							break;
						}
						goalArea.add(new Point(x,y + r));
					}
				}
			}
		}
		
		if (printGoalArea) {
			ArrayList<String> theMap = gameState.toStringList();
			
			for (Point p : goalArea) {
				char[] mapRow = theMap.remove(p.y).toCharArray();
				mapRow[p.x] = 'G';
				theMap.add(p.y, new String(mapRow));
			}
			
			String mapString = new String();
			for (String string : theMap) {
				mapString += string + '\n'; 
			}
			System.out.println(mapString);
			System.out.println();
		}
		
		return goalArea;
	}

}
