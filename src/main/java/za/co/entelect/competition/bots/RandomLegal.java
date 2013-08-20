package za.co.entelect.competition.bots;

import java.util.ArrayList;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;
import za.co.entelect.competition.Unit;
import za.co.entelect.competition.Util;

public class RandomLegal extends Bot {

	public RandomLegal(int playerIndex) {
		super(playerIndex);
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {
		return RandomLegal.getActionsStatic(gameState, getPlayerIndex());
	}
	

	public static int[] getActionsStatic(GameState gameState, int playerIdx) {
		int unitCode = 0;
		if (playerIdx == 0) {
			unitCode = Unit.TANK2A;
		} else {
			unitCode = Unit.TANK1A;
		}
    	ArrayList<Integer> t1Moves = gameState.getTankActions(unitCode);
    	ArrayList<Integer> t2Moves = gameState.getTankActions(unitCode + 1);
		
    	int[] next = new int[2];
		
		next[0] = t1Moves.get(Util.javaRandom.nextInt(t1Moves.size()));
		next[1] = t2Moves.get(Util.javaRandom.nextInt(t2Moves.size()));
		//next[0] = t1Moves.get(Math.abs(Util.random.nextInt()) % t1Moves.size());
		//next[1] = t2Moves.get(Math.abs(Util.random.nextInt()) % t2Moves.size());
		return next;
	}

	@Override
	public Bot clone() {
		return new RandomLegal(super.getPlayerIndex());
	}

}
