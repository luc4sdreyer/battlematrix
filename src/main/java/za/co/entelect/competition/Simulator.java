package za.co.entelect.competition;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Score {
	String[] name;
	int[] wld;
	
	public Score() {
		this.name = new String[2];
		this.wld = new int[3];
	}
}

class Result {	
	public Score score;
	public Result() {
		this.score = new Score();
	}
}

public class Simulator implements Runnable {
	private Game game;
	private ArrayList<GameAction>[] moveList;
	private int timeLimit = 100;
	private Result result;

	public Simulator(Result result) {
		this.game = new Game(
				"za.co.entelect.competition.bots.Random",
				//"za.co.entelect.competition.bots.Minimax",
				"za.co.entelect.competition.bots.MinimaxFixedDepth",
				"map.txt");
		//gameState.getTanks()[1].setAlive(false);
		//gameState.getTanks()[2].setAlive(false);
		//gameState.getTanks()[3].setAlive(false);

		this.moveList = ImageDrawingApplet.loadMoveList();
		this.result = result;
	}
	
	@Override
	public void run() {
		boolean active = true;
		
		while(active) {
			GameAction[] overrideActions = new GameAction[4];

			for (int i = 0; i < 4; i++) {
				if (moveList != null && this.game.getGameState().getTickCount() < moveList[i].size()) {
					overrideActions[i] = moveList[i].get(this.game.getGameState().getTickCount());
				}		
			}
			
			active = this.game.nextTick(overrideActions, null, timeLimit);
		}
		
		this.generateResult();
	}
	
	private void generateResult() {
		result.score.name[0] = this.game.getPlayer1().getClass().getSimpleName();
		result.score.name[1] = this.game.getPlayer2().getClass().getSimpleName();
		
		switch (this.game.getGameState().getStatus()) {
			case GameState.STATUS_PLAYER1_WINS:		result.score.wld[0]++;	break;
			case GameState.STATUS_PLAYER2_WINS:		result.score.wld[1]++;	break;
			case GameState.STATUS_DRAW:				result.score.wld[2]++;	break;
			default:								result.score.wld = null;
		}
	}

	public static void main(String[] args) {
		int numTests = 15;
		
		long time = System.nanoTime();
		
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		numTests = 50;
        
		ArrayList<Result> results = new ArrayList<Result>();
		for (int i = 0; i < numTests; i++) {
			Result nextResult = new Result();
			results.add(nextResult);
			Simulator sim = new Simulator(nextResult);
            executor.execute(sim);
		}
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        
        int[] wld = new int[3];
        for (Result result : results) {
        	wld[0] += result.score.wld[0];
        	wld[1] += result.score.wld[1];
        	wld[2] += result.score.wld[2];        	
		}
        System.out.println(results.get(0).score.name[0]+" vs "+results.get(0).score.name[1]);
        System.out.println(wld[0]+" "+wld[1]+" "+wld[2]);
        
		System.out.println("Total time: "+(System.nanoTime() - time)/1000000 + "ms");
	}
}
