package za.co.entelect.competition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import za.co.entelect.competition.bots.Bot;

/**
 * The Game class runs a game using two players (two Bot objects) and a GameState.
 * The interesting functions are the three different constructors, nextTick and generateResult.
 */
public class Game {
	private Bot player1;
	private Bot player2;
	private GameState gameState;
	protected za.co.entelect.challenge.Game[] eGame;
	private Result result;
	
	/**
	 * Create a new Game.
	 * 
	 * @param player1			Player 1's Bot object.
	 * @param player2			Player 2's Bot object.
	 * @param gameState			A GameState object to base the game on. 
	 */
	public Game(Bot player1, Bot player2, GameState gameState) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.gameState = gameState;
	}
	
	/**
	 * Create a new Game.
	 * 
	 * @param player1			The fully qualified name of the Bot. Example: "za.co.entelect.competition.bots.BruteV2"
	 * 							or "za.co.entelect.competition.bots.Random".
	 * @param player2			Same as player1.
	 * @param gameFile			The filename of the map. Maps are loaded from the assets folder.
	 * 							Example: "mapE1.txt" for Entelect's first map.
	 */
	public Game(String player1, String player2, String gameFile) {
		super();
		init(player1, player2);
		this.gameState = GameState.newGame(gameFile);
	}
	
	/**
	 * Create a new Game.
	 * 
	 * @param player1			The fully qualified name of the Bot. Example: "za.co.entelect.competition.bots.BruteV2"
	 * 							or "za.co.entelect.competition.bots.Random".
	 * @param player2			Same as player1.
	 * @param file				A list of String objects, each a line from the map file. 
	 * 							Example: "mapE1.txt" for Entelect's first map.
	 */
	public Game(String player1, String player2, ArrayList<String> file) {
		super();
		init(player1, player2);
		this.gameState = GameState.newGame(file);
	}
	
	private void init(String player1, String player2) {
		try {
			Constructor<?> player1Constructor = Class.forName(player1).getConstructor(Integer.TYPE);
			this.player1 = (Bot) player1Constructor.newInstance(0);

			Constructor<?> player2Constructor = Class.forName(player2).getConstructor(Integer.TYPE);
			this.player2 = (Bot) player2Constructor.newInstance(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		this.result = new Result();
	}
	
	public Bot getPlayer1() {
		return player1;
	}
	public void setPlayer1(Bot player1) {
		this.player1 = player1;
	}
	public Bot getPlayer2() {
		return player2;
	}
	public void setPlayer2(Bot player2) {
		this.player2 = player2;
	}
	public GameState getGameState() {
		return gameState;
	}
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	public int getTickCount() {
		return this.gameState.getTickCount();
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	
	/**
	 * Advance the Game by one tick:
	 * 	1. Tell player1 to generate moves.
	 * 	2. Tell player2 to generate moves.
	 * 	3. Possibly overwrite the moves.
	 *	4. Advance the GameState object by calling gameState.nextTick().
	 * 	
	 * @param overrideActions	An int[4] containing actions (see GameAction) to overwrite the 
	 * 							actions issues by the players. Set to null if not used, or set individual 
	 * 							parts to -1 to not overwrite the corresponding bot's actions.
	 * 							The order is: [P1 bot1, P1 bot2, P2 bot1, P2 bot2].
	 * @param performedActions	An int[4] containing the actions (see GameAction) that were performed.
	 * 							The order is: [P1 bot1, P1 bot2, P2 bot1, P2 bot2].
	 * @param timeLimitMS		The number of milliseconds to give each player to generate their moves.
	 * @return 					false if the game has ended.
	 */
	public boolean nextTick(int[] overrideActions, int[] performedActions, int timeLimitMS) {
		int[] p1Moves = player1.getActions(this.gameState, timeLimitMS);
		
		int[] p2Moves = player2.getActions(this.gameState, timeLimitMS);
		
		int[] actions = new int[4];
		
		actions[0] = p1Moves[0];
		actions[1] = p1Moves[1];
		actions[2] = p2Moves[0];
		actions[3] = p2Moves[1];
		
		for (int i = 0; i < 4; i++) {
			if (actions[i] == -1) {
				actions[i] = GameAction.ACTION_NONE;
			}
			if (overrideActions != null && overrideActions[i] != -1) {
				this.gameState.getTanks()[i].setNextAction(overrideActions[i]);
			} else {
				this.gameState.getTanks()[i].setNextAction(actions[i]);
			}			
		}
		if (performedActions != null) {
			for (int i = 0; i < 4; i++) {
				performedActions[i] = this.gameState.getTanks()[i].getNextAction();
			}
		}
		
		this.gameState.nextTick();
		
		boolean isActive = this.gameState.isActive();
		if (!isActive) {
			generateResult(this.result);
		}
		return isActive;
	}
	
	/**
	 * Store the game's result in the Result object.
	 */
	public void generateResult(Result result) {		
		result.score.name[0] = this.getPlayer1().getName();
		result.score.name[1] = this.getPlayer2().getName();
		
		switch (this.getGameState().getStatus()) {
			case GameState.STATUS_PLAYER1_WINS:		result.score.wld[0]++;	break;
			case GameState.STATUS_PLAYER2_WINS:		result.score.wld[1]++;	break;
			case GameState.STATUS_DRAW:				result.score.wld[2]++;	break;
			default:								result.score.wld = null;
		}
		
		result.score.numTicks = this.getGameState().getTickCount();
	}
}
