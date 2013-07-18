package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public abstract class Bot {
	private int playerIndex;
	public Bot(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	public abstract GameAction[] getActions(GameState gameState, int timeLimitMS);
}
