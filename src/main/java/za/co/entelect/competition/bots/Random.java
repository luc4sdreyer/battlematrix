package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;
import za.co.entelect.competition.Util;

public class Random extends Bot {

	public Random(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {
		return Random.getActionsStatic();
	}	

	public static int[] getActionsStatic() {
		int[] next = new int[2];
		
		int action = Util.javaRandom.nextInt(6);
		if (action >= 4) {
			action += 7;
		}
		
		int action2 = Util.javaRandom.nextInt(6);
		if (action2 >= 4) {
			action2 += 7;
		}
		
		next[0] = action;
		next[1] = action2;
		return next;
	}	

	public static int[] getActionsStaticDontFire() {
		int[] next = new int[2];
		
		int action = Util.javaRandom.nextInt(5);
		if (action >= 4) {
			action += 8;
		}
		
		int action2 = Util.javaRandom.nextInt(5);
		if (action2 >= 4) {
			action2 += 8;
		}
		
		next[0] = action;
		next[1] = action2;
		return next;
	}
}
