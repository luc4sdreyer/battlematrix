package za.co.entelect.competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simulator to test bots. Most of the configuration options are in the
 * code below (I hope my boss never finds this code because he'll probably
 * fire me. But if Jon Skeet finds this code he will consume my soul).
 * 
 * Anyway:
 * 		dbMode:					true to write results to MySQL database.
 * 		saveGamesWhereP1Loses:	Save a movelist in \movelists when P1 loses.
 * 		saveGamesWhereP2Loses:	Save a movelist in \movelists when P2 loses.
 * 		saveGamesWhereDraws:	Save a movelist in \movelists when the game draws.
 * 		numThreads:				Number of CPU threads to use.
 * 		bot1:					The full qualified name for player 1.
 * 		bot2:					The full qualified name for player 2.
 * 		numSims:				The number of sims. Each sim consist of numTest tests.
 * 		numTests:				The number of tests. Each test is one battlematrix game.
 * 								Therefore the number of games played is numSims * numTests.
 * 		mapName:				The filename of the map. Maps are loaded from the assets folder.
 *	 							Example: "mapE1.txt" for Entelect's first map. There are 
 *								options to have a different map for each sim.
 * 		
 */
public class Simulator implements Runnable {
	public static final boolean dbMode = false;
	public static boolean saveGamesWhereP1Loses = false;
	public static boolean saveGamesWhereP2Loses = false;
	public static boolean saveGamesWhereDraws = false;
	public static final int numThreads = 5;

	public static String bot1 =
			"za.co.entelect.competition.bots.Random"
			//"za.co.entelect.competition.bots.Endgame"
			//"za.co.entelect.competition.bots.Minimax"
			//"za.co.entelect.competition.bots.MinimaxFixedDepth2"
			//"za.co.entelect.competition.bots.MinimaxFixedDepth4"
			//"za.co.entelect.competition.bots.MCTS"
			//"za.co.entelect.competition.bots.Brute"
			//"za.co.entelect.competition.bots.BruteV2"
			;

	public static String bot2 =
			//"za.co.entelect.competition.bots.Random"
			//"za.co.entelect.competition.bots.Endgame"
			//"za.co.entelect.competition.bots.Minimax"
			//"za.co.entelect.competition.bots.MinimaxFixedDepth2"
			//"za.co.entelect.competition.bots.MinimaxFixedDepth4"
			//"za.co.entelect.competition.bots.MCTS"
			//"za.co.entelect.competition.bots.Brute"
			"za.co.entelect.competition.bots.BruteV2"
			;

	public static Connection connection = null;
	private Game game;
	private ArrayList<Integer>[] moveList;
	private int timeLimit = 100;
	private Result result;
	private int simID;

	public Simulator(Result result, ArrayList<String> file, ArrayList<Integer>[] moveList, String bot1, String bot2, int simID) {
		this.game = new Game(
				bot1,
				bot2,
				file);
		//gameState.getTanks()[1].setAlive(false);
		//gameState.getTanks()[2].setAlive(false);
		//gameState.getTanks()[3].setAlive(false);

		this.game.getPlayer1().setDebugLevel(0);
		this.game.getPlayer2().setDebugLevel(0);
		this.moveList = moveList;
		this.result = result;
		this.simID = simID;

		this.game.getGameState().setMapType();
		//this.game.getGameState().setDebugMode(true);
		//this.game.getGameState().setRules(GameState.RULES_TOTAL_DESTRUCTION);
		//GameState.maxTurns = 200;
		
		if (saveGamesWhereP1Loses || saveGamesWhereP2Loses || saveGamesWhereDraws) {
			this.game.getGameState().setDebugMode(true);
		}
		
		//System.out.println("Started simID: " +simID );
	}

	@Override
	public void run() {
		boolean active = true;

		while(active) {
			int[] overrideActions = new int[4];
			Arrays.fill(overrideActions, -1);

			for (int i = 0; i < 4; i++) {
				if (moveList != null && this.game.getGameState().getTickCount() < moveList[i].size()) {
					overrideActions[i] = moveList[i].get(this.game.getGameState().getTickCount());
				}		
			}

			active = this.game.nextTick(overrideActions, null, timeLimit);
		}

		//System.out.println("Completed  ID: " +simID );
		this.game.generateResult(this.result);
		if (saveGamesWhereP1Loses || saveGamesWhereP2Loses || saveGamesWhereDraws) {
			if ((saveGamesWhereP1Loses && this.game.getGameState().getStatus() == GameState.STATUS_PLAYER2_WINS)
					|| (saveGamesWhereP2Loses && this.game.getGameState().getStatus() == GameState.STATUS_PLAYER1_WINS)
					|| (saveGamesWhereDraws && this.game.getGameState().getStatus() == GameState.STATUS_DRAW)) {
				BufferedWriter fileWriter = null;
		        File currentMoveList = new File("movelists\\" + Util.date.format(Calendar.getInstance().getTime()) + "_" + this.simID + ".txt");
		        try {
					fileWriter = new BufferedWriter(new FileWriter(currentMoveList));
			        for (int j = 0; j < this.game.getGameState().getActionLog().size(); j++) {
						int[] actions = this.game.getGameState().getActionLog().get(j);
						for (int i = 0; i < actions.length; i++) {
							fileWriter.write(GameAction.toString(actions[i]));
						}
						fileWriter.write("\n");
					}
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void main(String[] args) {		
		@SuppressWarnings("unused")
		int zTableInitializer = Util.zTable.length;
		ArrayList<Integer> times = new ArrayList<Integer>();

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
			numSims = 1;
		}


		for (int j = 0; j < numSims; j++) {
			int numTests = -1;
			if (dbMode) {
				numTests = 100;
			} else {
				numTests = 20;
			}
			long time = System.nanoTime();

			ArrayList<String> gameFile = null;
			String mapString = null;
			if (dbMode) {
				gameFile = GameState.readGameFromFile("map" + j%5 + ".txt");
				
				mapString = new String();
				for (String str : gameFile) {
					mapString += str + "\n";
				}
			} else {
				String mapName = null;
				mapName = "mapE8.txt";
				//mapName = "mapE1_" + (j%4) + ".txt";
				//mapName = "mapE" + (j%8 + 1) + ".txt";
				//mapName = "mapBattle" + (j%3) + ".txt";
				gameFile = GameState.readGameFromFile(mapName);
				
				System.out.println("Playing on map: " + mapName);
				
//				int minWidth = 80;
//				int maxWidth = 100;
//				int minHeight = 80;
//				int maxHeight = 100;
//				int blockSize = 5;
//				double percentageWall = 0.15;
//				boolean basesOnSides = true;
//				boolean mirrored = true;
//				int numTanksP1 = 1;
//				int numTanksP2 = 0;

				int minWidth = 15;
				int maxWidth = 25;
				int minHeight = 15;
				int maxHeight = 25;
				int blockSize = 5;
				double percentageWall = 0.0;
				boolean basesOnSides = true;
				boolean mirrored = false;
				int numTanksP1 = 1;
				int numTanksP2 = 1;
				
				GameState newGame = MapGenerator.generateRandom(minWidth, maxWidth, minHeight, maxHeight,
						blockSize, percentageWall, basesOnSides, mirrored, numTanksP1, numTanksP2);
				//System.out.println(newGame);
				//gameFile = newGame.toStringList();
				
			}
			ArrayList<Integer>[] moveList = GUI.loadMoveList();
			moveList = null;

			ArrayList<Result> results = new ArrayList<Result>();
			if (numThreads == 1) {
				for (int i = 0; i < numTests; i++) {
					Result nextResult = new Result();
					results.add(nextResult);
					Simulator sim = null;
					//if (i < numTests/2) {
					sim = new Simulator(nextResult, gameFile, moveList, bot1, bot2, i);
					//} else {
					//	sim = new Simulator(nextResult, gameFile, moveList, bot2, bot1);
					//}
					sim.run();
				}
			} else {
				ExecutorService executor = Executors.newFixedThreadPool(numThreads);
				for (int i = 0; i < numTests; i++) {
					Result nextResult = new Result();
					results.add(nextResult);
					Simulator sim = null;
					//if (i < numTests/2) {
						sim = new Simulator(nextResult, gameFile, moveList, bot1, bot2, i);
					//} else {
					//	sim = new Simulator(nextResult, gameFile, moveList, bot2, bot1, i);
					//}
					executor.execute(sim);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
			}

			HashMap<String, int[]> resultMap = new HashMap<String, int[]>();
			int[] wld = null;
			long numTicks = 0;
			long numGames = results.size();
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
			times.add(milliSeconds);
			System.out.println("Total time: " + milliSeconds + "ms");
			System.out.println("Speed: " + numGames * 1000 / milliSeconds + " games/s, " + numTicks * 1000 / milliSeconds + " ticks/s");
		}
		System.out.println("\nFastest sim: " + Collections.min(times));
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
