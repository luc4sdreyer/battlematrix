package za.co.entelect.competition;

import java.awt.Point;


public class Tank extends Unit{
	private GameAction nextAction;
	private Point prevPosition;
	private int ID;
	public Tank(Point position, int rotation, GameAction nextAction) {
		super(position, rotation);
		this.nextAction = nextAction;
	}
	public Tank(Point position, int rotation, GameAction nextAction, boolean alive) {
		super(position, rotation, alive);
		this.nextAction = nextAction;
	}
	public Tank clone() {
		Tank newTank = new Tank(this.position, this.rotation, this.nextAction, this.alive);
		newTank.setPrevPosition(this.prevPosition);
		return newTank;
	}
	public void setPosition(Point position) {
		this.prevPosition = new Point(this.position);
		this.position = position;
	}
	public Point getPrevPosition() {
		if (prevPosition == null) {
			return getPosition();
		}
		return prevPosition;
	}
	public void setPrevPosition(Point prevPosition) {
		this.prevPosition = prevPosition;
	}
	public GameAction getNextAction() {
		return nextAction;
	}
	public void setNextAction(GameAction nextAction) {
		this.nextAction = nextAction;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public void clearNextAction() {
		this.nextAction = null;
	}
	public boolean hasNextAction() {
		if (this.nextAction != null) {
			return true;
		} else {
			return false;	
		}
	}
}

