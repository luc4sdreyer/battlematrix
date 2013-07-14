package za.co.entelect.competition;

import java.io.ObjectInputStream.GetField;
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
		System.out.println( "Hello World!" );

		ChallengeServiceSoapBindingStub service = null;
		try {
			java.net.URL url = new java.net.URL("http://localhost:9090/ChallengePort");
			service = new ChallengeServiceSoapBindingStub(url, null);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		State[][] gameState;
		try {
			gameState = service.login();
		} catch (NoBlameException e) {
			e.printStackTrace();
		} catch (EndOfGameException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		int prevTick = -1;

		while (true) {            
			za.co.entelect.challenge.Game game = null;
			try {
				game = service.getStatus();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			if (game.getCurrentTick() == prevTick) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			prevTick = game.getCurrentTick();

			System.out.println("getCurrentTick(): "+game.getCurrentTick());
			System.out.println("getPlayerName(): "+game.getPlayerName());
			for (BlockEvent blockEvent : game.getEvents().getBlockEvents()) {
				System.out.println("blockEvent: "+blockEvent.toString());
			}
			for (UnitEvent unitEvent : game.getEvents().getUnitEvents()) {
				System.out.println("unitEvent: "+unitEvent.toString());
			}
			for (Player player : game.getPlayers()) {
				System.out.println("player: "+player.toString());
			}
			
			try {
				service.setAction(0, za.co.entelect.challenge.Action.NONE);
				service.setAction(1, za.co.entelect.challenge.Action.NONE);
			} catch (EndOfGameException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			long timeLeft = Calendar.getInstance().getTimeInMillis() - game.getNextTickTime().getTimeInMillis();
			System.out.println("timeLeft: "+timeLeft);
			
			
			try {
				Thread.sleep(timeLeft);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
