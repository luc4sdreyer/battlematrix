package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class Network extends Bot {

	public Network(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {
		if (gameActions != null) {
			return gameActions;
		} else {
			int[] next = new int[2];
			
			next[0] = GameAction.ACTION_NONE;
			next[1] = GameAction.ACTION_NONE;
			return next;
		}		
	}
	

	public static int[] getActionsStatic(GameState gameState, int timeLimitMS) {
		return Random.getActionsStatic();
	}

}
