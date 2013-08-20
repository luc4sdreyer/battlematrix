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

public class BruteV2 extends Bot {	
	
	private ArrayList<PointS> path;
	private boolean active = true;
	private boolean initialized = false;
	
	//
	// Anticipate that enemy tanks might fire (if they can) from their current position.
	// Mark this in the bulletGrid accordingly.
	//
	private boolean considerPotentialEnemyBullets = true;
	private boolean swapMyTanks = false;
	private boolean swapEnemyTanks = false;
	private boolean attackBaseSooner = true;
	private boolean alwaysCheckForEvasiveAction = true;
	
	// Statistics
	private long sumTotalTime = 0;
	private long sumSearchTime = 0;
	private int numSearchRequests = 0;
	private int numSearches = 0;
	
	private static final boolean printGoalArea = false;
	private static final boolean printBulletGrid = false;
	private static final boolean printExtraOutput = false;

	public BruteV2(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public int[] getActions(GameState originalGameState, int timeLimitMS) {
		long totalTime = System.nanoTime();
		long searchTime = 0;
		if (!initialized) {
			this.init(originalGameState);
		}
		GameState gameState = originalGameState.clone();
		int[] gameActions = new int[2];
		Arrays.fill(gameActions, -1);
		path = null;
		//int tankIdx = 2 * getPlayerIndex();
		//int myT2Idx = 2 * getPlayerIndex() + 1;
		int enemyIndex = (getPlayerIndex() + 1) % 2;
		HashSet<Point> goalArea = new HashSet<Point>();
		//Tank myT2 = gameState.getTanks()[myT2Idx];
		int[][] map = gameState.getMap();
		boolean[][] grid = null;
		int[][] bulletGrid = null;
		HashSet<Point> safeArea = null;
		
		if (!gameState.getTanks()[2 * getPlayerIndex()].isAlive() && !gameState.getTanks()[2 * getPlayerIndex() + 1].isAlive()) {
			gameActions = Random.getActionsStatic();
		} else if (!gameState.isActive() || !active) {
			gameActions = Random.getActionsStatic();
			System.out.println("\tBrute using RANDOM move instead.");
		} else {
			
			// 
			// Arbitrary target assignment
			//
			for (int tankIdx = 2 * getPlayerIndex(); tankIdx < 2 * (getPlayerIndex()+1); tankIdx++) {
				Tank me = gameState.getTanks()[tankIdx];
				int gameActionIdx = tankIdx - 2 * getPlayerIndex();
				
				if (!me.isAlive()) {
					continue;
				}
				
				Point target = null;
				int targetRadius = -1000000000;
				int maxDistanceFromTarget = 1000000000;
				if ((!swapMyTanks && tankIdx % 2 == 0) || (swapMyTanks && tankIdx % 2 == 1)) {
					if (gameState.getMapType() == GameState.MAP_TYPE_E0) {
						if (map[40][15] == Unit.WALL) {
							target = new Point(15,40);
							targetRadius = 0;
						}
					}
					
					int targetEnemyTankPrimary = 2 * enemyIndex;
					int targetEnemyTankSecondary = 2 * enemyIndex + 1;
					
					if (this.swapEnemyTanks) {
						int temp = targetEnemyTankPrimary;
						targetEnemyTankPrimary = targetEnemyTankSecondary;
						targetEnemyTankSecondary = temp;
					} 
					
					if (target == null && gameState.getTanks()[targetEnemyTankPrimary].isAlive()) {
						target = new Point(gameState.getTanks()[targetEnemyTankPrimary].getPosition());
						target.translate(2, 2);
						targetRadius = 2;
					}
					
					if (!attackBaseSooner) {
						if (target == null && gameState.getTanks()[targetEnemyTankSecondary].isAlive()) {
							target = new Point(gameState.getTanks()[targetEnemyTankSecondary].getPosition());
							target.translate(2, 2);
							targetRadius = 2;
						}
					}
					
					if (target == null) {
						target = gameState.getBases()[enemyIndex].getPosition();
						targetRadius = 0;
					}
				} else {
					if (gameState.getMapType() == GameState.MAP_TYPE_E0) {
						if (gameState.getBases()[getPlayerIndex()].getPosition().y < map.length/2) {
							for (int y = 0 ; y < map.length; y++) {
								if (map[y][56] == Unit.WALL) {
									target = new Point(56, y);
									targetRadius = 0;
									maxDistanceFromTarget = 4;
									break;
								}
							}
						} else {
							for (int y = map.length -1 ; y >= 0 ; y--) {
								if (map[y][56] == Unit.WALL) {
									target = new Point(56, y);
									targetRadius = 0;
									maxDistanceFromTarget = 4;
									break;
								}
							}
						}
					}
					
					int targetEnemyTankPrimary = 2 * enemyIndex + 1;
					int targetEnemyTankSecondary = 2 * enemyIndex;
					
					if (this.swapEnemyTanks) {
						int temp = targetEnemyTankPrimary;
						targetEnemyTankPrimary = targetEnemyTankSecondary;
						targetEnemyTankSecondary = temp;
					} 
					
					if (target == null && gameState.getTanks()[targetEnemyTankPrimary].isAlive()) {
						target = new Point(gameState.getTanks()[targetEnemyTankPrimary].getPosition());
						target.translate(2, 2);
						targetRadius = 2;
					}

					if (!attackBaseSooner) {
						if (target == null && gameState.getTanks()[targetEnemyTankSecondary].isAlive()) {
							target = new Point(gameState.getTanks()[targetEnemyTankSecondary].getPosition());
							target.translate(2, 2);
							targetRadius = 2;
						}
					}
					
					if (target == null) {
						target = gameState.getBases()[enemyIndex].getPosition();
						targetRadius = 0;
					}
				}
	
				int[] ticksUntilBulletHit = new int[1];
				ticksUntilBulletHit[0] = GameState.maxNumBlocks;				
				
				path = null;
				if (path == null) {
	
					//
					// Convert map to grid. 
					//
					if (grid == null) {
						grid = getBasicGrid(map);
					}
					
					//
					// Generate Bullet grid for basic Bullet avoidance
					//
					if (bulletGrid == null) {
						bulletGrid = getBulletGrid(gameState, enemyIndex);
					}
					
					// TODO: Assume that base units won't be in the absolute corner and on the edge.
					//
					// Generate start and goal nodes
					//
					int numWallsAllowed = 0;
					Point startPoint = new Point();
					startPoint.setLocation(	gameState.getTanks()[tankIdx].getPosition().x + 2, 
											gameState.getTanks()[tankIdx].getPosition().y + 2);
					
					if (this.alwaysCheckForEvasiveAction) {
						if (bulletGrid[startPoint.y][startPoint.x] < GameState.maxNumBlocks) {
							//
							// There is a chance of being hit by a bullet, take evasive action if necessary.
							//
							if (safeArea == null) {
								safeArea = new HashSet<Point>();
								for (int y = 0; y < grid.length; y++) {
									for (int x = 0; x < grid[0].length; x++) {
										if (!grid[y][x] && bulletGrid[y][x] == GameState.maxNumBlocks) {
											safeArea.add(new Point(x,y));
										}
									}
								}
							}
							
							int[] totalNodesVisited = new int[1];
							PointS start = new PointS(startPoint.x, startPoint.y, 0, 'X');
							long tempTime = System.nanoTime();
							path = PathFind.BFSFinder(start, safeArea, target, grid, totalNodesVisited,
									PathFind.GOAL_PREFERENCE_FIRST_FOUND, bulletGrid, ticksUntilBulletHit);
							tempTime = System.nanoTime() - tempTime;
							searchTime += tempTime;
							if (ticksUntilBulletHit[0] == 1) {
								//
								// You have to move right now or be destroyed.
								//
								if (!path.isEmpty()) {
									path.add(0, start);
									Point tankCenter = new Point(me.getPosition());
									tankCenter.translate(2, 2);
									if (!tankCenter.equals(path.get(0).getP())) {
										System.out.println("FATAL ERROR: position does not match path!");
									} 
									int direction = Util.getDirection(path.get(0).getP(), path.get(1).getP());									
									gameActions[gameActionIdx] = direction;									
									path.remove(0);									
									continue;
								}
							}							
						}
					}
					
					PointS start = new PointS(startPoint.x, startPoint.y, 0, 'X');
					path = new ArrayList<PointS>();
					int[] unexploredSpace = new int[1]; 
					unexploredSpace[0] = 1;
					
					// TODO: Here you can optimise by looking at all the paths, and then choosing the best one.
					//while(path.isEmpty() && numWallsAllowed < Math.max(map.length, map[0].length)) {
					numSearchRequests++;
					int prevGoalSize = 0;
					while(path.isEmpty() && unexploredSpace[0] > 0) {
						unexploredSpace[0] = 0;
						prevGoalSize = goalArea.size();
						goalArea = getGoalArea(gameState, map, target, numWallsAllowed, targetRadius,
								tankIdx, unexploredSpace, maxDistanceFromTarget);
						if (goalArea.size() == prevGoalSize) {
							//
							// The search cannot find anything new with the same area.
							//
							//prevGoalSize 					
							numWallsAllowed++;
							continue;
						}
						numSearches++;
						int[] totalNodesVisited = new int[1];
						long tempTime = System.nanoTime();
						path = PathFind.BFSFinder(start, goalArea, null, grid, totalNodesVisited,
								PathFind.GOAL_PREFERENCE_CLOSEST_TO_START, bulletGrid, ticksUntilBulletHit);
						
						tempTime = System.nanoTime() - tempTime;
						searchTime += tempTime;						
						numWallsAllowed++;
					}
					
					if (path.isEmpty() && !goalArea.contains(start.p)) {
						if (printExtraOutput) {
							System.err.println("Could not find a path");
						}
						//active = false;
						gameActions[gameActionIdx] = Random.getActionsStaticDontFire()[gameActionIdx];
						continue;
					}
					
					path.add(0, start);
				}
				
				boolean shouldFire = false;
				//if (ticksUntilBulletHit[0] == 0) {
					//
					// Bullet impact is unavoidable. Might as well try to hit it or anything else 
					//
					
				//if (ticksUntilBulletHit[0] > 0 && !gameState.getBullets()[meIdx].isAlive()) {
				if (!gameState.getBullets()[tankIdx].isAlive()) {
					//
					// If the first thing you hit is an enemy base or tank or bullet coming at you,
					// you might as well try.
					// Wall shots are not considered.
					//
					Tank t = gameState.getTanks()[tankIdx];
	
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
					gameActions[gameActionIdx] = GameAction.ACTION_FIRE;
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
							if (me.getRotation() == direction && !gameState.getBullets()[tankIdx].isAlive()) {						
								gameActions[gameActionIdx] = GameAction.ACTION_FIRE;
							} else {
								if (me.getRotation() == direction) {
									//
									// This leads to a small improvement because it prevents the tank from being too close.
									//
									gameActions[gameActionIdx] = GameAction.ACTION_NONE;
								} else {
									gameActions[gameActionIdx] = direction;
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
						
						gameActions[gameActionIdx] = direction;
						
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
		}
		totalTime = System.nanoTime() - totalTime;
		
		sumSearchTime += searchTime;
		sumTotalTime += totalTime;
		
		if (printExtraOutput) {
			System.out.println("\tBrute actions: "+GameAction.toString(gameActions[0])+" "+GameAction.toString(gameActions[1]) + " " 
					+ "\tTotal time: " + Util.padRight(String.format("%.2fms", (totalTime/1000000.0)), 8)
					+ "Search time: " + String.format("%.2f", ((double)(searchTime * 10000 / totalTime))/100) + "%"
					+ "\n\tCumulative Total time: " + Util.padRight(String.format("%.2fms", (sumTotalTime/1000000.0)), 10)
					+ "Cumulative Search time: " + Util.padRight(String.format("%.2f", ((double)(sumSearchTime * 10000 / sumTotalTime))/100) + "%", 8)
					+ " search LF: " + Util.padRight(String.format("%.2fms", (numSearches/(double)numSearchRequests)), 10)
					);
		}
		return gameActions;
	}


	private void init(GameState originalGameState) {
		this.initialized = true;
		
		if (originalGameState.getMapType() == GameState.MAP_TYPE_E0) {
			Tank myEastTank = originalGameState.getTanks()[2 * getPlayerIndex() + 1];
			if (myEastTank.isAlive() && myEastTank.getPosition().x < originalGameState.getMap()[0].length/2) {
				this.swapMyTanks = true;
			}
			
			int enemyIndex = (getPlayerIndex() + 1) % 2;
			Tank enemyEastTank = originalGameState.getTanks()[2 * enemyIndex + 1];
			if (enemyEastTank.isAlive() && enemyEastTank.getPosition().x < originalGameState.getMap()[0].length/2) {
				this.swapEnemyTanks = true;
			}
		}
	}

	/**
	 * This grid is true where movement is not allowed.
	 */
	private boolean[][] getBasicGrid(int[][] map) {
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
		return grid;
	}

	private int[][] getBulletGrid(GameState originalState, int enemyIndex) {
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
							int numBulletSteps = 2;
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

	private HashSet<Point> getGoalArea(GameState gameState, int[][] map, Point target,
			int numWallsAllowed, int targetRadius, int tankIdx, int[] unexploredSpace, int maxDistanceFromTarget) {
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
		int startLoop = 0;
		if ((target.y == 0 || target.y == map.length - 1) && (target.x == 0 || target.x == map[0].length - 1)) {
			System.err.println("FATAL ERROR: Base may not be in the absolute corner!");
		} else {
			for (int r = -wideGoalArea; r <= wideGoalArea; r++) {
				int x = target.x;
				
				if (gameState.isInMap(new Point(x + r, 0))) {
					numWalls = 0;
					startLoop = target.y + (3 + targetRadius);
					for (int y = startLoop; y < map.length && Math.abs(y - startLoop) < maxDistanceFromTarget; y++) {
						if (map[y][x + r] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								unexploredSpace[0] += map.length - 1 - y;
								break;
							}
						} else if (Unit.isBase(map[y][x + r])) {
							break;
						}
						goalArea.add(new Point(x + r,y));
					}
					
					numWalls = 0;
					startLoop = target.y - (3 + targetRadius);
					for (int y = startLoop; y >=0 && Math.abs(y - startLoop) < maxDistanceFromTarget; y--) {
						if (map[y][x + r] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								unexploredSpace[0] += y;
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
					startLoop = target.x + (3 + targetRadius);
					for (x = startLoop; x < map[0].length && Math.abs(x - startLoop) < maxDistanceFromTarget; x++) {
						if (map[y + r][x] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								unexploredSpace[0] += map[0].length - 1 - y;
								break;
							}
						} else if (Unit.isBase(map[y + r][x])) {
							break;
						}
						goalArea.add(new Point(x,y + r));
					}
					
					numWalls = 0;
					startLoop = target.x - (3 + targetRadius);
					for (x = startLoop; x >=0 && Math.abs(x - startLoop) < maxDistanceFromTarget; x--) {
						if (map[y + r][x] == Unit.WALL) {
							numWalls++;
							if (numWalls > numWallsAllowed) {
								unexploredSpace[0] += x;
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
