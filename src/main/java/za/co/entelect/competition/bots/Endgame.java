package za.co.entelect.competition.bots;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;
import za.co.entelect.competition.PathFind;
import za.co.entelect.competition.Tank;
import za.co.entelect.competition.Unit;
import za.co.entelect.competition.PathFind.PointS;
import za.co.entelect.competition.Util;

public class Endgame extends Bot {

	private ArrayList<PointS> path;
	private boolean active = true;
	
	public Endgame(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {
		int[] gameActions = new int[2];
		Arrays.fill(gameActions, -1);
		
		
		
		if (!gameState.isActive() || !active) {
			gameActions = Random.getActionsStatic();
			System.out.println("\tEndgame using RANDOM move instead.");
//		} else if (gameState.getStage() < GameState.STAGE_P1_ENDGAME_T2) {
//			System.err.println("FATAL ERROR: Endgame Bot can only plan endgames!");			
//			gameActions = Random.getActionsStatic();
		} else {
			Point target = gameState.getBases()[(getPlayerIndex() + 1) % 2].getPosition();
			if (path == null) {
				path = null;
				long time = System.currentTimeMillis(); 
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
				
				//TODO: Assume that base units won't be in the absolute corner and on the edge.
				//
				// Generate start and goal nodes
				//
				int numWallsAllowed = 0;
				Point startPoint = new Point();
				HashSet<Point> goalArea = new HashSet<Point>();
				PointS start = null;
				path = new ArrayList<PointS>();
				
				// TODO: Here you can optimize by looking at all the paths, and then choosing the best one.
				while(path.isEmpty() && numWallsAllowed < Math.max(map.length, map[0].length)) {					
					goalArea = getGoalArea(gameState, map, startPoint, target, numWallsAllowed);
					int[] totalNodesVisited = new int[1];
					start = new PointS(startPoint.x, startPoint.y, 0, 'X');
					path = PathFind.BFSFinder(start, goalArea, target, grid, totalNodesVisited, gameState, PathFind.GOAL_PREFERENCE_CLOSEST_TO_TARGET, null, null);
					
					numWallsAllowed++;
				}
				
				if (path.isEmpty()) {
					System.err.println("Could not find a path");
					active = false;
					return Random.getActionsStatic();
				}
				
				path.add(0, start);
				//System.out.println("Time: " + (System.currentTimeMillis() - time));
			}
			
			int meIdx = getPlayerIndex()*2;
			Tank me = gameState.getTanks()[meIdx];
			Point tankCenter = new Point(me.getPosition());
			tankCenter.translate(2, 2);
			//System.out.println("tankCenter: "+tankCenter);
			if (path.size() <= 1) {				
				//
				// We should be on the goal area now.
				//
				if ((tankCenter.x == target.x) || (tankCenter.y == target.y)) {
					int direction = Util.getDirectionUnbounded(tankCenter, target);
					//
					// If there is already a bullet fired rather just move closer to the target.
					//
					if (me.getRotation() == direction && !gameState.getBullets()[meIdx].isAlive()) {						
						gameActions[0] = GameAction.ACTION_FIRE;
					} else {
						gameActions[0] = direction;
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
		gameActions[1] = GameAction.ACTION_NONE;
		return gameActions;
	}

	private HashSet<Point> getGoalArea(GameState gameState, int[][] map, Point start, Point target, int numWallsAllowed) {
		for (int i = getPlayerIndex()*2; i < (getPlayerIndex()+1)*2; i++) {
			if (gameState.getTanks()[i].isAlive()) {
				start.setLocation(	gameState.getTanks()[i].getPosition().x + 2, 
									gameState.getTanks()[i].getPosition().y + 2);
				break;
			}
		}
		
		
		HashSet<Point> goalArea = new HashSet<Point>();
		int numWalls = 0;
				
		if (target.y == 0 || target.y == map.length - 1) {
			int x = target.x;
			
			numWalls = 0;
			for (int y = target.y + 3; y < map.length; y++) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
			
			numWalls = 0;
			for (int y = target.y - 3; y >=0; y--) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
					continue;
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
		} else if (target.x == 0 || target.x == map[0].length - 1) {
			int y = target.y;
			
			numWalls = 0;
			for (int x = target.x + 3; x < map[0].length; x++) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
			
			numWalls = 0;
			for (int x = target.x - 3; x >=0; x--) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
					continue;
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
		} else if ((target.y == 0 || target.y == map.length - 1) && (target.x == 0 || target.x == map[0].length - 1)) {
			System.err.println("FATAL ERROR: Base may not be in the absolute corner!");
		} else {
			int x = target.x;
			
			numWalls = 0;
			for (int y = target.y + 3; y < map.length; y++) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
			
			numWalls = 0;
			for (int y = target.y - 3; y >=0; y--) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
					continue;
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
			
			int y = target.y;
			
			numWalls = 0;
			for (x = target.x + 3; x < map[0].length; x++) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
			
			numWalls = 0;
			for (x = target.x - 3; x >=0; x--) {
				if (map[y][x] == Unit.WALL) {
					numWalls++;
					if (numWalls > numWallsAllowed) {
						break;
					}
					continue;
				} else if (Unit.isBase(map[y][x])) {
					break;
				}
				goalArea.add(new Point(x,y));
			}
		}
		
		return goalArea;
	}

}
