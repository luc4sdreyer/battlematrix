package za.co.entelect.competition;

import java.awt.Point;


public abstract class Unit {
	protected Point position;
	protected int rotation;
	protected boolean alive;
	
	public final static int EMPTY            = 0;
	public final static int WALL             = 1;
	public final static int TANK1A           = 2;
	public final static int TANK1B           = 3;
	public final static int TANK2A           = 4;
	public final static int TANK2B           = 5;
	public final static int BULLET_TANK1A    = 6;
	public final static int BULLET_TANK1B    = 7;
	public final static int BULLET_TANK2A    = 8;
	public final static int BULLET_TANK2B    = 9;
	public final static int BASE1            = 10;
	public final static int BASE2            = 11;
	
	public Unit(Point position, int rotation) {
		super();
		this.position = position;
		this.rotation = rotation;
		this.alive = true;
	}
	public Unit(Point position, int rotation, boolean alive) {
		super();
		this.position = position;
		this.rotation = rotation;
		this.alive = alive;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public abstract Unit clone();
	public static boolean isTankOrEmpty(int unitCode) {
		if (unitCode == Unit.EMPTY || (unitCode >= Unit.TANK1A && unitCode <= Unit.TANK2B)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isBulletOrEmpty(int unitCode) {
		if (unitCode == Unit.EMPTY || (unitCode >= Unit.BULLET_TANK1A && unitCode <= Unit.BULLET_TANK2B)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isBase(int unitCode) {
		if (unitCode == Unit.BASE1 || unitCode == Unit.BASE2) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isBullet(int unitCode) {
		if (unitCode >= Unit.BULLET_TANK1A && unitCode <= Unit.BULLET_TANK2B) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isTank(int unitCode) {
		if (unitCode >= Unit.TANK1A && unitCode <= Unit.TANK2B) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isEmptyOrWall(int unitCode) {
		if (unitCode == Unit.EMPTY || unitCode == Unit.WALL) {
			return true;
		} else {
			return false;
		}
	}
}

