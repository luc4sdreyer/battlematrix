package za.co.entelect.competition;

/**
 * Each of the game's actions represented by in integer.
 */
public class GameAction {
	public final static int ACTION_MOVE_NORTH = 0;
	public final static int ACTION_MOVE_EAST = 1;
	public final static int ACTION_MOVE_SOUTH = 2;
	public final static int ACTION_MOVE_WEST = 3;
	public final static int ACTION_MOVE_GUI_NORTH = 2;
	public final static int ACTION_MOVE_GUI_SOUTH = 0;
	
	public final static int ACTION_FIRE = 11;
	public final static int ACTION_NONE = 12;
		
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