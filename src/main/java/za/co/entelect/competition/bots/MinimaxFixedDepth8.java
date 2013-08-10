package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class MinimaxFixedDepth8 extends Minimax {	
	public MinimaxFixedDepth8(int playerIndex) {
		super(playerIndex);
		this.ignoreTimeLimit = true;
		this.maxDepthLimit = 8;
		this.debugLevel = 1;
	}

	@Override
	public int[] getActions(GameState gameState, int timeLimitMS) {		
		return super.getActions(gameState, timeLimitMS);
	}
}
