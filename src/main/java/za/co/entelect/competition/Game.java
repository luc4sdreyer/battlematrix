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
	protected za.co.entelect.challenge.Game[] eGame;
	private boolean generateEGame;
	private Result result;
	
	public Game(Bot player1, Bot player2, GameState gameState, boolean generateEGame) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.gameState = gameState;
		this.generateEGame = generateEGame;
	}
	
	public Game(String player1, String player2, String gameFile, boolean generateEGame) {
		super();
		init(player1, player2, generateEGame);
		this.gameState = newGame(gameFile);
	}
	
	public Game(String player1, String player2, ArrayList<String> file, boolean generateEGame) {
		super();
		init(player1, player2, generateEGame);
		this.gameState = newGame(file);
	}
	
	private void init(String player1, String player2, boolean generateEGame) {
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

		this.generateEGame = generateEGame;
		this.result = new Result();
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
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public boolean nextTick(GameAction[] overrideActions, GameAction[] performedActions, int timeLimitMS) {
		GameAction[] p1Moves = player1.getActions(this.gameState, timeLimitMS);
		GameAction[] p2Moves = player2.getActions(this.gameState, timeLimitMS);
		GameAction[] actions = new GameAction[4];
		
		actions[0] = p1Moves[0];
		actions[1] = p1Moves[1];
		actions[2] = p2Moves[0];
		actions[3] = p2Moves[1];
		
		for (int i = 0; i < 4; i++) {
			if (actions[i] == null) {
				actions[i] = new GameAction(GameAction.NONE, GameAction.NORTH);
			}
			if (overrideActions != null && overrideActions[i] != null) {
				this.gameState.getTanks()[i].setNextAction(overrideActions[i]);
			} else {
				this.gameState.getTanks()[i].setNextAction(actions[i]);
			}			
		}
		if (performedActions != null) {
			for (int i = 0; i < 4; i++) {
				performedActions[i] = this.gameState.getTanks()[i].getNextAction();
			}
		}
		
		this.gameState.nextTick();
		
		if (this.gameState.getTickCount() > GameState.maxTurns) {
			this.gameState.setStatus(GameState.STATUS_DRAW);
		}
		
		if (this.generateEGame) {
			this.eGame = null;
			this.eGame = getEGame();
			
			this.resetEBoard();
		}
		
		boolean isActive = this.gameState.isActive();
		if (!isActive) {
			generateResult(this.result);
		}
		return isActive;
	}
	
	public void resetEBoard() {
	}

	public void generateResult(Result result) {		
		result.score.name[0] = this.getPlayer1().getName();
		result.score.name[1] = this.getPlayer2().getName();
		
		switch (this.getGameState().getStatus()) {
			case GameState.STATUS_PLAYER1_WINS:		result.score.wld[0]++;	break;
			case GameState.STATUS_PLAYER2_WINS:		result.score.wld[1]++;	break;
			case GameState.STATUS_DRAW:				result.score.wld[2]++;	break;
			default:								result.score.wld = null;
		}
		
		result.score.numTicks = this.getGameState().getTickCount();
	}
	
	public static ArrayList<String> readGameFromFile(String filename) {
		Scanner in = null;
		//File f = new File("./");
		//System.out.println(f.getAbsolutePath());
		try {
			in = new Scanner(new File("./assets/"+filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> file = new ArrayList<String>(); 
		while (in.hasNext()) {
			String line = in.nextLine();
			file.add(line);
		}
		in.close();
		
		return file;
	}
	
	public static GameState newGame(String filename) {		
		return newGame(readGameFromFile(filename));
	}
	
	public static GameState newGame(ArrayList<String> file) {		
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
				char c = file.get(y).charAt(x);
				if (c == '_') {
					map[y][x] = 0;
				} else if (c == '#') {
					map[y][x] = 1;
				} else if (c == 'A') {
					if (tanks[0] == null) {
						tanks[0] = new Tank(new Point(x,y), getRotationFromChar(file.get(y).charAt(x+1)), null);
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'B') {
					if (tanks[1] == null) {
						tanks[1] = new Tank(new Point(x,y), getRotationFromChar(file.get(y).charAt(x+1)), null);
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'X') {
					if (tanks[2] == null) {
						tanks[2] = new Tank(new Point(x,y), getRotationFromChar(file.get(y).charAt(x+1)), null);
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'Y') {
					if (tanks[3] == null) {
						tanks[3] = new Tank(new Point(x,y), getRotationFromChar(file.get(y).charAt(x+1)), null);
					} else {
						map[y][x] = 0;
					}
				} else if (c == 'C') {
					bases[0] = new Base(new Point(x,y), 2);
					map[y][x] = Unit.BASE1;
				} else if (c == 'Z') {
					bases[1] = new Base(new Point(x,y), 2);
					map[y][x] = Unit.BASE2;
				//} else if (c == '*') {
				//	bullets[i] = new Bullet(new Point(x,y), 2);
				} else if (c == '_' || c == '0' || c == '1' || c == '2' || c == '3') {
					map[y][x] = 0;
				} else {
					System.err.println("UNKNOWN SYMBOL IN MAP.TXT: "+c);
				}
			}
		}
		for (int i = 0; i < tanks.length; i++) {
			Tank t = tanks[i];
			if (tanks[i] == null) {
				tanks[i] = new Tank(new Point(0,0), 2, null, false);
				continue;
			}
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
	
	private static int getRotationFromChar(char c) {
		int rotation = 2;		
		if (Character.isDigit(c)) {
			int n = Character.getNumericValue(c);
			if (n >= 0 && n <= 3) {
				rotation = n;
			}
		}
		return rotation;
	}

	public za.co.entelect.challenge.Game[] getEGame() {
		return null;
	}
}
