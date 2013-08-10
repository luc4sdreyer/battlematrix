package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class MinimaxFixedDepth2 extends Minimax {	
	public MinimaxFixedDepth2(int playerIndex) {
		super(playerIndex);
		this.ignoreTimeLimit = true;
		this.maxDepthLimit = 2;
		this.debugLevel = 0;
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {		
		return super.getActions(gameState, timeLimitMS);
	}
}
