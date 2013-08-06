package za.co.entelect.competition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulator implements Runnable {
	public static final boolean dbMode = false;
	public static final int numThreads = 5;
	public static Connection connection = null;
	
	private Game game;
	private ArrayList<GameAction>[] moveList;
	private int timeLimit = 100;
	private Result result;	

	public Simulator(Result result, ArrayList<String> file, ArrayList<GameAction>[] moveList, String bot1, String bot2) {
		this.game = new Game(
				bot1,
				bot2,
				file,
				false);
		//gameState.getTanks()[1].setAlive(false);
		//gameState.getTanks()[2].setAlive(false);
		//gameState.getTanks()[3].setAlive(false);

		this.game.getPlayer1().setDebugLevel(0);
		this.game.getPlayer2().setDebugLevel(0);
		this.moveList = moveList;
		this.result = result;
		
		this.game.getGameState().setDebugMode(true);
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

		this.game.generateResult(this.result);
	}


	public static void main(String[] args) {		
		@SuppressWarnings("unused")
		int zTableInitializer = Util.zTable.length;

		int numSims;
		if (dbMode) {
			numSims = Integer.MAX_VALUE;

			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Where is your MySQL JDBC Driver?");
				e.printStackTrace();
				return;
			}

			try {
				connection = DriverManager
						.getConnection("jdbc:mysql://localhost:3306/battlematrix","root", "password");

			} catch (SQLException e) {
				System.out.println("Connection Failed! Check output console");
				e.printStackTrace();
				return;
			}

			if (connection == null) {
				System.out.println("Failed to make connection!");
			}
		} else {
			numSims = 100;
		}


		for (int j = 0; j < numSims; j++) {
			int numTests = -1;
			if (dbMode) {
				numTests = 100;
			} else {
				numTests = 10;
			}
			long time = System.nanoTime();
			ExecutorService executor = Executors.newFixedThreadPool(numThreads);

			ArrayList<String> gameFile = null;
			String mapString = null;
			if (dbMode) {
				gameFile = Game.readGameFromFile("map" + j%5 + ".txt");
				
				mapString = new String();
				for (String str : gameFile) {
					mapString += str + "\n";
				}
			} else {
				//gameFile = Game.readGameFromFile("map.txt");
				
				gameFile = MapGenerator.generateRandom(80, 100, 80, 100, 5, 0.15, true, true, 1, 0).toStringList();
			}
			ArrayList<GameAction>[] moveList = null; //ImageDrawingApplet.loadMoveList();

			String bot1 =
					"za.co.entelect.competition.bots.Endgame"
					//"za.co.entelect.competition.bots.Minimax"
					//"za.co.entelect.competition.bots.MinimaxFixedDepth2"
					//"za.co.entelect.competition.bots.MinimaxFixedDepth4"
					;

			String bot2 =
					"za.co.entelect.competition.bots.Random"
					//"za.co.entelect.competition.bots.Minimax"
					//"za.co.entelect.competition.bots.MinimaxFixedDepth2"
					//"za.co.entelect.competition.bots.MinimaxFixedDepth4"
					;

			ArrayList<Result> results = new ArrayList<Result>();
			for (int i = 0; i < numTests; i++) {
				Result nextResult = new Result();
				results.add(nextResult);
				Simulator sim = null;
				//if (i < numTests/2) {
				sim = new Simulator(nextResult, gameFile, moveList, bot1, bot2);
				//} else {
				//	sim = new Simulator(nextResult, gameFile, moveList, bot2, bot1);
				//}
				executor.execute(sim);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

			HashMap<String, int[]> resultMap = new HashMap<String, int[]>();
			int[] wld = null;
			int numTicks = 0;
			int numGames = results.size();
			for (Result result : results) {
				String name = result.score.name[0] + " vs " + result.score.name[1];
				if (resultMap.containsKey(name)) {
					wld = resultMap.get(name);
					wld[0] += result.score.wld[0];
					wld[1] += result.score.wld[1];
					wld[2] += result.score.wld[2];    
				} else {
					resultMap.put(name, result.score.wld);
				}
				numTicks += result.score.numTicks;
			}

			for (Entry<String, int[]> e : resultMap.entrySet()) {
				if (dbMode) {
					Statement statement = null;
					try {
						statement = connection.createStatement();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					String names[] = e.getKey().split(" vs ");
					String query = 	"INSERT INTO battlematrix.sim_results (name_p1, name_p2, win, draw, loss, map)" + 
									"VALUES ('"+names[0]+"', '"+names[1]+"', "+e.getValue()[0]+", "+e.getValue()[2]+", "+e.getValue()[1] + 
									", '"+ mapString +"')";
					System.out.println("query: " + query);
					try {
						statement.executeUpdate(query);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} else {
					System.out.println(e.getKey());
					System.out.println(e.getValue()[0]+" "+e.getValue()[2]+" "+e.getValue()[1]);
					System.out.println();
				}
			}
			//		System.out.println(results.get(0).score.name[0]+" vs "+results.get(0).score.name[1]);
			//		System.out.println("W/D/L:");
			//		System.out.println(wld[0]+" "+wld[2]+" "+wld[1]);

			int milliSeconds = (int) ((System.nanoTime() - time)/1000000);
			System.out.println("Total time: " + milliSeconds + "ms");
			System.out.println("Speed: " + numGames * 1000 / milliSeconds + " games/s, " + numTicks * 1000 / milliSeconds + " ticks/s");
		}
		if (dbMode) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}


class Score {
	String[] name;
	int[] wld;
	int numTicks;

	public Score() {
		this.name = new String[2];
		this.wld = new int[3];
		this.numTicks = 0;
	}
}

class Result {	
	public Score score;
	public Result() {
		this.score = new Score();
	}
	public String toString() {
		String output = score.name[0]+" vs "+score.name[1]+". W/D/L:" + score.wld[0]+" "+score.wld[2]+" "+score.wld[1];
		return output;
	}
}
