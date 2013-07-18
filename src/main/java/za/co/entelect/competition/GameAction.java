package za.co.entelect.competition;


public class GameAction {
	final static int NORTH = 0;
	final static int EAST = 1;
	final static int SOUTH = 2;
	final static int WEST = 3;
	final static int GUI_NORTH = 2;
	final static int GUI_SOUTH = 0;
	
	public final static int MOVE = 10;
	public final static int FIRE = 11;
	public final static int NONE = 12;
	
	public final int type;
	public final int direction;
	
	public int level;
	
	public GameAction(int type, int direction) {
		super();
		this.type = type;
		this.direction = direction;
		this.level = -1;
	}
	
	public GameAction clone() {
		return new GameAction(this.type, this.direction);
	}
	
	public String toString() {
		String desc = "";

//		switch (this.type) {
//			case GameAction.MOVE: 	desc += "MOVE";		break;
//			case GameAction.FIRE: 	desc += "FIRE";		break;
//			case GameAction.NONE: 	desc += "NONE";		break;
//			default:	desc += "UNKNOWN(" + this.type + ")";
//		}
//		desc += " ";
//		switch (this.direction) {
//			case GameAction.GUI_SOUTH: 	desc += "SOUTH v";		break;
//			case GameAction.EAST: 	desc += "EAST  >";		break;
//			case GameAction.GUI_NORTH: 	desc += "NORTH ^";		break;
//			case GameAction.WEST: 	desc += "WEST  <";		break;
//			default:	desc += "UNKNOWN(" + this.direction + ")";
//		}
		switch (this.type) {
		case GameAction.MOVE:
			switch (this.direction) {
				case GameAction.GUI_SOUTH: 	desc += "v";		break;
				case GameAction.EAST: 		desc += ">";		break;
				case GameAction.GUI_NORTH: 	desc += "^";		break;
				case GameAction.WEST: 		desc += "<";		break;
				default:	desc += "UNKNOWN(" + this.direction + ")";
			}
			break;
		case GameAction.FIRE: 	desc += "F";		break;
		case GameAction.NONE: 	desc += ".";		break;
		default:	desc += "UNKNOWN(" + this.type + ")";
		}
		desc += " ";
		
		return desc;
	}
}