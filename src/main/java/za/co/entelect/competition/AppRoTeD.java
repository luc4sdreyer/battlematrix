package za.co.entelect.competition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.axis.AxisFault;

import org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.*;
import org.tempuri.*;


import za.co.entelect.competition.bots.Bot;

/**
 * Hello world!
 *
 */
public class AppRoTeD 
{
	public static void main( String[] args )
	{
		String myName2 = "Client1";
		if (args.length == 1) {
			myName2 = args[0];
		}
		
		Bot stupidBot = null;
		if (myName2.equals("Client1")) {
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
		
		BasicHttpBinding_IGameInterfaceStub service = null;
		try {
			//java.net.URL url = new java.net.URL("http://localhost:9090/ChallengePort");
			//java.net.URL url = new java.net.URL("http://localhost:8080/Axis2WSTest/services/ChallengeService");
			java.net.URL url = new java.net.URL("http://ec2-176-34-161-166.eu-west-1.compute.amazonaws.com/BattleCity/WebService/BasicGameHost.svc");
			
			service = new BasicHttpBinding_IGameInterfaceStub(url, null);
			service.setMaintainSession(true);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		//State[][] eStateGrid = null;
		String pName = "Lucas_StupidBot";
		String pKey = "sdf$%hgj&*346fdg9084tretg4e5tdthe6rYh45%$4h%5heghyl";		
		ReturnValues loginResult = null;
		try {
			loginResult = service.doLogin(pName, pKey);
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		System.out.println("loginResult: "+loginResult);
		
		GameInfoTickBoard gameInfoTickBoard = null;
		int prevTick = -1;

		while (true) {
			
			try {
				gameInfoTickBoard = service.getGameInfoTickBoard(pName, pKey);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			if (gameInfoTickBoard.getCurrentTick() == prevTick) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
				continue;
			}
			
			if (gameInfoTickBoard.getIsComplete()) {
				System.out.println("Game Over!");
			}
			
			prevTick = gameInfoTickBoard.getCurrentTick();

			System.out.println("============================== Current Tick(): " + prevTick + 
							   "==============================");
			
			//DateSync dateSync = service.getDateSync(pName, pKey, Calendar.getInstance());
			//Board board = gameInfoTickBoard.getTickBoard();

			GameState xGameState = GameState.fromRGame(gameInfoTickBoard);
			System.out.println("Game state:");
			System.out.println(xGameState.toString());
			
			ArrayList<Actions> actions = new ArrayList<Actions>();			
			
			int[] gameActions = stupidBot.getActions(xGameState, 3000);
			for (int i = 0; i < 2; i++) {
				Tank tank = xGameState.getTanks()[i];
				if (!tank.isAlive()) {
					continue;
				}
				Actions action = GameState.XActionToRAction(gameActions[i]);
				actions.add(action);
			}
			
			int numAliveTanks = 0;
			for (int i = 0; i < 2; i++) {
				if (xGameState.getTanks()[i].isAlive()) {
					numAliveTanks++;
				}
			}
			System.out.println("numAliveTanks : "+numAliveTanks);
			for (int i = 0; i < actions.size(); i++) {
				Actions action = actions.get(i);
				try {
					service.setActionID(pName, pKey, i, action);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				System.out.println("T"+(i+1)+" doing: "+action);
			}
			
			long timeLeft = 0;
			timeLeft = gameInfoTickBoard.getNextTickTime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
			
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
