package za.co.entelect.competition;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis.AxisFault;

import za.co.entelect.challenge.*;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		String myName = "NoName";
		if (args.length == 1) {
			myName = args[0];
		}
		 
		ChallengeServiceSoapBindingStub service = null;
		try {
			//java.net.URL url = new java.net.URL("http://localhost:9090/ChallengePort");
			java.net.URL url = new java.net.URL("http://localhost:8080/Axis2WSTest/services/ChallengeService");
			//javax.xml.rpc.Service s = 
			service = new ChallengeServiceSoapBindingStub(url, null);
			service.setMaintainSession(true);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		State[][] gameState = null;
		try {
			gameState = service.login(myName);
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
		System.out.println("gameState[0][0]: "+gameState[0][0]);
		
		int prevTick = -1;

		while (true) {            
			za.co.entelect.challenge.Game game = null;
			try {
				game = service.getStatus();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			//System.out.println("Got status. Game: "+game);
			
			if (game.getCurrentTick() == prevTick) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
				continue;
			}
			
			prevTick = game.getCurrentTick();

			System.out.println("getCurrentTick(): "+game.getCurrentTick());
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
			
			za.co.entelect.challenge.Action a1 = null;
			za.co.entelect.challenge.Action a2 = null;
			
			int rand = (int)Math.random()*5;
			switch(rand) {
				case 0: 	a1 = za.co.entelect.challenge.Action.UP; 	break;
				case 1: 	a1 = za.co.entelect.challenge.Action.RIGHT; break;
				case 2: 	a1 = za.co.entelect.challenge.Action.DOWN;	break;
				case 3: 	a1 = za.co.entelect.challenge.Action.RIGHT; break;
				case 4: 	a1 = za.co.entelect.challenge.Action.FIRE; 	break;
			}
			
			rand = (int)Math.random()*5;
			switch(rand) {
				case 0: 	a2 = za.co.entelect.challenge.Action.UP; 	break;
				case 1: 	a2 = za.co.entelect.challenge.Action.RIGHT; break;
				case 2: 	a2 = za.co.entelect.challenge.Action.DOWN; 	break;
				case 3: 	a2 = za.co.entelect.challenge.Action.RIGHT; break;
				case 4: 	a2 = za.co.entelect.challenge.Action.FIRE; 	break;
			}
			
			try {
				service.setAction(0, a1);
				service.setAction(1, a2);
			} catch (EndOfGameException e) {
				e.printStackTrace();
				break;
			} catch (RemoteException e) {
				e.printStackTrace();
				break;
			}
			
			long timeLeft = game.getNextTickTime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
			//System.out.println("timeLeft: "+timeLeft);
			
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
