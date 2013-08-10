package za.co.entelect.competition;


public class GameAction {
	public final static int ACTION_MOVE_NORTH = 0;
	public final static int ACTION_MOVE_EAST = 1;
	public final static int ACTION_MOVE_SOUTH = 2;
	public final static int ACTION_MOVE_WEST = 3;
	public final static int ACTION_MOVE_GUI_NORTH = 2;
	public final static int ACTION_MOVE_GUI_SOUTH = 0;
	
	//public final static int MOVE = 10;
	public final static int ACTION_FIRE = 11;
	public final static int ACTION_NONE = 12;
	
	//public final int type;
	//public final int direction;
	
	//public int level;
	
//	public GameAction(int type, int direction) {
//		super();
//		this.type = type;
//		this.direction = direction;
//		//this.level = -1;
//	}
//	
//	public GameAction(GameAction gameAction) {
//		super();
//		this.type = gameAction.type;
//		this.direction = gameAction.direction;
//		//this.level = -1;
//	}
//
//	public GameAction clone() {
//		return new GameAction(this.type, this.direction);
//	}
	
	public static String toString(int action) {
		String desc = "";

		switch (action) {
			case GameAction.ACTION_MOVE_GUI_SOUTH: 	desc += "v";		break;
			case GameAction.ACTION_MOVE_EAST: 		desc += ">";		break;
			case GameAction.ACTION_MOVE_GUI_NORTH: 	desc += "^";		break;
			case GameAction.ACTION_MOVE_WEST: 		desc += "<";		break;
			case GameAction.ACTION_FIRE: 	desc += "F";		break;
			case GameAction.ACTION_NONE: 	desc += ".";		break;
			default:	desc += "UNKNOWN(" + action + ")";
		}
		desc += " ";
		
		return desc;
	}
}