package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public class MinimaxFixedDepth4 extends Minimax {	
	public MinimaxFixedDepth4(int playerIndex) {
		super(playerIndex);
		this.ignoreTimeLimit = true;
		this.maxDepthLimit = 4;
		this.debugLevel = 1;
	}

	@Override
	public GameAction[] getActions(GameState gameState, int timeLimitMS) {		
		return super.getActions(gameState, timeLimitMS);
	}
}
