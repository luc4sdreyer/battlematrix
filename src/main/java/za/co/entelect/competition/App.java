package za.co.entelect.competition;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import org.apache.axis.AxisFault;

import za.co.entelect.challenge.*;
import za.co.entelect.competition.bots.Bot;
import za.co.entelect.competition.bots.BruteV2;
import za.co.entelect.competition.bots.Random;

public class App 
{
	/**
	 * This is the main application. It implements the competition rules as specified by Entelect.
	 * A copy of the rules are available at doc\rules.txt
	 * 
	 * The configuration options in configApp.properties:
	 * 	1. playStyle can be one of the following:
	 * 		- BruteV2: aggressive, dependable and not very smart. I submitted this bot in the end.
	 * 		- MinimaxFixedDepth2: Uses a minimax search depth of 2 or 8, depending on how you look at it.
	 * 							  Each bot makes two decisions, resulting in 8 plies.
	 * 		- Random: completely random actions.
	 * 		- Fire: Will fire forever
	 * 		- North: Will move north forever
	 * 2. myName: A string that the program uses to recognise itself.
	 * 3. extraOutput: Boolean indicating whether A LOT of extra debugging output should be shown.  
	 * 
	 * @param args The endpoint (game hosting server) to connect to, as per the rules.
	 */
	public static void main( String[] args )
	{
		Properties prop = new Properties();		
		String propertiesFile = "configApp.properties";
		
		String playStyle = new String();
		String myName = new String();
		boolean extraOutput = false;
		
		
		try {
			prop.load(new FileInputStream(propertiesFile));
			playStyle = prop.getProperty("playStyle");
			if (prop.getProperty("myName") != null) {
				myName = prop.getProperty("myName");
			}
			extraOutput = Boolean.parseBoolean(prop.getProperty("extraOutput"));
			
			System.out.println("playStyle=" + playStyle);
		} catch (IOException ex) {
			System.err.println("Could not read " + propertiesFile);
		}

		String endPoint = new String();
		if (args.length == 1) {
			endPoint = args[0];
		} else if (args.length == 2) {
			endPoint = args[0];
			if (myName.equals("")) {
				myName = args[1];
			}
		}
		System.out.println("Connecting to: " + endPoint);
		System.out.println("myName: " + myName);

		Bot stupidBot = null;
		try {
			if (playStyle.equals("MinimaxFixedDepth2")) {
				Constructor<?> player1Constructor = Class.forName("za.co.entelect.competition.bots.MinimaxFixedDepth2").getConstructor(Integer.TYPE);				
				stupidBot = (Bot) player1Constructor.newInstance(0);
			} else if (playStyle.equals("BruteV2")) {
				Constructor<?> player1Constructor = Class.forName("za.co.entelect.competition.bots.BruteV2").getConstructor(Integer.TYPE);				
				stupidBot = (Bot) player1Constructor.newInstance(0);
				BruteV2.setPrintBulletGrid(extraOutput);
				BruteV2.setPrintExtraOutput(extraOutput);
				BruteV2.setPrintGoalArea(extraOutput);
			} else if (playStyle.equals("Random")) {
				Constructor<?> player1Constructor = Class.forName("za.co.entelect.competition.bots.Random").getConstructor(Integer.TYPE);				
				stupidBot = (Bot) player1Constructor.newInstance(0);
			}
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
		
		if (stupidBot == null) {
			stupidBot = new Random(0);
		}

		Challenge service = null;
		try {
			java.net.URL url = new java.net.URL(endPoint);
			service = new ChallengePortBindingStub(url, null);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		za.co.entelect.challenge.Board board = null;
		State[][] eStateGrid = null;
		HashMap<Integer, Integer> eBullets = new HashMap<Integer, Integer>();
		GameState xGameState = null;
		GameState prevXGameState = null;
		
		try {
			board = service.login();
		} catch (NoBlameException e) {
			e.printStackTrace();
		} catch (EndOfGameException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		StateArray[] stateArrayArray = board.getStates();
		eStateGrid = new State[stateArrayArray.length][stateArrayArray[0].getItem().length];
		for (int i = 0; i < stateArrayArray.length; i++) {
			State[] stateArray = stateArrayArray[i].getItem();
			State[] newStateArray = new State[stateArray.length];
			for (int j = 0; j < stateArray.length; j++) {
				newStateArray[j] = stateArray[stateArray.length - j - 1];
			}
			eStateGrid[i] = newStateArray;
		}

		int prevTick = -1;
		int mapType = -1;

		while (true) {            
			za.co.entelect.challenge.Game eGame = null;
			try {
				eGame = service.getStatus();
			} catch (Exception e) {
				System.out.println("Exception: " + e.toString());
				System.out.println("Game Over!");
				return;
			}

			if (eGame.getCurrentTick() == prevTick) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
				continue;
			}

			System.out.println("===============================================================================");
			if (prevTick != -1 && prevTick + 1 != eGame.getCurrentTick()) {
				System.err.println("Missed state update, it's over for you :( ");
			}
			prevTick = eGame.getCurrentTick();

			System.out.println("getCurrentTick(): "+eGame.getCurrentTick());
			System.out.println("eStateGrid.length: " + eStateGrid.length);
			
			if (eGame.getEvents() != null) {
				if (eGame.getEvents().getBlockEvents() != null) {
					for (BlockEvent blockEvent : eGame.getEvents().getBlockEvents()) {
						System.out.println("eStateGrid[" + blockEvent.getPoint().getX() + "][" + blockEvent.getPoint().getY() +
								"] is set to: " + blockEvent.getNewState());
					}
				}
				if (eGame.getEvents().getUnitEvents() != null) {
					for (UnitEvent unitEvent : eGame.getEvents().getUnitEvents()) {
						System.out.println("unitEvent: "+unitEvent.toString());
					}
				}
			}
			
			int[] playerIdxHolder = new int[1];
			playerIdxHolder[0] = -1;
			prevXGameState = xGameState;
			xGameState = GameState.fromEGame(eGame, eStateGrid, eBullets, myName, playerIdxHolder, prevXGameState);			
			int playerIdx = playerIdxHolder[0];
			System.out.println("PlayerIdx: " + playerIdx);
			
			if (playerIdx == -1) {
				System.err.println("FATAL ERROR: player index could not be found.");
				playerIdx = 0;
			}
			stupidBot.setPlayerIndex(playerIdx);
			
			if (mapType == -1) {
				mapType = GameState.identifyMapType(xGameState.getMap()); 
				System.out.println("mapType set to: " + mapType);
			}
			xGameState.setMapType(mapType);
			
			if (extraOutput) {
				System.out.println("Game state:");
				System.out.println(xGameState.toString());	
			}

			ArrayList<za.co.entelect.challenge.Action> actions = new ArrayList<za.co.entelect.challenge.Action>();			

			if (playStyle.equals("Fire")) {
				actions.add(GameState.XActionToEAction(GameAction.ACTION_FIRE));
				actions.add(GameState.XActionToEAction(GameAction.ACTION_FIRE));
			} else if (playStyle.equals("North")) {
				actions.add(GameState.XActionToEAction(GameAction.ACTION_MOVE_NORTH));
				actions.add(GameState.XActionToEAction(GameAction.ACTION_MOVE_NORTH));
			} else if (playStyle.equals("FireMove")) {
				for (int i = 2*playerIdx; i < 2*playerIdx + 2; i++) {
					Tank tank = xGameState.getTanks()[i];
					if (!tank.isAlive()) {
						continue;
					}
					za.co.entelect.challenge.Action action = null;

					if (xGameState.getBullets()[i].isAlive()) {
						if (tank.getPosition().y < xGameState.getMap().length/2) {
							action = GameState.XActionToEAction(GameAction.ACTION_MOVE_NORTH);
						} else {
							action = GameState.XActionToEAction(GameAction.ACTION_MOVE_SOUTH);
						}
					} else {
						action = za.co.entelect.challenge.Action.FIRE;
					}

					actions.add(action);
				}
			} else {
				int[] gameActions = stupidBot.getActions(xGameState, 3000);
				for (int i = 0; i < 2; i++) {
					Tank tank = xGameState.getTanks()[playerIdx*2 + i];
					if (!tank.isAlive()) {
						continue;
					}
					za.co.entelect.challenge.Action action = GameState.XActionToEAction(gameActions[i]);
					actions.add(action);
				}
			}

			int numAliveTanks = 0;
			for (int i = 0; i < 2; i++) {
				if (xGameState.getTanks()[i].isAlive()) {
					numAliveTanks++;
				}
			}
			System.out.println("numAliveTanks : "+numAliveTanks);
			try {
				if (xGameState.getTanks()[playerIdx*2 + 0].isAlive() && actions.size() > 0) {
					System.out.println("T0 is doing: "+actions.get(0));
					service.setAction(xGameState.getTanks()[playerIdx*2 + 0].getID(), actions.get(0));
				}
				if (xGameState.getTanks()[playerIdx*2 + 1].isAlive() && actions.size() > 1) {
					System.out.println("T1 is doing: "+actions.get(1));
					service.setAction(xGameState.getTanks()[playerIdx*2 + 1].getID(), actions.get(1));
				}
			} catch (EndOfGameException e) {
				System.out.println("Game Over!");
				try {
					System.in.read();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			} catch (RemoteException e) {
				e.printStackTrace();
				break;
			} catch (Exception e) {
				System.out.println("Exception: " + e.toString());
				System.out.println("Game Over!");
				return;
			}

			long timeLeft = 0;
			timeLeft = Calendar.getInstance().getTimeInMillis() - eGame.getNextTickTime().getTimeInMillis();
			System.out.println("Time left: " + timeLeft + " ms");

			if (timeLeft > 20) {
				try {
					Thread.sleep((long) (timeLeft*0.9));
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
}
