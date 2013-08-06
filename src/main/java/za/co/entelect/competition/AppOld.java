package za.co.entelect.competition;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.axis.AxisFault;

import za.co.entelect.challenge.*;
import za.co.entelect.competition.bots.Bot;

/**
 * Hello world!
 *
 */
public class AppOld 
{
	public static void main( String[] args )
	{
		String myName = "Client1";
		if (args.length == 1) {
			myName = args[0];
		}
		
		Bot stupidBot = null;
		if (myName.equals("Client1")) {
			try {
				Constructor<?> player1Constructor = Class.forName("za.co.entelect.competition.bots.MinimaxFixedDepth").getConstructor(Integer.TYPE);
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
			//java.net.URL url = new java.net.URL("http://localhost:9090/ChallengePort");
			//java.net.URL url = new java.net.URL("http://localhost:8080/Axis2WSTest/services/ChallengeService");
			java.net.URL url = new java.net.URL("http://ec2-176-34-161-166.eu-west-1.compute.amazonaws.com/BattleCity/WebService/BasicGameHost.svc");
			
			service = new ChallengeServiceSoapBindingStub(url, null);
			service.setMaintainSession(true);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		State[][] eStateGrid = null;
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
		System.out.println("eStateGrid[0][0]: "+eStateGrid[0][0]);
		
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
			
//			for (BlockEvent blockEvent : game.getEvents().getBlockEvents()) {
//				System.out.println("blockEvent: "+blockEvent.toString());
//			}
//			for (UnitEvent unitEvent : game.getEvents().getUnitEvents()) {
//				System.out.println("unitEvent: "+unitEvent.toString());
//			}
//			for (Player player : game.getPlayers()) {
//				System.out.println("player: "+player.toString());
//			}
			
			
			GameState xGameState = null; //GameState.fromEGame(eGame, eStateGrid);
			System.out.println("Game state:");
			System.out.println(xGameState.toString());
			
			ArrayList<za.co.entelect.challenge.Action> actions = new ArrayList<za.co.entelect.challenge.Action>();			
			
			if (myName.equals("Client1")) {
				GameAction[] gameActions = stupidBot.getActions(xGameState, 3000);
				for (int i = 0; i < 2; i++) {
					Tank tank = xGameState.getTanks()[i];
					if (!tank.isAlive()) {
						continue;
					}
					za.co.entelect.challenge.Action action = GameState.XActionToEAction(gameActions[i]);
					actions.add(action);
				}
			} else {
				for (int i = 0; i < 2; i++) {
					Tank tank = xGameState.getTanks()[i];
					if (!tank.isAlive()) {
						continue;
					}
					za.co.entelect.challenge.Action action = null;
					
					if (prevTick == 0) {
						if (tank.getPosition().x < xGameState.getMap()[0].length/2) {
							action = za.co.entelect.challenge.Action.RIGHT;
						} else {
							action = za.co.entelect.challenge.Action.LEFT;
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
				for (int i = 0; i < actions.size(); i++) {
					za.co.entelect.challenge.Action action = actions.get(i);
					service.setAction(i, action);
					System.out.println("T"+(i+1)+" doing: "+action);
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
