package za.co.entelect.competition;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;



class GameState {
	private boolean[][] map;
	private ArrayList<Bullet> bullets;
	private ArrayList<Collision> collisions;
	private Tank[] tanks;
	private Base[] bases;
	private int tickCount = 0;
	public GameState(boolean[][] map, ArrayList<Bullet> bullets, Tank[] tanks, Base[] bases, ArrayList<Collision> collisions, int tickCount) {
		super();
		this.map = map;
		this.bullets = bullets;
		this.collisions = collisions;
		this.tanks = tanks;
		this.bases = bases;
		this.tickCount = tickCount;
	}
	public boolean[][] getMap() {
		return map;
	}
	public void setMap(boolean[][] map) {
		this.map = map;
	}
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	public void setBullets(ArrayList<Bullet> bullets) {
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
	public GameState clone() {
		boolean[][] newMap = new boolean[this.map.length][this.map[0].length];
		for (int y = 0; y < newMap.length; y++) {
			newMap[y] = Arrays.copyOf(this.map[y], this.map[0].length);
		}
		
		ArrayList<Bullet> newBullets = new ArrayList<Bullet>();
		for (Bullet bullet : this.bullets) {
			newBullets.add(bullet.clone());
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
		
		return new GameState(newMap, newBullets, newTanks, newBases, newCollisions, this.tickCount);
	}
	public ArrayList<Point> getMoves(Point p) {
		ArrayList<Point> moves = new ArrayList<Point>();
		Point newP;
		newP = new Point(p.x, p.y+1);
		if (newP.y < this.map.length && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		newP = new Point(p.x+1, p.y);
		if (newP.x < this.map[0].length && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		newP = new Point(p.x, p.y-1);
		if (newP.y >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		newP = new Point(p.x-1, p.y);
		if (newP.x >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		return moves;
	}
	public ArrayList<Integer> getMovesDirection(Point p) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		Point newP;
		newP = new Point(p.x, p.y+1);
		if (newP.y < this.map.length && this.map[newP.y][newP.x] == false) {
			moves.add(0);
		}
		newP = new Point(p.x+1, p.y);
		if (newP.x < this.map[0].length && this.map[newP.y][newP.x] == false) {
			moves.add(1);
		}
		newP = new Point(p.x, p.y-1);
		if (newP.y >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(2);
		}
		newP = new Point(p.x-1, p.y);
		if (newP.x >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(3);
		}
		return moves;
	}
	public boolean canMove(Point oldP, Point newP) {
		if (isInMap(newP)
				&& this.map[newP.y][newP.x] == false
				&& (isInMap(newP) == true)
				&& (Util.mDist(oldP, newP) == 1)) {
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
	public void nextTick() {
		this.tickCount++;
		
		// Clear all collisions, they last only one tick
		collisions.clear();
		
		// Update Tank positions
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
			if (t.getNextAction().type != GameAction.NONE) {
				if (t.getNextAction().type == GameAction.MOVE) {
					Point nextP = Util.movePoint(t.getPosition(), t.getNextAction().direction);
					if (canMove(t.getPosition(), nextP)) {
						t.setPosition(nextP);
						t.setRotation(t.getNextAction().direction);
					}
				} else if (t.getNextAction().type == GameAction.FIRE) {
					bullets.add(new Bullet(new Point(t.getPosition()), t.getNextAction().direction));
					t.setRotation(t.getNextAction().direction);
				}
				t.clearNextAction();
			}
		}
		
		// Update Bullet positions
		Bullet b;
		for (int i = 0; i < bullets.size(); i++) {
			b = bullets.get(i);
			Point nextP = Util.movePoint(b.getPosition(), b.getRotation());
			if (isInMap(nextP)) {
				b.setPosition(nextP);
			} else {
				// Bullet is outside map and will cause collision
				bullets.remove(i--);
				collisions.add(new Collision(new Point(b.getPosition()), 0));
			}
		}
		
		ArrayList<Unit> deadList = new ArrayList<Unit>(); 
		
		// Now all positions are updated, create list of all units and check for collisions
		HashMap<Point, Unit> hitmap = new HashMap<Point, Unit>();
		Unit me;
		for (int i = 0; i < bases.length; i++) {
			me = bases[i];
			if (!me.isAlive()) {
				continue;
			}
			if (hitmap.containsKey(me.position)) {
				Unit obstacle = hitmap.get(me.position);
				deadList.add(me);
				deadList.add(obstacle);
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else {
				hitmap.put(me.position, me);
			}
		}
		for (int i = 0; i < tanks.length; i++) {
			me = tanks[i];
			if (!me.isAlive()) {
				continue;
			}
			if (hitmap.containsKey(me.position)) {
				Unit obstacle = hitmap.get(me.position);
				deadList.add(me);
				deadList.add(obstacle);
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else {
				hitmap.put(me.position, me);
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			me = bullets.get(i);
			if (!me.isAlive()) {
				continue;
			}
			if (hitmap.containsKey(me.position)) {
				Unit obstacle = hitmap.get(me.position);
				deadList.add(me);
				deadList.add(obstacle);
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else if (this.map[me.getPosition().y][me.getPosition().x] == true) {	
				deadList.add(me);
				this.map[me.getPosition().y][me.getPosition().x] = false;
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else {
				hitmap.put(me.position, me);
			}
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			me = bullets.get(i);
			if (!me.isAlive()) {
				bullets.remove(i--);
			}
		}
		
		for (Unit unit : deadList) {
			unit.setAlive(false);
		}
		
		// End-game conditions
		for (int i = 0; i < bases.length; i++) {
			me = bases[i];
			if(me.isAlive() == false) {
				System.out.println("GAME OVER, player " + ((i+1)%bases.length) + " wins!");				
			}
		}
	}
	
	public String toString() {
		return Integer.toString(tickCount);
	}
	
	public void getCommanderActions(ArrayList<GameAction>[] moveList) {

		Random rand = new Random();
		

//		MOVE = 10;
//		FIRE = 11;
//		NONE = 12;
		
		for (int i = 0; i < 4; i++) {
			int action = rand.nextInt(3)+10;
			int direction = rand.nextInt(4);
			if (action == GameAction.MOVE) {
				ArrayList<Integer> moves = getMovesDirection(getTanks()[i].getPosition());
				direction = moves.get(rand.nextInt(moves.size()));
			}
			
			GameAction next = new GameAction(action, direction);
			if (moveList != null && moveList[i] != null && moveList[i].size() > 0) {
				next = moveList[i].remove(0);
			}
			if (i == -1) {
				//next = p1reqAction;
				//p1reqAction = new GameAction(GameAction.NONE, GameAction.SOUTH);
				//System.out.println("next GameAction: "+next);
			}
			
			getTanks()[i].setNextAction(next);
			
		}
		
	}
}