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

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		Properties prop = new Properties();		
		String propertiesFile = "config.properties";
		
		String playStyle = new String();
		String myName = new String();
		
		try {
			//load a properties file
			prop.load(new FileInputStream(propertiesFile));

			//get the property value and print it out
			playStyle = prop.getProperty("playStyle");
			
			System.out.println("playStyle=" + playStyle);
		} catch (IOException ex) {
			System.err.println("Could not read " + propertiesFile);
		}

		String endPoint = new String();
		if (args.length == 1) {
			endPoint = args[0];
		} else if (args.length == 2) {
			endPoint = args[0];
			myName = args[1];
		}
		System.out.println("Connecting to: " + endPoint);
		System.out.println("myName: " + myName);

		Bot stupidBot = null;
		if (playStyle.equals("Client1")) {
			try {
				Constructor<?> player1Constructor = Class.forName("za.co.entelect.competition.bots.MinimaxFixedDepth2").getConstructor(Integer.TYPE);
				stupidBot = (Bot) player1Constructor.newInstance(0);
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
		}

		ChallengeServiceSoapBindingStub service = null;
		try {
			java.net.URL url = new java.net.URL(endPoint);

			service = new ChallengeServiceSoapBindingStub(url, null);
			service.setMaintainSession(true);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		State[][] eStateGrid = null;
		HashMap<Integer, Integer> eBullets = new HashMap<Integer, Integer>();
		GameState xGameState = null;
		GameState prevXGameState = null;
		
		try {
			eStateGrid = service.login();
		} catch (NoBlameException e) {
			e.printStackTrace();
		} catch (EndOfGameException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//		catch (java.net.ConnectException e) {
		//			System.out.println("Could not connect");
		//		}

		int prevTick = -1;

		while (true) {            
			za.co.entelect.challenge.Game eGame = null;
			try {
				eGame = service.getStatus();
			} catch (RemoteException e) {
				e.printStackTrace();				
			}
			//System.out.println("Got status. Game: "+game);
			//System.out.println("game.getCurrentTick(): "+game.getCurrentTick());

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
			prevTick = eGame.getCurrentTick();

			System.out.println("getCurrentTick(): "+eGame.getCurrentTick());
			//System.out.println("getPlayerName(): "+game.getPlayerName());

			if (eGame.getEvents() != null) {
				if (eGame.getEvents().getBlockEvents() != null) {
					for (BlockEvent blockEvent : eGame.getEvents().getBlockEvents()) {
						eStateGrid[blockEvent.getPoint().getX()][blockEvent.getPoint().getY()] = blockEvent.getNewState();
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
			
			System.out.println("Game state:");
			System.out.println(xGameState.toString());

			ArrayList<za.co.entelect.challenge.Action> actions = new ArrayList<za.co.entelect.challenge.Action>();			

			if (playStyle.equals("Client1")) {
				int[] gameActions = stupidBot.getActions(xGameState, 3000);
				for (int i = 0; i < 2; i++) {
					Tank tank = xGameState.getTanks()[i];
					if (!tank.isAlive()) {
						continue;
					}
					za.co.entelect.challenge.Action action = GameState.XActionToEAction(gameActions[i]);
					actions.add(action);
				}
			} else if (playStyle.equals("Fire")) {
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

					//				int rand = (int)(Math.random()*5);
					//				switch(rand) {
					//					case 0: 	a1 = za.co.entelect.challenge.Action.UP; 	break;
					//					case 1: 	a1 = za.co.entelect.challenge.Action.RIGHT; break;
					//					case 2: 	a1 = za.co.entelect.challenge.Action.DOWN;	break;
					//					case 3: 	a1 = za.co.entelect.challenge.Action.RIGHT; break;
					//					case 4: 	a1 = za.co.entelect.challenge.Action.FIRE; 	break;
					//				}

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
//				for (int i = 0; i < actions.size(); i++) {
//					za.co.entelect.challenge.Action action = actions.get(i);
//					service.setActions(i, action);
//					System.out.println("T"+(i+1)+" doing: "+action);
//				}
				if (xGameState.getTanks()[playerIdx*2 + 0].isAlive()) {
					System.out.println("T0 is doing: "+actions.get(0));
					@SuppressWarnings("unused")
					Delta d1 = service.setAction(xGameState.getTanks()[playerIdx*2 + 0].getID(), actions.get(0));
				}
				if (xGameState.getTanks()[playerIdx*2 + 1].isAlive()) {
					System.out.println("T1 is doing: "+actions.get(1));
					@SuppressWarnings("unused")
					Delta d2 = service.setAction(xGameState.getTanks()[playerIdx*2 + 1].getID(), actions.get(1));
				}
			} catch (EndOfGameException e) {
				System.out.println("Game Over!");
				try {
					System.in.read();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				break;
			}

			long timeLeft = 0;
			if (eGame.getNextTickTime() == null) {
				System.err.println("CRITICAL SERVER ERROR: eGame.getNextTickTime() == null");
				timeLeft = 0;
			} else {
				timeLeft = eGame.getNextTickTime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
			}

			if (timeLeft > 0) {
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
