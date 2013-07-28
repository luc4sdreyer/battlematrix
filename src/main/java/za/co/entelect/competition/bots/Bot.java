package za.co.entelect.competition.bots;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.GameState;

public abstract class Bot {
	private int playerIndex;
	private String name;
	protected GameAction[] gameActions;
	protected int debugLevel;
	
	public Bot(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	public String getName() {
		if (name == null) {
			return this.getClass().getSimpleName();
		} else {
			return name;
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public GameAction[] getGameActions() {
		return gameActions;
	}
	public void setGameActions(GameAction[] gameActions) {
		this.gameActions = gameActions;
	}
	public int getDebugLevel() {
		return debugLevel;
	}
	public void setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
	}
	public abstract GameAction[] getActions(GameState gameState, int timeLimitMS);
}
