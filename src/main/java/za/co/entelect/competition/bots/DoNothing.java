package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class DoNothing extends Bot {

	public DoNothing(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {
		return DoNothing.getActionsStatic();
	}
	

	public static int[] getActionsStatic() {
		int[] next = new int[2];
		
		next[0] = GameAction.ACTION_NONE;
		next[1] = GameAction.ACTION_NONE;
		return next;
	}

}
