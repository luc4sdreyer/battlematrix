package za.co.entelect.competition;

import java.awt.Point;
import java.util.ArrayList;

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
 * @author luc4s_000
 *
 */

public class GameState {
	public static final int H_MINIMAX = 0;
	public static final int NEG_INF = -1000000;
	public static final int POS_INF = 1000000;
	
	public static final int STATUS_ACTIVE = 0;
	public static final int STATUS_PLAYER1_WINS = 1;
	public static final int STATUS_PLAYER2_WINS = 2;
	public static final int STATUS_DRAW = 3;
	
	private int[][] map;
	private ArrayList<Collision> collisions;
	private Tank[] tanks;
	private Bullet[] bullets;
	private Base[] bases;
	private int tickCount = 0;
	private int status = 0;
	public final static int tankSize = 5;
	public GameState(int[][] map, Bullet[] bullets, Tank[] tanks, Base[] bases, ArrayList<Collision> collisions, int tickCount, int status) {
		super();
		this.map = map;
		this.bullets = bullets;
		this.collisions = collisions;
		this.tanks = tanks;
		this.bases = bases;
		this.tickCount = tickCount;
		this.status = status;
	}
	public int[][] getMap() {
		return map;
	}
	public void setMap(int[][] map) {
		this.map = map;
	}
	public Bullet[] getBullets() {
		return bullets;
	}
	public void setBullets(Bullet[] bullets) {
		this.bullets = bullets;
	}
	public Tank[] getTanks() {
		return tanks;
	}
	public void setTanks(Tank[] tanks) {
		this.tanks = tanks;
	}	
	public Base[] getBases() {
		return bases;
	}
	public void setBases(Base[] bases) {
		this.bases = bases;
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
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isActive() {
		if (this.status == GameState.STATUS_ACTIVE) {
			return true;
		} else {
			return false;
		}
	}
	public GameState clone() {
		int[][] newMap = new int[this.map.length][this.map[0].length];
		for (int y = 0; y < newMap.length; y++) {
			//newMap[y] = Arrays.copyOf(this.map[y], this.map[0].length);
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
		
		return new GameState(newMap, newBullets, newTanks, newBases, newCollisions, this.tickCount, this.status);
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
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < this.map.length; y++) {
			for (int x = 0; x < this.map[0].length; x++) {
				switch (this.map[y][x]) {
				case 0: 	sb.append('_'); 	break;
				case 1: 	sb.append('#'); 	break;
				case 2: 	sb.append('A'); 	break;
				case 3: 	sb.append('B'); 	break;
				case 4: 	sb.append('X'); 	break;
				case 5: 	sb.append('Y'); 	break;
				case 6: 	sb.append('1'); 	break;
				case 7: 	sb.append('2'); 	break;
				case 8: 	sb.append('3'); 	break;
				case 9: 	sb.append('4'); 	break;
				case 10: 	sb.append('C'); 	break;
				case 11: 	sb.append('Z'); 	break;
				default: 	sb.append("UNKNOWN"); 	break;
				}
			}
			if (y == this.map.length - 1) {
				sb.append(Integer.toString(tickCount));
			}	
			sb.append("\n");
		}
		return sb.toString();
	}
//	public ArrayList<Point> getTankMoves(Point p) {
//		ArrayList<Point> moves = new ArrayList<Point>();
//		Point newP;
//		newP = new Point(p.x, p.y+1);
//		if (newP.y < this.map.length && this.map[newP.y][newP.x] == 0) {
//			moves.add(newP);
//		}
//		newP = new Point(p.x+1, p.y);
//		if (newP.x < this.map[0].length && this.map[newP.y][newP.x] == 0) {
//			moves.add(newP);
//		}
//		newP = new Point(p.x, p.y-1);
//		if (newP.y >= 0 && this.map[newP.y][newP.x] == 0) {
//			moves.add(newP);
//		}
//		newP = new Point(p.x-1, p.y);
//		if (newP.x >= 0 && this.map[newP.y][newP.x] == 0) {
//			moves.add(newP);
//		}
//		return moves;
//	}
	public ArrayList<GameAction> getTankActions(int unitCode) {
		if (Unit.isTank(unitCode)) {
			Tank me = (Tank) getUnit(unitCode);
			ArrayList<GameAction> actions = new ArrayList<GameAction>();
			
			if (me.isAlive()) {
				for (int i = 0; i < 4; i++) {
					actions.add(new GameAction(GameAction.MOVE, GameAction.NORTH + i));
				}
				if (!this.bullets[unitCode - 2].isAlive()) {
					actions.add(new GameAction(GameAction.FIRE, GameAction.NORTH));
				}
			}
			actions.add(new GameAction(GameAction.NONE, GameAction.NORTH));
			
			return actions;
		} else {		
			return null;
		}
	}
	//TODO add moves that move into an area that currently contains a bullet but will be cleared next round.
//	public ArrayList<Integer> getTankMovesDirection(Point p) {
//		ArrayList<Integer> moves = new ArrayList<Integer>();
//		Point newP;
//		newP = new Point(p.x, p.y + tankSize + 1);
//		if (newP.y < this.map.length
//				&& this.map[newP.y][newP.x] == 0
//				&& this.map[newP.y][newP.x+1] == 0
//				&& this.map[newP.y][newP.x+2] == 0
//				&& this.map[newP.y][newP.x+3] == 0
//				&& this.map[newP.y][newP.x+4] == 0) {
//			moves.add(0);
//		}
//		newP = new Point(p.x + tankSize + 1, p.y);
//		if (newP.x < this.map[0].length
//				&& this.map[newP.y][newP.x] == 0
//				&& this.map[newP.y+1][newP.x] == 0
//				&& this.map[newP.y+2][newP.x] == 0
//				&& this.map[newP.y+3][newP.x] == 0
//				&& this.map[newP.y+4][newP.x] == 0) {
//			moves.add(1);
//		}
//		newP = new Point(p.x, p.y-1);
//		if (newP.y >= 0
//				&& this.map[newP.y][newP.x] == 0
//				&& this.map[newP.y][newP.x+1] == 0
//				&& this.map[newP.y][newP.x+2] == 0
//				&& this.map[newP.y][newP.x+3] == 0
//				&& this.map[newP.y][newP.x+4] == 0) {
//			moves.add(2);
//		}
//		newP = new Point(p.x-1, p.y);
//		if (newP.x >= 0
//				&& this.map[newP.y][newP.x] == 0
//				&& this.map[newP.y+1][newP.x] == 0
//				&& this.map[newP.y+2][newP.x] == 0
//				&& this.map[newP.y+3][newP.x] == 0
//				&& this.map[newP.y+4][newP.x] == 0) {
//			moves.add(3);
//		}
//		return moves;
//	}	
//	//private boolean moveTank(Point oldP, Point nextP, GameAction gameAction, int idx) {	
//	//	return false;
//	//}
//	public boolean canTankMove(Point oldP, Point newP) {
//		int unitValue = this.map[oldP.y][oldP.x];
//		if ((isInMapTank(newP) == true)
//				&& (Util.mDist(oldP, newP) == 1)) {
//			for (int y = newP.y; y < tankSize + newP.y; y++) {
//				for (int x = newP.x; x < tankSize + newP.x; x++) {
//					if (this.map[y][x] != 0 && this.map[y][x] != unitValue) {
//						return false;
//					}
//				}
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
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
			
			//
			// H levels:
			// * 100k: 		Base existence
			// * 60k - 20k:	Tank existence
			// * 10k - 100:	Distance to enemy base
			
			//
			// Calculate H
			//			
			if (numBase1 != numBase2) {
				h = 100000 * (numBase1 - numBase2);
				return h;
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
			
			//
			// Being close to the enemy base line is a good thing
			//			
			for (int i = 0; i < tanks.length; i++) {
				int score = 0;
				if (i < 2) {
					score = 200 - Util.mDist(tanks[i].getPosition(), getUnit(Unit.BASE2).getPosition());					
				} else {
					score = -200 + Util.mDist(tanks[i].getPosition(), getUnit(Unit.BASE1).getPosition());
				}
				h += score;
			}
			
			//
			// Firing at enemy targets is a good thing
			// 
			for (int i = 0; i < bullets.length; i++) {
				if (!bullets[i].isAlive()) {
					continue;
				}
				int score = 0;
				int walls = 0;
				int empty = 0;
				Point start = new Point(bullets[i].getPosition());
				Point end = Util.movePointDist(bullets[i].getPosition(), bullets[i].getRotation(), 1000, this);
				while(!start.equals(end)) {
					if (this.map[start.y][start.x] == Unit.EMPTY) {
						empty++;
					} else if (this.map[start.y][start.x] == Unit.WALL) {
						walls++;
					} else {
						if (Unit.isBase(this.map[start.y][start.x])) {
							if (Unit.BASE1 + i/2 == this.map[start.y][start.x] && walls == 0) {
								//
								// Firing at own base!
								//
								score = -(10000 - empty);
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
					if (start.x > end.x) {
						start.x--;
					} else if (start.x < end.x) {
						start.x++;
					} else if (start.y > end.y) {
						start.y--;
					} else {
						start.y++;
					}
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
	//TODO: Assume bullet explosions damage only walls
	//TODO: Assume that explosions happen when a bullet hits anything
	//TODO: Assume bullets that hit the edge of the map disappear
	//TODO: Assume that more than one bullet can hit the same object at the same time (e.g a WALL)
	//TODO: Assume a Tank can move into an area that had a wall or base (NOT tank) in it the previous round
	//TODO: Assume Tanks move in a 100% turn based way
	//TODO: Assume when a Tank tries to move into a bullet it is destroyed
	//TODO: Assume Bullets are moved twice at the start of the Round
	//TODO: Assume you can always have one bullet. A tank can fire a bullet if his bullet will be destroyed next round.
	//TODO: Assume you cannot fire on the edge of the map.
	//TODO: Assume Bullets will collide if they never share a space
	public void nextTick() {
		this.tickCount++;
		
		//
		// Clear all collisions, they last only one tick
		//
		collisions.clear();
 
		ArrayList<Point> deadListPoint = new ArrayList<Point>(); 

		//
		// Bullets move twice
		//
		moveBullets(deadListPoint);
		moveBullets(deadListPoint);
		
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
				t.setNextAction(new GameAction(GameAction.NONE, 0));
			}
			if (t.getNextAction().type < GameAction.MOVE || t.getNextAction().type > GameAction.NONE) {
				System.err.println("TANK[" + i + "] has invalid action set, doing NONE");
				t.setNextAction(new GameAction(GameAction.NONE, 0));
			}
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
			if (t.getNextAction().type == GameAction.MOVE) {
				final Point nextP = Util.movePoint(t.getPosition(), t.getNextAction().direction);
				final Point oldP = t.getPosition();

				t.setRotation(t.getNextAction().direction);
				final Point toAdd;
				final Point toClear;
				if (t.getNextAction().direction == GameAction.NORTH || t.getNextAction().direction == GameAction.SOUTH) {				
					if (t.getNextAction().direction == GameAction.NORTH) {
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
					}
				} else {				
					if (t.getNextAction().direction == GameAction.EAST) {
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
					}
				}
			}
		}		

		//
		// Check if any tanks have been hit by old Bullets
		//
		destroyTankBulletHits();

		//
		// Fire new Bullets
		//
		Bullet[] newBullets = new Bullet[4];
		for (int i = 0; i < this.tanks.length; i++) {
			t = this.tanks[i];
			if (!t.isAlive()) {
				continue;
			}
			if (t.getNextAction().type == GameAction.FIRE) {
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
		destroyNonTankBulletHits(deadListPoint);

		//
		// The game might be over now. Possibly return
		//
		
		//
		// Check if any tanks have been hit by old Bullets
		//
		destroyTankBulletHits();		
		
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
		if (!bases[0].isAlive() 
				|| !bases[1].isAlive()
				|| (!tanks[0].isAlive() && !tanks[1].isAlive() &&!tanks[2].isAlive() &&!tanks[3].isAlive())) {
			if ((!bases[0].isAlive() && !bases[1].isAlive())
				|| (!tanks[0].isAlive() && !tanks[1].isAlive() &&!tanks[2].isAlive() &&!tanks[3].isAlive())) {
				this.status = STATUS_DRAW;
			} else if (!bases[0].isAlive()) {
				this.status = STATUS_PLAYER2_WINS;
			} else {
				this.status = STATUS_PLAYER1_WINS;
			}
		}
	}
	private void moveBullets(ArrayList<Point> deadListPoint) {
		
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
		destroyNonTankBulletHits(deadListPoint);
	}
	private void destroyTankBulletHits() {
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
	private void destroyNonTankBulletHits(ArrayList<Point> deadListPoint) {
		Point p;
		for (int i = 0; i < deadListPoint.size(); i++) {
			p = deadListPoint.get(i);
			if (!Unit.isEmptyOrWall(this.map[p.y][p.x])) {
				Unit obstacle = getUnit(this.map[p.y][p.x]);
				obstacle.setAlive(false);
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
				
				//if (obstacleUnitCode == Unit.WALL) {
				
				//
				// Destroy this Unit and 5 perpendicular WALL Units
				//
				if (me.getRotation() == GameAction.NORTH || me.getRotation() == GameAction.SOUTH) {
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
}