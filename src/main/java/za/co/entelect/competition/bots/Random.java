package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;

public class Random extends Bot {

	@Override
	public GameAction[] getActions() {
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
