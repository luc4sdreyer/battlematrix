package za.co.entelect.competition;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import za.co.entelect.challenge.Action;

/**
 * The core of what happens in the game. Contains an integer grid of all units (see Unit class)
 * and implements the game logic. 
 */

public class GameState {
	public static final int H_MINIMAX = 0;
	public static final int NEG_INF = -2000000000;
	public static final int POS_INF = 2000000000;
	
	public static final int STATUS_ACTIVE = 0;
	public static final int STATUS_PLAYER1_WINS = 1;
	public static final int STATUS_PLAYER2_WINS = 2;
	public static final int STATUS_DRAW = 3;
	
	public static final int STAGE_EQUAL_T2 = 0;
	public static final int STAGE_EQUAL_T1 = 1;
	public static final int STAGE_P1_ADVANTAGE = 2;
	public static final int STAGE_P2_ADVANTAGE = 3;
	public static final int STAGE_P1_ENDGAME_T2 = 4;
	public static final int STAGE_P2_ENDGAME_T2 = 5;
	public static final int STAGE_P1_ENDGAME_T1 = 6;
	public static final int STAGE_P2_ENDGAME_T1 = 7;
	
	public static final int RULES_NORMAL = 0;
	public static final int RULES_TOTAL_DESTRUCTION = 1;
	
	public static final int MAP_TYPE_UNKNOWN = 0;
	public static final int MAP_TYPE_E0 = 1;
	public static final int MAP_TYPE_E1 = 2;
	public static final int MAP_TYPE_E2 = 3;
	public static final int MAP_TYPE_E3 = 4;
	public static final int MAP_TYPE_E4 = 5;
	public static final int MAP_TYPE_E5 = 6;
	public static final int MAP_TYPE_E6 = 7;
	public static final int MAP_TYPE_E7 = 8;
	
	public final static int tankSize = 5;
	public static int maxTurns = 215;
	public final static int maxNumBlocks = 10000;
	

	/**
	 * A tank is defined by its top-left point
	 * 
	 * Unit mapping:
	 * 0	Empty
	 * 1	Wall
	 * 2	Tank1A
	 * 3	Tank1B
	 * 4	Tank2A
	 * 5	Tank2B
	 * 6	Tank1A bullet
	 * 7	Tank1B bullet
	 * 8	Tank2A bullet
	 * 9	Tank2B bullet
	 * 10	Base1
	 * 11	Base2
	 * 
	 */	
	private int[][] map;	
	
	private ArrayList<Collision> collisions;
	private Tank[] tanks;
	private Bullet[] bullets;
	private Base[] bases;
	private int tickCount = 0;
	private int status = 0;
	private int stage = 0;
	private int rules = 0;
	private int mapType = 0;
	private boolean debugMode;
	private boolean saveActions;
	private ArrayList<GameState> gameLog;
	private ArrayList<int[]> actionLog;
	
	private long hashCode;
	
	public GameState(int[][] map, Bullet[] bullets, Tank[] tanks, Base[] bases, ArrayList<Collision> collisions, int tickCount) {
		super();
		this.map = map;
		this.bullets = bullets;
		this.collisions = collisions;
		this.tanks = tanks;
		this.bases = bases;
		this.tickCount = tickCount;
		this.rules = RULES_NORMAL;
		this.mapType = MAP_TYPE_UNKNOWN;
		
		this.init();		
	}	
	public GameState(int[][] map, Bullet[] bullets, Tank[] tanks, Base[] bases, ArrayList<Collision> collisions, int tickCount,
			int rules, int mapType) {
		super();
		this.map = map;
		this.bullets = bullets;
		this.collisions = collisions;
		this.tanks = tanks;
		this.bases = bases;
		this.tickCount = tickCount;
		this.rules = rules;
		this.mapType = mapType;
		
		this.init();		
	}
	private void init() {		
		this.debugMode = false; 
		this.saveActions = false;
		
		this.setStatusAndStage();
		if (this.map.length * this.map[0].length > maxNumBlocks) {
			System.err.println("FATAL ERROR: Map is too large. Max size: " + GameState.maxNumBlocks);
		}		
		//
		// Generate the starting hashCode
		//
		this.hashCode = this.generateHash();
	}
	public int[][] getMap() {
		return map;
	}
	public Bullet[] getBullets() {
		return bullets;
	}
	public Tank[] getTanks() {
		return tanks;
	}
	public Base[] getBases() {
		return bases;
	}
	public ArrayList<Collision> getCollisions() {
		return collisions;
	}
	public void setCollisions(ArrayList<Collision> collisions) {
		this.collisions = collisions;
	}	
	public int getTickCount() {
		return tickCount;
	}
	public void setTickCount(int tickCount) {
		this.tickCount = tickCount;
	}
	public int getStatus() {
		return status;
	}
	public String getStatusString() {
		switch (status) {
			case GameState.STATUS_ACTIVE: 		return "STATUS_ACTIVE"; 
			case GameState.STATUS_DRAW: 		return "STATUS_DRAW"; 
			case GameState.STATUS_PLAYER1_WINS: return "PLAYER1_WINS"; 
			case GameState.STATUS_PLAYER2_WINS: return "PLAYER2_WINS";
			default: return null;
		}
	}
	public int getStage() {
		return stage;
	}
	public boolean isActive() {
		if (this.status == GameState.STATUS_ACTIVE) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isSaveActions() {
		return saveActions;
	}
	public void setSaveActions(boolean saveActions) {
		this.saveActions = saveActions;
		if (this.getGameLog() == null) {
			this.setGameLog(new ArrayList<GameState>());			
		}
		if (this.getActionLog() == null) {
			this.setActionLog(new ArrayList<int[]>());			
		}
	}
	public boolean isDebugMode() {
		return debugMode;
	}
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
		this.setSaveActions(true);
	}
	public ArrayList<GameState> getGameLog() {
		return gameLog;
	}
	public void setGameLog(ArrayList<GameState> gameLog) {
		this.gameLog = gameLog;
	}
	public ArrayList<int[]> getActionLog() {
		return actionLog;
	}
	public void setActionLog(ArrayList<int[]> actionLog) {
		this.actionLog = actionLog;
	}
	public int getRules() {
		return rules;
	}
	public void setRules(int rules) {
		this.rules = rules;
		this.setStatusAndStage();
	}
	public int getMapType() {
		return mapType;
	}
	public void setMapType(int mapType) {
		this.mapType = mapType;
	}
	private final static boolean fastClone = true;
	public GameState clone() {
		if (!fastClone) {
			int[][] newMap = new int[this.map.length][this.map[0].length];
			for (int y = 0; y < newMap.length; y++) {
				for (int x = 0; x < newMap[0].length; x++) {
					newMap[y][x] = this.map[y][x];
				}
			}
			
			Bullet[] newBullets = new Bullet[this.bullets.length];
			for (int i = 0; i < this.bullets.length; i++) {
				newBullets[i] = bullets[i].clone();
			}
			
			ArrayList<Collision> newCollisions = new ArrayList<Collision>();
			for (Collision collision : this.collisions) {
				newCollisions.add(collision.clone());
			}
			
			Tank[] newTanks = new Tank[this.tanks.length];
			for (int i = 0; i < this.tanks.length; i++) {
				newTanks[i] = tanks[i].clone();
			}
			
			Base[] newBases = new Base[this.bases.length];
			for (int i = 0; i < this.bases.length; i++) {
				newBases[i] = bases[i].clone();
			}
			
			return new GameState(newMap, newBullets, newTanks, newBases, newCollisions, this.tickCount, this.rules, this.mapType);
		} else {
			final int mLength = this.map.length;
			int[][] newMap = new int[mLength][];
			for (int y = 0; y < mLength; y++) {
				newMap[y] = map[y].clone();
			}
			
			Bullet[] newBullets = new Bullet[this.bullets.length];
			for (int i = 0; i < this.bullets.length; i++) {
				newBullets[i] = bullets[i].clone();
			}
			
			ArrayList<Collision> newCollisions = new ArrayList<Collision>();
			for (Collision collision : this.collisions) {
				newCollisions.add(collision.clone());
			}
			
			Tank[] newTanks = new Tank[this.tanks.length];
			for (int i = 0; i < this.tanks.length; i++) {
				newTanks[i] = tanks[i].clone();
			}
			
			Base[] newBases = new Base[this.bases.length];
			for (int i = 0; i < this.bases.length; i++) {
				newBases[i] = bases[i].clone();
			}
			
			return new GameState(newMap, newBullets, newTanks, newBases, newCollisions, this.tickCount, this.rules, this.mapType);
		}
	}
	
	/**
	 * Convert from the Entelect version of GameState called za.co.entelect.challenge.Game
	 */
	public static GameState fromEGame(za.co.entelect.challenge.Game eGame, za.co.entelect.challenge.State[][] eStateGrid, 
			HashMap<Integer, Integer> eBullets, String myName, int[] playerIdxHolder, GameState prevXGameState) {
		if (eGame.getPlayers().length != 2) {
			System.err.println("FATAL ERROR: eGame.getPlayers().length != 2");
		}
		
		Tank[] newTanks = new Tank[4];
		Base[] newBases = new Base[2];
		Bullet[] newBullets = new Bullet[4];
		
		ArrayList<za.co.entelect.challenge.Bullet> eBulletsTemp = new ArrayList<za.co.entelect.challenge.Bullet>();;
		
		for (int i = 0; i < 2; i++) {
			za.co.entelect.challenge.Player ePlayer = eGame.getPlayers()[i];
			
			if (ePlayer.getName().equals(myName)) {
				playerIdxHolder[0] = i;
			}
			
			za.co.entelect.challenge.Base eBase = ePlayer.getBase();
			if (eBase == null) {
				newBases[i] = new Base(new Point(0,0), 2, false);
			} else {
				newBases[i] = new Base(new Point(eBase.getX(),eBase.getY()), 2, true);
			}
			
			za.co.entelect.challenge.Unit eUnits[] = ePlayer.getUnits();			
			if (eUnits != null) {
				if (eUnits.length > 2) {
					System.err.println("FATAL ERROR: eUnits.length > 2");					
				}				
				
				for (int j = 0; j < eUnits.length; j++) {
					System.out.println("Got info for unit with ID=" + eUnits[j].getId());
					newTanks[i*2 + j] = new Tank(new Point(eUnits[j].getX() - 2,eUnits[j].getY() - 2), EDirectionToXRotation(eUnits[j].getDirection()), -1, true);
					newTanks[i*2 + j].setID(eUnits[j].getId());
				}
			}

			
			if (ePlayer.getBullets() != null) {
				for (int j = 0; j < ePlayer.getBullets().length; j++) {
					eBulletsTemp.add(ePlayer.getBullets()[j]);
				}
			}
		}
		
		for (int i = 0; i < eBulletsTemp.size(); i++) {
			int key = -1;
			if (!eBullets.containsKey(eBulletsTemp.get(i).getId())) {
				//
				// This is a new bullet. Find out who it belongs to.
				//
				if (prevXGameState == null) {
					//TODO: Return null;
					System.err.println("FATAL ERROR: Bullets cannot exist on the first round!");
					continue;
				} else {
					Point bulletPosition = new Point(eBulletsTemp.get(i).getX(),eBulletsTemp.get(i).getY());
					int oppositeOfBulletRotation = (EDirectionToXRotation(eBulletsTemp.get(i).getDirection()) + 2) % 4;
					bulletPosition = Util.movePoint(bulletPosition, oppositeOfBulletRotation);
					int unitCode = prevXGameState.getTank(bulletPosition);
					if (Unit.isTank(unitCode)) {
						eBullets.put(eBulletsTemp.get(i).getId(), unitCode + 4);
					} else {
						System.err.println("FATAL ERROR: Bullet(" + eBulletsTemp.get(i).getX() + ", " + eBulletsTemp.get(i).getY() 
								+ ", " + EDirectionToXRotation(eBulletsTemp.get(i).getDirection()) + ") was not fired by tank at (" 
								+ bulletPosition.x + ", " + bulletPosition.y + ")! unitCode: " + unitCode);
						continue;
					}
				}
			}
			key = eBulletsTemp.get(i).getId();
			newBullets[eBullets.get(key) - Unit.BULLET_TANK1A] = new Bullet(new Point(eBulletsTemp.get(i).getX(),eBulletsTemp.get(i).getY()), EDirectionToXRotation(eBulletsTemp.get(i).getDirection()), true);
		}
		
		int[][] newMap = new int[eStateGrid[0].length][eStateGrid.length];

		for (int y = 0; y < newMap.length; y++) {
			for (int x = 0; x < newMap[0].length; x++) {
				if (eStateGrid[x][y].getValue().equals(za.co.entelect.challenge.State._FULL)) {
					newMap[y][x] = Unit.WALL;
				} else if (eStateGrid[x][y].getValue().equals(za.co.entelect.challenge.State._EMPTY)) {
					newMap[y][x] = Unit.EMPTY;
				} else if (eStateGrid[x][y].getValue().equals(za.co.entelect.challenge.State._OUT_OF_BOUNDS)) {
					newMap[y][x] = Unit.WALL;
				} else {
					System.err.println("FATAL ERROR: eStateGrid[" + x + "][" + y + "] is unknown: " + eStateGrid[x][y].getValue());
					continue;
				}
			}
		}		

		for (int i = 0; i < newBases.length; i++) {
			if (newBases[i] == null) {
				newBases[i] = new Base(new Point(0,0), 2, false);
			}
			Base b = newBases[i]; 
			if (!b.isAlive()) {
				continue;
			}
			if (GameState.isInMap(b.getPosition(), newMap)) {
				newMap[b.getPosition().y][b.getPosition().x] = Unit.BASE1+i;
			} else {
				System.err.println("FATAL ERROR: The point (" + b.getPosition().x + "," + b.getPosition().y + ") where Base " + i + " should be placed is outside the map.");
				b.setAlive(false);
			}			
		}
		for (int i = 0; i < newTanks.length; i++) {
			if (newTanks[i] == null) {
				newTanks[i] = new Tank(new Point(0,0), 2, -1, false);
			}
			Tank t = newTanks[i];
			if (!t.isAlive()) {
				continue;
			}
			for (int y2 = 0; y2 < GameState.tankSize; y2++) {
				for (int x2 = 0; x2 < GameState.tankSize; x2++) {
					Point nextPoint = new Point(t.getPosition().x+x2, t.getPosition().y+y2);
					if (!GameState.isInMap(nextPoint, newMap)) {
						t.setAlive(false);
						y2 = GameState.tankSize;
						System.err.println("FATAL ERROR: The point (" + nextPoint.x + "," + nextPoint.y + ") where Tank " + i + " should be placed is outside the map.");
						break;
					}
					if (newMap[nextPoint.y][nextPoint.x] != Unit.EMPTY) {
						t.setAlive(false);
						y2 = GameState.tankSize;
						System.err.println("Tank " + i + " has been killed because it is not on EMPTY cells at (" 
								+ nextPoint.x + "," + nextPoint.y + ").");
						break;
					}
					newMap[nextPoint.y][nextPoint.x] = Unit.TANK1A+i;
				}
			}
		}
		for (int i = 0; i < newBullets.length; i++) {
			if (newBullets[i] == null) {
				newBullets[i] = new Bullet(new Point(0,0), 2, false);
			}
			Bullet b = newBullets[i];
			if (!b.isAlive()) {
				continue;
			}
			if (GameState.isInMap(b.getPosition(), newMap)) {
				newMap[b.getPosition().y][b.getPosition().x] = Unit.BULLET_TANK1A+i;
			} else {
				System.err.println("FATAL ERROR: The point (" + b.getPosition().x + "," + b.getPosition().y + ") where Bullet " + i + " should be placed is outside the map.");
				b.setAlive(false);
			}
		}
		
		ArrayList<Collision> newCollisions = new ArrayList<Collision>();
		return new GameState(newMap, newBullets, newTanks, newBases, newCollisions, eGame.getCurrentTick());
	}
	public static za.co.entelect.challenge.Action XActionToEAction(int xAction) {
		Action eAction = null;
		switch (xAction) {
			case GameAction.ACTION_MOVE_NORTH:	eAction = Action.DOWN;	break;
			case GameAction.ACTION_MOVE_EAST:	eAction = Action.RIGHT;	break;
			case GameAction.ACTION_MOVE_SOUTH:	eAction = Action.UP;	break;
			case GameAction.ACTION_MOVE_WEST:	eAction = Action.LEFT;	break;
			case GameAction.ACTION_FIRE:	eAction = Action.FIRE;	break;
			case GameAction.ACTION_NONE:	eAction = Action.NONE;	break;
		}
		return eAction;
	}
	public static int EDirectionToXRotation(za.co.entelect.challenge.Direction eDirection) {
		int rotation = -1;
		switch (eDirection.getValue()) {
			case "UP":		rotation = GameAction.ACTION_MOVE_SOUTH;	break;
			case "RIGHT":	rotation = GameAction.ACTION_MOVE_EAST;		break;
			case "DOWN":	rotation = GameAction.ACTION_MOVE_NORTH;	break;
			case "LEFT":	rotation = GameAction.ACTION_MOVE_WEST;		break;
		}
		return rotation;
	}
	public Unit getUnit(int unitCode) {
		switch (unitCode) {
			case 2:		return this.tanks[0];
			case 3:		return this.tanks[1];
			case 4:		return this.tanks[2];
			case 5:		return this.tanks[3];
			case 6:		return this.bullets[0];
			case 7:		return this.bullets[1];
			case 8:		return this.bullets[2];
			case 9:		return this.bullets[3];
			case 10:	return this.bases[0];
			case 11:	return this.bases[1];
			default:	return null;
		}
	}
	public String toString() {
		ArrayList<char[]> out = new ArrayList<char[]>(this.map.length);
		for (int y = 0; y < this.map.length; y++) {
			char[] line = new char[this.map[0].length];
			out.add(line);
			for (int x = 0; x < this.map[0].length; x++) {
				switch (this.map[y][x]) {
				case 0: 	line[x] = '_'; 	break;
				case 1: 	line[x] = '#'; 	break;
				case 2: 	line[x] = 'A'; 	break;
				case 3: 	line[x] = 'B'; 	break;
				case 4: 	line[x] = 'X'; 	break;
				case 5: 	line[x] = 'Y'; 	break;
				case 6: 	line[x] = '1'; 	break;
				case 7: 	line[x] = '2'; 	break;
				case 8: 	line[x] = '3'; 	break;
				case 9: 	line[x] = '4'; 	break;
				case 10: 	line[x] = 'C'; 	break;
				case 11: 	line[x] = 'Z'; 	break;
				default: 	line[x] = '?'; 	break;
				}
			}
		}
		for (int i = 0; i < this.tanks.length; i++) {
			if (!this.tanks[i].isAlive()) {
				continue;
			}
			Tank t = this.tanks[i];
			out.get(t.getPosition().y)[t.getPosition().x + 1] = (char) (t.getRotation() + '0'); 
		}
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < out.size(); y++) {
			for (int x = 0; x < out.get(y).length; x++) {
				sb.append(out.get(y)[x]);
			}
			sb.append('\n');			
		}
		
		return sb.toString();
	}
	public ArrayList<String> toStringList() {
		ArrayList<String> list = new ArrayList<String>();
		String gameString = this.toString();
		Scanner scanner = new Scanner(gameString);
		while (scanner.hasNextLine()) {
			list.add(scanner.nextLine());
		}
		scanner.close();
		return list;
	}

	public ArrayList<Integer> getTankActions(int unitCode) {
		if (Unit.isTank(unitCode)) {
			Tank me = (Tank) getUnit(unitCode);
			ArrayList<Integer> actions = new ArrayList<Integer>();

			if (me.isAlive()) {
				if (!this.bullets[unitCode - 2].isAlive()) {
					actions.add(GameAction.ACTION_FIRE);
				}
				for (int i = 0; i < 4; i++) {
					actions.add(GameAction.ACTION_MOVE_NORTH + i);
				}
			}
			actions.add(GameAction.ACTION_NONE);
			
			return actions;
		} else {		
			return null;
		}
	}
	//TODO add moves that move into an area that currently contains a bullet but will be cleared next round.
	public ArrayList<Integer> getNaiveTankMovesDirection(Point p) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		Point newP;
		newP = new Point(p.x, p.y + tankSize + 1);
		if (newP.y < this.map.length
				&& this.map[newP.y][newP.x] == 0
				&& this.map[newP.y][newP.x+1] == 0
				&& this.map[newP.y][newP.x+2] == 0
				&& this.map[newP.y][newP.x+3] == 0
				&& this.map[newP.y][newP.x+4] == 0) {
			moves.add(0);
		}
		newP = new Point(p.x, p.y-1);
		if (newP.y >= 0
				&& this.map[newP.y][newP.x] == 0
				&& this.map[newP.y][newP.x+1] == 0
				&& this.map[newP.y][newP.x+2] == 0
				&& this.map[newP.y][newP.x+3] == 0
				&& this.map[newP.y][newP.x+4] == 0) {
			moves.add(2);
		}
		newP = new Point(p.x + tankSize + 1, p.y);
		if (newP.x < this.map[0].length
				&& this.map[newP.y][newP.x] == 0
				&& this.map[newP.y+1][newP.x] == 0
				&& this.map[newP.y+2][newP.x] == 0
				&& this.map[newP.y+3][newP.x] == 0
				&& this.map[newP.y+4][newP.x] == 0) {
			moves.add(1);
		}
		newP = new Point(p.x-1, p.y);
		if (newP.x >= 0
				&& this.map[newP.y][newP.x] == 0
				&& this.map[newP.y+1][newP.x] == 0
				&& this.map[newP.y+2][newP.x] == 0
				&& this.map[newP.y+3][newP.x] == 0
				&& this.map[newP.y+4][newP.x] == 0) {
			moves.add(3);
		}
		return moves;
	}
	public ArrayList<Point> getNaiveTankMovesPoint(Point p) {
		ArrayList<Integer> directions = this.getNaiveTankMovesDirection(p);
		ArrayList<Point> points = new ArrayList<Point>(4);
		for (int i = 0; i < directions.size(); i++) {
			points.add(Util.movePoint(p, directions.get(i)));
		}
		return points;
	}
	public boolean isInMapTank(Point p) {
		if ((p.x + tankSize < this.map[0].length)
				&& (p.x >= 0)
				&& (p.y + tankSize < this.map.length)
				&& (p.y >= 0)) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isInMap(Point p) {
		if ((p.x < this.map[0].length)
				&& (p.x >= 0)
				&& (p.y < this.map.length)
				&& (p.y >= 0)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isInMap(Point p, int[][] map) {
		if ((p.x < map[0].length)
				&& (p.x >= 0)
				&& (p.y < map.length)
				&& (p.y >= 0)) {
			return true;
		} else {
			return false;
		}
	}
	public int getHeuristicValue(int heuristicType) {
		int h = 0;
		if (heuristicType == H_MINIMAX) {
			int numBase1 = 0;
			int numBase2 = 0;
			int numTank1 = 0;
			int numTank2 = 0;
			
			//
			// Count number of units
			//
			if (this.bases[0].isAlive()) {
				numBase1++;
			}
			if (this.bases[1].isAlive()) {
				numBase2++;
			}			
			if (this.tanks[0].isAlive()) {
				numTank1++;
			}
			if (this.tanks[1].isAlive()) {
				numTank1++;
			}
			if (this.tanks[2].isAlive()) {
				numTank2++;
			}
			if (this.tanks[3].isAlive()) {
				numTank2++;
			}
			
			if (this.rules == GameState.RULES_NORMAL) {
				//
				// H levels:
				// * 100k: 		Base existence
				// * 80k - 40k:	Tank existence			
				
				//
				// Subtract the current tickCount from the maximum to prevent procrastination. 
				//
				if (numBase1 != numBase2) {
					h = 100000 * Math.abs(numBase1 - numBase2);
					h += GameState.maxTurns - this.getTickCount();
				}
				
				if (numTank1 != numTank2) {
					if (numTank1 == 0 || numTank2 == 0) {
						if (numTank1 == 0) {
							h = -(numTank2 * 20000 + 40000);
						} else {
							h = (numTank1 * 20000 + 40000);
						}
					} else {
						h = (numTank1 - numTank2) * 40000;
					}
				}
				
				if (numBase1 != numBase2) {
					if (numBase1 > numBase2) {
						return h;
					} else {
						return -h;	
					}
				}
			} else if (this.rules == GameState.RULES_TOTAL_DESTRUCTION) {
				int numUnits1 = numBase1 + numTank1;
				int numUnits2 = numBase2 + numTank2;				
				
				if (numUnits1 != numUnits2) {
					if (numUnits1 == 0 || numUnits2 == 0) {
						if (numUnits1 == 0) {
							h = -100000;
						} else {
							h = 100000;
						}
					} else {
						h = (numUnits1 - numUnits2) * 30000;
					}
				}
				h += GameState.maxTurns - this.getTickCount();
			}
			
			//
			// Being close to the enemy base line is a good thing. Bonus points for aiming in the right direction!
			// A Tank on the base line or 1 unit away from it is rewarded in addition to the normal reward.
			//
			//TODO: base these rewards on true distances, not Manhattan distance
			for (int i = 0; i < tanks.length; i++) {
				if (!tanks[i].isAlive()) {
					continue;
				}
				int score = 0;
				Point target = null;
				if (i < 2) {
					target = new Point(getUnit(Unit.BASE2).getPosition());
				} else {
					target = new Point(getUnit(Unit.BASE1).getPosition());
				}
				
				Point tankCenter = new Point(tanks[i].getPosition());
				tankCenter.translate(2, 2);
				boolean onTarget = false;
				
				if (target.y >= 2 && target.y < this.map.length - 2) {
					score = (this.map.length - Math.abs(tankCenter.y - target.y))*3 - (Math.abs(tankCenter.x - target.x));					
					if (Math.abs(tankCenter.y - target.y) <= 1) {
						score += 3;
					}
					if (tankCenter.y == target.y) {
						score += 3;
						onTarget = true;
					}
				} else if (target.x >= 2 && target.x < this.map[0].length - 2) {
					score = (this.map[0].length - Math.abs(tankCenter.x - target.x))*3 - (Math.abs(tankCenter.y - target.y));
					if (Math.abs(tankCenter.x - target.x) <= 1) {
						score += 3;
					}
					if (tankCenter.x == target.x) {
						score += 3;
						onTarget = true;
					}
				} else {
					score = this.map.length + this.map[0].length - Util.mDist(tankCenter, target);	
				}				
				score *= 10;
				
				//
				// Rotational award = 10
				// Only applies if you're on the line
				//
				if (onTarget) {
					int minDist = Integer.MAX_VALUE;
					int bestRotation = 0;
					for (int j = 0; j < 4; j++) {
						Point potentialBullet = Util.movePointDist(tankCenter, j, 2);
						int dist = Util.mDist(potentialBullet, target);
						if (dist < minDist) {
							minDist = dist;
							bestRotation = j;
						}
					}				
					if (tanks[i].getRotation() == bestRotation) {
						score += 10;
					}
				}
				//TODO: There is a conflict between standing still and rotating to fire. 
				//TODO: Bots still get stuck. 
				
				
//				if (target.x < 2) {
//					target.x = 2;
//				}
//				if (target.x >= this.map[0].length - 2) {
//					target.x = this.map[0].length - 3;
//				}
//				if (target.y < 2) {
//					target.y = 2;
//				}				
//				if (target.y >= this.map.length - 2) {
//					target.y = this.map.length - 3;
//				}
//
//				// Tank offset
//				target.translate(-2, -2);
//				
//				if (i < 2) {
//					score = 200 - Util.mDist(tanks[i].getPosition(), target);					
//				} else {
//					score = -200 + Util.mDist(tanks[i].getPosition(), target);
//				}
				if (i < 2) {				
					h += score;
				} else {
					h -= score;
				}
			}
			
			//
			// Firing at enemy targets is a good thing
			// 
			for (int i = 0; i < bullets.length; i++) {
				if (!bullets[i].isAlive()) {
					continue;
				}
				//if (i == 2 && bullets[i].getPosition().y == 7 && (bullets[i].getRotation() % 2 == 1)) {
				if (i == 0 && bullets[i].getPosition().x == 14 && (bullets[i].getRotation() % 2 == 1)) {
					int abc = 0;
					abc = abc == 0 ? abc++ : abc--;
				}
				int score = 0;
				int walls = 0;
				int empty = 0;
				Point start = new Point(bullets[i].getPosition());
				Point end = Util.movePointDist(bullets[i].getPosition(), bullets[i].getRotation(), 1000, this);
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
					if (this.map[start.y][start.x] == Unit.EMPTY) {
						empty++;
					} else if (this.map[start.y][start.x] == Unit.WALL) {
						walls++;
					} else {
						if (Unit.isBase(this.map[start.y][start.x])) {
							if (Unit.BASE1 + i/2 == this.map[start.y][start.x]) {
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
						} else if (Unit.isTank(this.map[start.y][start.x])) {
							boolean friendlyFire = false;
							if (i < 2) {
								if ((Unit.TANK1A == this.map[start.y][start.x] 
										|| Unit.TANK1B == this.map[start.y][start.x]) 
										&& walls == 0) {
									friendlyFire = true;
								}
							} else {
								if ((Unit.TANK2A == this.map[start.y][start.x] 
										|| Unit.TANK2B == this.map[start.y][start.x]) 
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
						}
					}
				}
				if (i < 2) {
					h += score;
				} else {
					h -= score;
				}
			}


			//
			// Standing still is a BAD thing!
			//
			for (int i = 0; i < tanks.length; i++) {
				if (!tanks[i].isAlive()) {
					continue;
				}
				int score = 0;
				Point currentP = tanks[i].getPosition();
				Point prevP = tanks[i].getPrevPosition();
				if (currentP.equals(prevP)) {
					score  = -9;
				}
				if (i < 2) {
					h += score;
				} else {
					h -= score;
				}
			}
			
		}		
		return h;
	}
	//TODO: 1.  Assume bullet explosions damage only walls
	//TODO: 2.  Assume that explosions happen when a bullet hits anything
	//TODO: 3.  Assume bullets that hit the edge of the map disappear
	//TODO: 4.  Assume that more than one bullet can hit the same object at the same time (e.g a WALL)
	//TODO: 5.  Assume a Tank can move into an area that had a wall or base (NOT tank) in it the previous round
	//TODO: 6.  Assume Tanks move in a 100% turn based way
	//TODO: 7.  Assume when a Tank tries to move into a bullet it is destroyed
	//TODO: 8.  Assume Bullets are moved twice at the start of the Round
	//TODO: 9.  Assume you can always have one bullet. A tank can fire a bullet if his bullet will be destroyed next round.
	//TODO: 10. Assume you cannot fire on the edge of the map.
	//TODO: 11. Assume Bullets will collide if they never share a space
	//TODO: 12. Assume that the base will always be on the edge of the map.
	//TODO: 13. In cases where Bullet collisions involve Tanks, the Tanks are always destroyed. This probably isn't correct but it is safe.
	public void nextTick() {
		//
		// Optionally record old GameState
		//
		if (this.saveActions) {
			this.gameLog.add(this.clone());
		}
		
		this.tickCount++;

		
		if (this.getTickCount() > GameState.maxTurns) {
			this.status = GameState.STATUS_DRAW;
			return;
		}
		
		//
		// Hash OUT all the units.
		//
		this.hashCode = addUnitsToHash(this.hashCode);
		
		//
		// Clear all collisions, they last only one tick
		//
		collisions.clear();
 
		//
		// A list of all points (excluding Tanks) that was destroyed by Bullets.
		//
		ArrayList<Point> deadListPoint = new ArrayList<Point>();
		
		//
		// A list of all points where Bullets collided.
		//
		ArrayList<Point> bulletCollisionList = new ArrayList<Point>();

		//
		// Bullets move twice
		//
		moveBullets(deadListPoint, bulletCollisionList);
		moveBullets(deadListPoint, bulletCollisionList);
		
		//
		// Check tank moves
		//
		Tank t;
		for (int i = 0; i < this.tanks.length; i++) {
			t = this.tanks[i];
			if (!t.isAlive()) {
				continue;
			}
			if (!t.hasNextAction()) {
				System.err.println("TANK[" + i + "] has no action set, doing NONE");
				t.setNextAction(GameAction.ACTION_NONE);
			}
			if (t.getNextAction() < GameAction.ACTION_MOVE_NORTH || t.getNextAction() > GameAction.ACTION_NONE) {
				System.err.println("TANK[" + i + "] has invalid action set, doing NONE");
				t.setNextAction(GameAction.ACTION_NONE);
			}
		}
		
		//
		// Optionally record moves
		//
		if (this.saveActions) {
			int[] performedActions = new int[4];
			for (int i = 0; i < 4; i++) {
				performedActions[i] = getTanks()[i].getNextAction();
			}
			this.actionLog.add(performedActions);
		}

		//
		// Remove old Tank positions. This is only needed if Tanks can really move into a position 
		// that was previously occupied by another tank but the Tank has moved (but not if it 
		// was destroyed)
		//
		
		
		
		//
		// Move tanks
		//
		for (int i = 0; i < this.tanks.length; i++) {
			t = this.tanks[i];
			if (!t.isAlive()) {
				continue;
			}
			if (t.getNextAction() == GameAction.ACTION_NONE) {
				t.setPosition(t.getPosition());
			} else if (t.getNextAction() <= GameAction.ACTION_MOVE_WEST) {
				final Point nextP = Util.movePoint(t.getPosition(), t.getNextAction());
				final Point oldP = t.getPosition();

				t.setRotation(t.getNextAction());
				final Point toAdd;
				final Point toClear;
				if (t.getNextAction() == GameAction.ACTION_MOVE_NORTH || t.getNextAction() == GameAction.ACTION_MOVE_SOUTH) {				
					if (t.getNextAction() == GameAction.ACTION_MOVE_NORTH) {
						toAdd = new Point(oldP.x, oldP.y + tankSize);
						toClear = new Point(oldP.x, oldP.y);
					} else {
						toAdd = new Point(nextP.x, nextP.y);
						toClear = new Point(nextP.x, nextP.y + tankSize);
					}
					boolean obstructed = false;
					for (int x = toAdd.x; x < toAdd.x + tankSize; x++) {
						if (!isInMap(new Point(x, toAdd.y)) || !Unit.isBulletOrEmpty(this.map[toAdd.y][x])) {
							obstructed = true;
							break;
						}
					}
					//
					// Finally move the Tank N/S
					//
					if (!obstructed) {
						t.setPosition(nextP);
						for (int x = 0; x < tankSize; x++) {
							if (this.map[toAdd.y][toAdd.x + x] == Unit.EMPTY) {
								this.map[toAdd.y][toAdd.x + x] = Unit.TANK1A + i;
							}
							if (this.map[toClear.y][toClear.x + x] == (Unit.TANK1A + i)) {
								this.map[toClear.y][toClear.x + x] = 0;
							}
						}
					} else {
						t.setPosition(t.getPosition());
					}
				} else {				
					if (t.getNextAction() == GameAction.ACTION_MOVE_EAST) {
						toAdd = new Point(oldP.x + tankSize, oldP.y);
						toClear = new Point(oldP.x, oldP.y);
					} else {
						toAdd = new Point(nextP.x, nextP.y);
						toClear = new Point(nextP.x + tankSize, nextP.y);
					}
					boolean obstructed = false;
					for (int y = toAdd.y; y < toAdd.y + tankSize; y++) {
						if (!isInMap(new Point(toAdd.x, y)) || !Unit.isBulletOrEmpty(this.map[y][toAdd.x])) {
							obstructed = true;
							break;
						}
					}					
					//
					// Finally move the Tank E/W
					//
					if (!obstructed) {
						t.setPosition(nextP);
						for (int y = 0; y < tankSize; y++) {
							if (this.map[toAdd.y + y][toAdd.x] == Unit.EMPTY) {
								this.map[toAdd.y + y][toAdd.x] = Unit.TANK1A + i;
							}
							if (this.map[toClear.y + y][toClear.x] == (Unit.TANK1A + i)) {
								this.map[toClear.y + y][toClear.x] = 0;
							}
						}
					} else {
						t.setPosition(t.getPosition());
					}
				}
			}
		}		

		//
		// Check if any tanks have been hit by old Bullets or Bullet collisions.
		//
		destroyTankBulletHits(bulletCollisionList);

		//
		// Fire new Bullets
		//
		Bullet[] newBullets = new Bullet[4];
		for (int i = 0; i < this.tanks.length; i++) {
			t = this.tanks[i];
			if (!t.isAlive()) {
				continue;
			}
			if (t.getNextAction() == GameAction.ACTION_FIRE) {
				if (!bullets[i].isAlive()) {
					Point bulletPosition = new Point(t.getPosition().x + tankSize/2, t.getPosition().y + tankSize/2);
					bulletPosition = Util.movePoint(bulletPosition, t.getRotation());
					bulletPosition = Util.movePoint(bulletPosition, t.getRotation());
					if (isInMap(bulletPosition)) {
						//
						// The Bullet is inside the parent Tank now
						//
						bullets[i] = new Bullet(bulletPosition, t.getRotation(), true);
						newBullets[i] = bullets[i];
					}
				}
			}
		}
		
		for (int i = 0; i < this.tanks.length; i++) {
			this.tanks[i].clearNextAction();
		}
		
		//
		// Move new Bullets
		//
		for (int j = 0; j < newBullets.length; j++) {
			if (newBullets[j] != null) {
				moveBulletsCheckNonTankCollisions(newBullets[j], deadListPoint, j);
			}
		}
		
		//
		// Destroy everything that was hit by a new Bullet
		//
		destroyNonTankBulletHits(deadListPoint, bulletCollisionList);

		//
		// The game might be over now. Possibly return
		//
		
		//
		// Check if any tanks have been hit by old Bullets
		//
		destroyTankBulletHits(bulletCollisionList);		
		
		//TODO: test bullets following each other head to tail
		//TODO: test tanks following each other head to tail. Will fail
		//TODO: test tanks following each other head to tail with 1 space.
		//TODO: test let two bullets hit the same 5 unit wall 
		//TODO: test let a tank alternate firing and moving against a wall
		//TODO: test shoot a bullet out of the air
		

		//TODO: when tank is cleared remove only map where = tank. Bullet might be on top!
		

		//	
		// End-game conditions
		//	
		this.setStatusAndStage();
		
		//
		// Hash IN all the units.
		//
		this.hashCode = addUnitsToHash(this.hashCode);
		
		if (this.isDebugMode()) {
			//
			// Test that Tank area == Tank ID 
			//
			for (int i = 0; i < this.tanks.length; i++) {
				t = this.tanks[i];
				if (!t.isAlive()) {
					continue;
				}
				for (int y = t.getPosition().y; y < t.getPosition().y + tankSize; y++) {
					for (int x = t.getPosition().x; x < t.getPosition().x + tankSize; x++) {
						if (this.map[y][x] != Unit.TANK1A + i) {
							System.err.println("FATAL ERROR: A Tank's area must be set to that Tank's ID!");
						}
					}
				}
			}			

			long newHash = generateHash();
			if (hashCode != newHash) {
				System.err.println("FATAL ERROR: hashCode does not match generateHash()!");
			}
		}		
	}
	private void setStatusAndStage() {
		//
		// Count number of units
		//	
		int numBase1 = 0;
		int numBase2 = 0;
		int numTank1 = 0;
		int numTank2 = 0;
		
		if (this.bases[0].isAlive()) {
			numBase1++;
		}
		if (this.bases[1].isAlive()) {
			numBase2++;
		}			
		if (this.tanks[0].isAlive()) {
			numTank1++;
		}
		if (this.tanks[1].isAlive()) {
			numTank1++;
		}
		if (this.tanks[2].isAlive()) {
			numTank2++;
		}
		if (this.tanks[3].isAlive()) {
			numTank2++;
		}

		if (this.rules == RULES_NORMAL) {
			if (numBase1 != numBase2) {
				if (numBase1 > numBase2) {
					this.status = STATUS_PLAYER1_WINS;
				} else {
					this.status = STATUS_PLAYER2_WINS;
				}
			} else if (numBase1 == 0 && numBase2 == 0) {
				this.status = STATUS_DRAW;
			} else if (numTank1 == 0 && numTank2 == 0) {
				this.status = STATUS_DRAW;
			} else {
				//
				// Game is still active, split it into stages.
				//
				this.status = STATUS_ACTIVE;
	
				if (numTank1 == 2 && numTank2 == 2) {
					this.stage = STAGE_EQUAL_T2;
					
				} else if (numTank1 == 1 && numTank2 == 1) {
					this.stage = STAGE_EQUAL_T1;
					
				} else if (numTank1 == 2 && numTank2 == 1) {
					this.stage = STAGE_P1_ADVANTAGE;
					
				} else if (numTank1 == 1 && numTank2 == 2) {
					this.stage = STAGE_P2_ADVANTAGE;
					
				} else if (numTank1 == 2 && numTank2 == 0) {
					this.stage = STAGE_P1_ENDGAME_T2;
					
				} else if (numTank1 == 1 && numTank2 == 0) {
					this.stage = STAGE_P1_ENDGAME_T1;
					
				} else if (numTank1 == 0 && numTank2 == 2) {
					this.stage = STAGE_P2_ENDGAME_T2;
					
				} else if (numTank1 == 0 && numTank2 == 1) {
					this.stage = STAGE_P2_ENDGAME_T1;
					
				}		
			}
		} else if (this.rules == RULES_TOTAL_DESTRUCTION) {
			int numUnits1 = numBase1 + numTank1;
			int numUnits2 = numBase2 + numTank2;
			
			if (numUnits1 != numUnits2) {
				if (numUnits2 == 0) {
					this.status = STATUS_PLAYER1_WINS;
				} else if (numUnits1 == 0) {
					this.status = STATUS_PLAYER2_WINS;
				} else {
					// Game is still active!
					this.status = STATUS_ACTIVE;
				}
			} else if (numUnits1 == 0 && numUnits2 == 0) {
				this.status = STATUS_DRAW;
			} else {
				// Game is still active!
				this.status = STATUS_ACTIVE;
			}
		}
	}
	private void moveBullets(ArrayList<Point> deadListPoint, ArrayList<Point> bulletCollisionList) {
		
		Point[] oldBulletPositions = new Point[4];
		for (int i = 0; i < oldBulletPositions.length; i++) {
			if (bullets[i].isAlive()) {
				oldBulletPositions[i] = new Point(bullets[i].position);				
			}			 
		}
		
		//
		// Clear old Bullet positions
		//
		Unit me;
		for (int i = 0; i < bullets.length; i++) {
			me = bullets[i];
			if (!me.isAlive()) {
				continue;
			}
			//
			// This Bullet might be on top a Tank right now because the Bullet was moved twice.
			//
			int tankBelow = this.getTank(me.position);
			if (tankBelow == -1) {
				this.map[me.position.y][me.position.x] = 0;
			} else {
				this.map[me.position.y][me.position.x] = tankBelow;
			}
		}
		
		//
		// Move old Bullets
		//
		Bullet b = null;
		for (int i = 0; i < bullets.length; i++) {
			b = bullets[i];
			if (!b.isAlive()) {
				continue;
			}
			moveBulletsCheckNonTankCollisions(b, deadListPoint, i);
		}

		//
		// Check for the case where two Bullets passed through each other.
		// In this case destroy both bullets.
		//
		Point[] newBulletPositions = new Point[4];
		for (int i = 0; i < newBulletPositions.length; i++) {
			if (bullets[i].isAlive()) {
				newBulletPositions[i] = new Point(bullets[i].position);				
			}			 
		}
		for (int i = 0; i < oldBulletPositions.length; i++) {
			for (int j = 0; j < newBulletPositions.length; j++) {
				if (i != j 
						&& oldBulletPositions[i] != null
						&& oldBulletPositions[j] != null
						&& newBulletPositions[i] != null
						&& newBulletPositions[j] != null
						&& oldBulletPositions[i].equals(newBulletPositions[j])
						&& oldBulletPositions[j].equals(newBulletPositions[i])) {
					deadListPoint.add(newBulletPositions[i]);
					deadListPoint.add(newBulletPositions[j]);
					collisions.add(new Collision(new Point(	newBulletPositions[i].x - tankSize/2, 
															newBulletPositions[i].y - tankSize/2), 0));
					collisions.add(new Collision(new Point(	newBulletPositions[j].x - tankSize/2, 
															newBulletPositions[j].y - tankSize/2), 0));
				}
			}
		}
		
		//
		// Destroy everything that was hit by an old Bullet. Tanks are hit later.
		//
		destroyNonTankBulletHits(deadListPoint, bulletCollisionList);
	}
	private void destroyTankBulletHits(ArrayList<Point> bulletCollisionList) {
		Tank t = null;
		for (int i = 0; i < this.tanks.length; i++) {
			t = this.tanks[i];
			if (!t.isAlive()) {
				continue;
			}
			boolean destroy = false;
			for (int y = t.getPosition().y; y < t.getPosition().y + tankSize; y++) {
				for (int x = t.getPosition().x; x < t.getPosition().x + tankSize; x++) {
					if (Unit.isBullet(this.map[y][x])) {
						destroy = true;
						y = t.getPosition().y + tankSize;
						break;
					}
				}
			}
			if (!destroy) {
				for (Point collision : bulletCollisionList) {
					if (getTank(collision) == Unit.TANK1A + i) {
						destroy = true;
						//
						// Fill in the Tank on this point to enable destruction to complete.
						//
						this.map[collision.y][collision.x] = Unit.TANK1A + i;
					}
				}
			}
			if (destroy) {
				//
				// This Tank and everything on top of it (more Bullets) is killed. Set to alive = false and EMPTY
				//
				for (int y = t.getPosition().y; y < t.getPosition().y + tankSize; y++) {
					for (int x = t.getPosition().x; x < t.getPosition().x + tankSize; x++) {
						if (Unit.isTank(this.map[y][x]) || Unit.isBullet(this.map[y][x])) {
							Unit obstacle = getUnit(this.map[y][x]);
							obstacle.setAlive(false);
							this.map[y][x] = Unit.EMPTY;
						} else {
							System.err.println("FATAL ERROR: Tank area must be either Tank or Bullet");
						}
					}
				}
			}
		}
	}
	private void destroyNonTankBulletHits(ArrayList<Point> deadListPoint, ArrayList<Point> bulletCollisionList) {
		Point p;
		for (int i = 0; i < deadListPoint.size(); i++) {
			p = deadListPoint.get(i);
			if (!Unit.isEmptyOrWall(this.map[p.y][p.x])) {
				if (Unit.isBullet(this.map[p.y][p.x])) {
					bulletCollisionList.add(new Point(p));
				}
				Unit obstacle = getUnit(this.map[p.y][p.x]);
				obstacle.setAlive(false);
			} else if (this.map[p.y][p.x] == Unit.WALL) {				
				//
				// Hash OUT this destroyed Wall
				//
				this.hashCode ^= Util.zTable[p.y * this.map[0].length + p.x][Util.Z_WALL];				
				this.hashCode ^= Util.zTable[p.y * this.map[0].length + p.x][Util.Z_EMPTY];
			}
			this.map[p.y][p.x] = 0;
		}
		deadListPoint.clear();		
	}
	private void moveBulletsCheckNonTankCollisions(Bullet me, ArrayList<Point> deadListPoint, int i) {
		final Point nextP = Util.movePoint(me.getPosition(), me.getRotation());
		if (isInMap(nextP)) {
			me.setPosition(nextP);
			int obstacleUnitCode = this.map[nextP.y][nextP.x];
			if (Unit.isTankOrEmpty(obstacleUnitCode)) {
				this.map[nextP.y][nextP.x] = Unit.BULLET_TANK1A + i;
			} else {
				me.setAlive(false);
				//
				// The Bullet has hit a Base, a Wall or another Bullet
				//
				
				//
				// If the obstacle is a Bullet and there is a tank underneath 
				//
				//if (obstacleUnitCode == Unit.WALL) {}
				
				//
				// Destroy this Unit and 5 perpendicular WALL Units
				//
				
				//if (obstacleUnitCode == Unit.WALL) {
				if (me.getRotation() == GameAction.ACTION_MOVE_NORTH || me.getRotation() == GameAction.ACTION_MOVE_SOUTH) {
					deadListPoint.add(nextP);
					for (int x = nextP.x - (tankSize/2); x <= nextP.x + (tankSize/2); x++) {
						if (x >= 0
								&& x < this.map[0].length 
								&& this.map[nextP.y][x] == Unit.WALL) {
							deadListPoint.add(new Point(x, nextP.y));
						}
					}
				} else {
					deadListPoint.add(nextP);
					for (int y = nextP.y - (tankSize/2); y <= nextP.y + (tankSize/2); y++) {
						if (y >= 0
								&& y < this.map.length 
								&& this.map[y][nextP.x] == Unit.WALL) {
							deadListPoint.add(new Point(nextP.x, y));
						}
					}
				}
				//}
				//else if (Unit.isBase(obstacleUnitCode)) {
				//	deadListPoint.add(nextP);
				//} else if (Unit.isBullet(obstacleUnitCode)) {
				//	deadListPoint.add(nextP);
				//} 
				collisions.add(new Collision(new Point(nextP.x - tankSize/2, nextP.y - tankSize/2), 0));
			}
		} else {
			//
			// Bullet is outside map and will cause collision
			//
			me.setAlive(false);
			
			collisions.add(new Collision(new Point(me.getPosition().x - tankSize/2, me.getPosition().y - tankSize/2), 0));
		}
	}
	/**
	 * Find a Tank based on Tank.position
	 * @param p
	 * @return
	 */
	private int getTank(Point p) {
		Point me = null;
		for (int i = 0; i < 4; i++) {
			if (!this.tanks[i].isAlive()) {
				continue;
			}
			me = this.tanks[i].getPosition();
			if ((p.x >= me.x && p.x < me.x + GameState.tankSize)
					&& (p.y >= me.y && p.y < me.y + GameState.tankSize)) {
				return Unit.TANK1A + i;
			}
		}
		return -1;
	}
	
	public static int getTank(Point p, Tank[] tanks) {
		Point me = null;
		for (int i = 0; i < 4; i++) {
			if (!tanks[i].isAlive()) {
				continue;
			}
			me = tanks[i].getPosition();
			if ((p.x >= me.x && p.x < me.x + GameState.tankSize)
					&& (p.y >= me.y && p.y < me.y + GameState.tankSize)) {
				return Unit.TANK1A + i;
			}
		}
		return -1;
	}
	
	public long generateHash() {
		long newHashCode = 0;
		for (int y = 0; y < this.map.length; y++) {
			for (int x = 0; x < this.map[0].length; x++) {
				if (this.map[y][x] == Unit.WALL) {
					newHashCode ^= Util.zTable[y * this.map[0].length + x][Util.Z_WALL];
				} else {
					newHashCode ^= Util.zTable[y * this.map[0].length + x][Util.Z_EMPTY];
				}
			}
		}
		
		newHashCode = addUnitsToHash(newHashCode);
		
		return newHashCode;
	}
	
	private long addUnitsToHash(long newHashCode) {
		Unit me = null;
		for (int i = 0; i < this.bullets.length; i++) {
			me = this.bullets[i];
			if (me.isAlive()) {
				newHashCode ^= Util.zTable[me.getPosition().y * this.map[0].length + me.getPosition().x][Util.Z_EMPTY];
				newHashCode ^= Util.zTable[me.getPosition().y * this.map[0].length + me.getPosition().x][Util.Z_BULLET_TANK1A + (i*4) + me.getRotation()];
			}
		}
		
		for (int i = 0; i < this.tanks.length; i++) {
			me = this.tanks[i];
			if (me.isAlive()) {
				newHashCode ^= Util.zTable[me.getPosition().y * this.map[0].length + me.getPosition().x][Util.Z_EMPTY];
				newHashCode ^= Util.zTable[me.getPosition().y * this.map[0].length + me.getPosition().x][Util.Z_TANK1A + (i*4) + me.getRotation()];
			}
		}
		
		for (int i = 0; i < this.bases.length; i++) {
			me = this.bases[i];
			if (me.isAlive()) {
				newHashCode ^= Util.zTable[me.getPosition().y * this.map[0].length + me.getPosition().x][Util.Z_EMPTY];
				newHashCode ^= Util.zTable[me.getPosition().y * this.map[0].length + me.getPosition().x][Util.Z_BASE1 + i];
			}
		}
		
		return newHashCode;
	}
	@Override
	public int hashCode() {
		return (int)this.hashCodeLong();
	}
	
	public long hashCodeLong() {
		return hashCode;
	}
	
	public void setMapType() {
		this.mapType = GameState.identifyMapType(this.map);
		if (this.debugMode) {
			System.out.println("MapType set to type " + this.mapType);
		} 
	}
	
	static int getRotationFromChar(char c) {
		int rotation = 2;		
		if (Character.isDigit(c)) {
			int n = Character.getNumericValue(c);
			if (n >= 0 && n <= 3) {
				rotation = n;
			}
		}
		return rotation;
	}
	public static ArrayList<String> readGameFromFile(String filename) {
		Scanner in = null;
		try {
			in = new Scanner(new File("./assets/"+filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> file = new ArrayList<String>(); 
		while (in.hasNext()) {
			String line = in.nextLine();
			file.add(line);
		}
		in.close();
		
		return file;
	}
	public static GameState newGame(String filename) {		
		return newGame(GameState.readGameFromFile(filename));
	}
	public static GameState newGame(ArrayList<String> file) {		
		int[][] map = new int[file.size()][file.get(0).length()];
		Tank[] tanks = new Tank[4];
		Base[] bases = new Base[2];
		Bullet[] bullets = new Bullet[4];
		for (int i = 0; i < bullets.length; i++) {
			bullets[i] = new Bullet(new Point(0,0), 0, false);
		}
		ArrayList<Collision> collisions = new ArrayList<Collision>();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				char c = file.get(y).charAt(x);
				if (c == '_') {
					map[y][x] = 0;
				} else if (c == '#') {
					map[y][x] = 1;
				} else if (c == 'A') {
					if (tanks[0] == null) {
						tanks[0] = new Tank(new Point(x,y), GameState.getRotationFromChar(file.get(y).charAt(x+1)), -1);
						x++;
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'B') {
					if (tanks[1] == null) {
						tanks[1] = new Tank(new Point(x,y), GameState.getRotationFromChar(file.get(y).charAt(x+1)), -1);
						x++;
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'X') {
					if (tanks[2] == null) {
						tanks[2] = new Tank(new Point(x,y), GameState.getRotationFromChar(file.get(y).charAt(x+1)), -1);
						x++;
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'Y') {
					if (tanks[3] == null) {
						tanks[3] = new Tank(new Point(x,y), GameState.getRotationFromChar(file.get(y).charAt(x+1)), -1);
						x++;
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'C') {
					bases[0] = new Base(new Point(x,y), 2);
					map[y][x] = Unit.BASE1;
				} else if (c == 'Z') {
					bases[1] = new Base(new Point(x,y), 2);
					map[y][x] = Unit.BASE2;
				} else if (c == '0') {
					bullets[0] = new Bullet(new Point(x,y), 2);
				} else if (c == '1') {
					bullets[1] = new Bullet(new Point(x,y), 2);
				} else if (c == '2') {
					bullets[2] = new Bullet(new Point(x,y), 2);
				} else if (c == '3') {
					bullets[3] = new Bullet(new Point(x,y), 2);
				} else if (c == '_') {
					map[y][x] = 0;
				} else {
					System.err.println("UNKNOWN SYMBOL IN MAP.TXT: "+c);
				}
			}
		}
		for (int i = 0; i < tanks.length; i++) {
			Tank t = tanks[i];
			if (tanks[i] == null) {
				tanks[i] = new Tank(new Point(0,0), 2, -1, false);
				continue;
			}
			for (int y2 = 0; y2 < tankSize; y2++) {
				for (int x2 = 0; x2 < tankSize; x2++) {
					map[t.getPosition().y+y2][t.getPosition().x+x2] = Unit.TANK1A+i;
				}
			}
		}
		for (int i = 0; i < bases.length; i++) {
			if (bases[i] == null) {
				bases[i] = new Base(new Point(0,0), 2, false);
				continue;
			}
		}
		GameState newGame = new GameState(map, bullets, tanks, bases, collisions, 0);
		return newGame;
	}
	
	/**
	 * Match up the provided map with one of the Entelect maps.
	 */
	public static int identifyMapType(int[][] unknownMap) {
		String[] maps = {
			"mapE1.txt",
			"mapE2.txt",
			"mapE3.txt",
			"mapE4.txt",
			"mapE5.txt",
			"mapE6.txt",
			"mapE7.txt",
			"mapE8.txt",
		};
		
		int[] mapTypes = {
			GameState.MAP_TYPE_E0,
			GameState.MAP_TYPE_E1,
			GameState.MAP_TYPE_E2,
			GameState.MAP_TYPE_E3,
			GameState.MAP_TYPE_E4,
			GameState.MAP_TYPE_E5,
			GameState.MAP_TYPE_E6,
			GameState.MAP_TYPE_E7,
		};
		
		for (int i = 0; i < maps.length; i++) {
			GameState gameState = GameState.newGame(maps[i]);
			int[][] map = gameState.getMap();			

			int numEmptyOrWall = 0;
			for (int y = 0; y < map.length; y++) {
				for (int x = 0; x < map[0].length; x++) {
					if (Unit.isEmptyOrWall(map[y][x])) {
						numEmptyOrWall++;
					}
				}
			}

			int numEqual = 0;
			for (int y = 0; y < unknownMap.length; y++) {
				for (int x = 0; x < unknownMap[0].length; x++) {
					if (gameState.isInMap(new Point(x,y))) {
						if (Unit.isEmptyOrWall(map[y][x]) && map[y][x] == unknownMap[y][x]) {
							numEqual++;
						}
					}
				}
			}
			
			if (numEqual > 0.9 * (numEmptyOrWall)) {
				return mapTypes[i];
			}				
		}
		return GameState.MAP_TYPE_UNKNOWN;
	}
	
}