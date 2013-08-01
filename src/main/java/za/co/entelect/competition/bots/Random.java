package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class Random extends Bot {

	public Random(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public GameAction[] getActions(GameState gameState, int timeLimitMS) {
		return Random.getActionsStatic();
	}
	

	public static GameAction[] getActionsStatic() {
		GameAction[] next = new GameAction[2];
		
		int action = ((int)(Math.random()*3))+10;
		int direction = ((int)(Math.random()*4));
		
		int action2 = ((int)(Math.random()*3))+10;
		int direction2 = ((int)(Math.random()*4));
		
		next[0] = new GameAction(action, direction);
		next[1] = new GameAction(action2, direction2);
		return next;
	}

}
