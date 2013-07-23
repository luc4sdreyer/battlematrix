package za.co.entelect.competition.bots;

import java.util.ArrayList;

import za.co.entelect.competition.GameAction;
import za.co.entelect.competition.Unit;
import za.co.entelect.competition.GameState;
import za.co.entelect.competition.OutOfTimeException;

public class Minimax extends Bot {
	private long timer;
	protected boolean ignoreTimeLimit;
	protected int maxDepthLimit;
	protected int debugLevel;
	
	public Minimax(int playerIndex) {
		super(playerIndex);
		this.ignoreTimeLimit = false;
		this.maxDepthLimit = 4;
		this.debugLevel = 2;
	}

	@Override
	public GameAction[] getActions(GameState gameState, int timeLimitMS) {
		ArrayList<GameAction[]> actions = minimaxID(gameState, false, timeLimitMS);
		if (actions.isEmpty()) {
			actions.add(Random.getActionsStatic(gameState, timeLimitMS));
			actions.add(Random.getActionsStatic(gameState, timeLimitMS));
			System.out.println("\tMinimax using RANDOM move instead.");
		}
		GameAction[] action = actions.get(getPlayerIndex());
		if (this.debugLevel > 0) {
			System.out.println();
			for (int i = 0; i < actions.size(); i += 2) {
				System.out.println("\t"+ actions.size()+" Minimax actions: "+actions.get(i+0)[0]+" "+actions.get(i+0)[1]+ " "+actions.get(i+1)[0]+" "+actions.get(i+1)[1]);
			}
		}
		
		return action;
	}

	/**
	 * Minimax search algorithm
	 *  - with Iterative deepening
	 *  - without AB pruning
	 */
	public ArrayList<GameAction[]> minimaxID(GameState gameState, boolean doAlphaBetaPruning, 
			int timeLimitMS) {
		this.timer = System.nanoTime();
		long timeLimit = timeLimitMS;
		timeLimit *= 1000000;

		if (ignoreTimeLimit == true) {
			timeLimit = Long.MAX_VALUE;
		}

		ArrayList<GameAction[]> bestPath = new ArrayList<GameAction[]>();

		//int negInf = -1000000000;
		//int posInf = 1000000000;

		int alpha = Integer.MIN_VALUE;
		int deepestPly = 0;
		for (int depthLimit = 2; depthLimit <= maxDepthLimit; depthLimit += 2) {
			GameState newState = gameState.clone();     
			ArrayList<GameAction[]> tempBestPath = new ArrayList<GameAction[]>();
			int tempAlpha = 0;

//			if (doAlphaBetaPruning == true) {                
//				int alpha = negInf;
//				int beta = posInf;
//				int playerNumber = newState.getNextToMove();
//				int maximizing = newState.isPlayerMax(playerNumber);
//						tempAlpha = this.Minimax(tempBestPath, timeLimit, depthLimit, 0, newState, alpha, beta);
//			} else {
//			}
			try {
				tempAlpha = this.MinimaxWithoutAB(tempBestPath, timeLimit, depthLimit, 0, newState, null);

				//
				// There is a bug where Action that start with NONE are suggested, leading to the tank standing still 
				// for no reason. This might help, or a heuristic that encourages moving.
				//				
//				if (tempAlpha > alpha) {
					bestPath = tempBestPath;
//				}
				alpha = Math.max(alpha, tempAlpha);
				deepestPly = depthLimit;
				
				//
				// If the game is over the search must return immediately
				//
				if (Math.abs(alpha) >= 100000) {
					break;
				}
			} catch (OutOfTimeException e) {
				break;
			}
		}
		if (this.debugLevel > 0) {
			System.out.print("best Alpha: "+alpha+"\t deepestPly: "+deepestPly);
		}
		//console.log("best Alpha: "+alpha);
		return bestPath;
	};

	public int MinimaxWithoutAB(ArrayList<GameAction[]> bestPath, long timeLimit,
			int depthLimit, int currentDepth, GameState currentState, GameAction[] parentMove) throws OutOfTimeException {
		long diff = System.nanoTime() - this.timer;
		if (diff > timeLimit) {
			throw new OutOfTimeException();
		}
		
		if (depthLimit % 2 != 0) {
			System.err.println("depthLimit must be a multiple of two!");
		}

		if (!currentState.isActive() ||	currentDepth >= depthLimit) {
			return currentState.getHeuristicValue(GameState.H_MINIMAX);
		}

		boolean maximizing = true;
		if (currentDepth % 2 == 1) {
			maximizing = false;
		}
		
		int alpha = 0;
		if (maximizing) {
			alpha = GameState.NEG_INF;
		} else {
			alpha = GameState.POS_INF;
		}

		ArrayList<GameAction> movesTA = null;
		ArrayList<GameAction> movesTB = null;	
		if (maximizing) {
			movesTA = currentState.getTankActions(Unit.TANK1A);
			movesTB = currentState.getTankActions(Unit.TANK1B);
		} else {
			movesTA = currentState.getTankActions(Unit.TANK2A);
			movesTB = currentState.getTankActions(Unit.TANK2B);
		}
		
		GameAction[] bestMove = null;
		ArrayList<GameAction[]> bestNewPath = null; 
		for (int tA = 0; tA < movesTA.size(); tA++) {
			for (int tB = 0; tB < movesTB.size(); tB++) {
				GameState newState = null;

				GameAction[] thisMove = new GameAction[2];
				thisMove[0] = movesTA.get(tA);
				thisMove[1] = movesTB.get(tB);
				thisMove[0].level = currentDepth;
				thisMove[1].level = currentDepth;
				
				if (maximizing) {
					newState = currentState;
				} else {
					newState = currentState.clone();
					newState.getTanks()[0].setNextAction(parentMove[0]);
					newState.getTanks()[1].setNextAction(parentMove[1]);
					newState.getTanks()[2].setNextAction(thisMove[0]);
					newState.getTanks()[3].setNextAction(thisMove[1]);					
					newState.nextTick();	
				}
	//			if ((currentState.equals(newState)) != true) {
	//				console.log("currentState !== equals(newState)");
	//			}
				
	//			var moveSuccess = newState.move(moves[i]);
	//			if (moveSuccess !== true) {
	//				console.log("moveSuccess !== true");
	//				console.log("moves[i]: "+moves[i]);
	//				console.log("moves: "+moves);
	//			}
				ArrayList<GameAction[]> newPath = new ArrayList<GameAction[]>();
	
				int result = this.MinimaxWithoutAB(newPath, timeLimit, depthLimit, currentDepth+1, newState, thisMove);
				
				if (thisMove[0].type == GameAction.FIRE && result == -1000000) {
					//System.out.println();
				}
				
				if (bestMove == null)  {
					bestMove = thisMove;
					bestNewPath = newPath;
				}
				if (maximizing == true) {
					if (result > alpha) {
						//System.out.println("Max: New best:"+result);
						bestMove = thisMove;
						bestNewPath = newPath;
					}
					alpha = Math.max(alpha, result);
				} else {
					if (result < alpha) {
						//System.out.println("Max: New best:"+result);
						bestMove = thisMove;
						bestNewPath = newPath;
					}
					alpha = Math.min(alpha, result);
				}
			}
		}
		if (currentDepth == 0) {
			System.nanoTime();
		}
		//bestPath = bestPath, bestMove, bestNewPath
		bestPath.add(bestMove);
		for (GameAction[] gameAction : bestNewPath) {
			bestPath.add(gameAction);
		}
		//var bestMoveArray = [bestMove];
		//bestMoveArray = bestMoveArray.concat(bestNewPath);
		//bestPath.push.apply(bestPath, bestMoveArray);       //The only one-liner way to concat arrays without creating a new one!
		return alpha;
	};

}
