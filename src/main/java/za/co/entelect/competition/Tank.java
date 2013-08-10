package za.co.entelect.competition;

import java.awt.Point;


public class Tank extends Unit{
	private int nextAction;
	private Point prevPosition;
	private int ID;
	public Tank(Point position, int rotation, int nextAction) {
		super(position, rotation);
		this.nextAction = nextAction;
	}
	public Tank(Point position, int rotation, int nextAction, boolean alive) {
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
	public int getNextAction() {
		return nextAction;
	}
	public void setNextAction(int nextAction) {
		this.nextAction = nextAction;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public void clearNextAction() {
		this.nextAction = -1;
	}
	public boolean hasNextAction() {
		if (this.nextAction != -1) {
			return true;
		} else {
			return false;	
		}
	}
}

