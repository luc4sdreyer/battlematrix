package za.co.entelect.competition;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

import za.co.entelect.competition.bots.Bot;

public class Game {

	private Bot player1;
	private Bot player2;
	private GameState gameState;	
	
	public Game(Bot player1, Bot player2, GameState gameState) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.gameState = gameState;
	}
	public Game(String player1, String player2, String gameFile) {
		super();
		try {
			Constructor<?> player1Constructor = Class.forName(player1).getConstructor(Integer.TYPE);
			this.player1 = (Bot) player1Constructor.newInstance(0);

			Constructor<?> player2Constructor = Class.forName(player2).getConstructor(Integer.TYPE);
			this.player2 = (Bot) player2Constructor.newInstance(1);
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
		
		this.gameState = newGame(gameFile);
	}	
	public Bot getPlayer1() {
		return player1;
	}
	public void setPlayer1(Bot player1) {
		this.player1 = player1;
	}
	public Bot getPlayer2() {
		return player2;
	}
	public void setPlayer2(Bot player2) {
		this.player2 = player2;
	}
	public GameState getGameState() {
		return gameState;
	}
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	public int getTickCount() {
		return this.gameState.getTickCount();
	}	
	public boolean nextTick(GameAction[] overrideActions, GameAction[] realActions, int timeLimitMS) {
		GameAction[] p1Moves = player1.getActions(this.gameState, timeLimitMS);
		GameAction[] p2Moves = player2.getActions(this.gameState, timeLimitMS);
		GameAction[] actions = new GameAction[4];
		
		actions[0] = p1Moves[0];
		actions[1] = p1Moves[1];
		actions[2] = p2Moves[0];
		actions[3] = p2Moves[1];
		
		for (int i = 0; i < 4; i++) {
			if (overrideActions != null && overrideActions[i] != null) {
				this.gameState.getTanks()[i].setNextAction(overrideActions[i]);
			} else {
				this.gameState.getTanks()[i].setNextAction(actions[i]);
			}			
		}
		if (realActions != null) {
			for (int i = 0; i < 4; i++) {
				realActions[i] = this.gameState.getTanks()[i].getNextAction();
			}
		}
		
		this.gameState.nextTick();
		
		if (this.gameState.getTickCount() > 200) {
			this.gameState.setStatus(GameState.STATUS_DRAW);
		}
		return this.gameState.isActive();
	}
	
	public static GameState newGame(String filename) {
		Scanner in = null;
		try {
			in = new Scanner(new File(".\\assets\\"+filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> file = new ArrayList<String>(); 
		while (in.hasNext()) {
			String line = in.nextLine();
			file.add(line);
		}
		in.close();
		
		int[][] map = new int[file.size()][file.get(0).length()];
		Tank[] tanks = new Tank[4];
		Base[] bases = new Base[2];
		Bullet[] bullets = new Bullet[4];
		for (int i = 0; i < bullets.length; i++) {
			bullets[i] = new Bullet(new Point(0,0), 0, false);
		}
		ArrayList<Collision> collisions = new ArrayList<Collision>();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (file.get(y).charAt(x) == '_') {
					map[y][x] = 0;
				} else if (file.get(y).charAt(x) == '#') {
					map[y][x] = 1;
				} else if (file.get(y).charAt(x) == 'A') {
					tanks[0] = new Tank(new Point(x,y), 2, null);
				} else if (file.get(y).charAt(x) == 'B') {
					tanks[1] = new Tank(new Point(x,y), 2, null);
				} else if (file.get(y).charAt(x) == 'X') {
					tanks[2] = new Tank(new Point(x,y), 2, null);
				} else if (file.get(y).charAt(x) == 'Y') {
					tanks[3] = new Tank(new Point(x,y), 2, null);
				} else if (file.get(y).charAt(x) == 'C') {
					bases[0] = new Base(new Point(x,y), 2);
					map[y][x] = Unit.BASE1;
				} else if (file.get(y).charAt(x) == 'Z') {
					bases[1] = new Base(new Point(x,y), 2);
					map[y][x] = Unit.BASE2;
				//} else if (file.get(y).charAt(x) == '*') {
				//	bullets[i] = new Bullet(new Point(x,y), 2);
				} else if (file.get(y).charAt(x) != '_') {
					System.err.println("UNKNOWN SYMBOL IN MAP.TXT: "+file.get(y).charAt(x));
				}
			}
		}
		for (int i = 0; i < tanks.length; i++) {
			Tank t = tanks[i];
			for (int y2 = 0; y2 < GameState.tankSize; y2++) {
				for (int x2 = 0; x2 < GameState.tankSize; x2++) {
					map[t.getPosition().y+y2][t.getPosition().x+x2] = Unit.TANK1A+i;
				}
			}
		}
		GameState newGame = new GameState(map, bullets, tanks, bases, collisions, 0, GameState.STATUS_ACTIVE);
		//System.out.println("New map:");
		//System.out.println(newGame.toString());
		return newGame;
	}
}
