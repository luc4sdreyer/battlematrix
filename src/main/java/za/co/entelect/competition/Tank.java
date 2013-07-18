package za.co.entelect.competition;

import java.awt.Point;


public class Tank extends Unit{
	private GameAction nextAction;
	public Tank(Point position, int rotation, GameAction nextAction) {
		super(position, rotation);
		this.nextAction = nextAction;
	}
	public Tank(Point position, int rotation, GameAction nextAction, boolean alive) {
		super(position, rotation, alive);
		this.nextAction = nextAction;
	}
	public Tank clone() {
		return new Tank(this.position, this.rotation, this.nextAction, this.alive);
	}
	public GameAction getNextAction() {
		return nextAction;
	}
	public void setNextAction(GameAction nextAction) {
		this.nextAction = nextAction;
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

