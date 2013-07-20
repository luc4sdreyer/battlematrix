package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class Network extends Bot {

	public Network(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public GameAction[] getActions(GameState gameState, int timeLimitMS) {
		if (gameActions != null) {
			return gameActions;
		} else {
			GameAction[] next = new GameAction[2];
			
			int action = GameAction.NONE;
			int direction = GameAction.NORTH;
			
			int action2 = GameAction.NONE;
			int direction2 = GameAction.NORTH;
			
			next[0] = new GameAction(action, direction);
			next[1] = new GameAction(action2, direction2);
			return next;
		}		
	}
	

	public static GameAction[] getActionsStatic(GameState gameState, int timeLimitMS) {
		GameAction[] next = new GameAction[2];
		
		int action = GameAction.NONE;
		int direction = GameAction.NORTH;
		
		int action2 = GameAction.NONE;
		int direction2 = GameAction.NORTH;
		
		next[0] = new GameAction(action, direction);
		next[1] = new GameAction(action2, direction2);
		return next;
	}

}
