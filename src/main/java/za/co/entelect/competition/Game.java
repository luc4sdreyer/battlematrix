package za.co.entelect.competition;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import za.co.entelect.challenge.Action;
import za.co.entelect.challenge.Board;
import za.co.entelect.challenge.Direction;
import za.co.entelect.challenge.SetAction;
import za.co.entelect.challenge.State;
import za.co.entelect.challenge.StateArray;
//import za.co.entelect.challenge.Board;
//import za.co.entelect.challenge.Direction;
//import za.co.entelect.challenge.Events;
//import za.co.entelect.challenge.Player;
//import za.co.entelect.challenge.State;
//import za.co.entelect.challenge.StateArray;
//import za.co.entelect.challenge.Unit;
import za.co.entelect.competition.bots.Bot;

public class Game {

	private Bot player1;
	private Bot player2;
	private GameState gameState;
	private za.co.entelect.challenge.Game eGame;
	private Board eBoard;
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
		
		if (this.gameState.getTickCount() > 200) {
			this.gameState.setStatus(GameState.STATUS_DRAW);
		}
		
		if (this.generateEGame) {
			this.eGame = null;
			this.eGame = getEGame();
			
			this.eBoard = null;
			this.eBoard = getEBoard();
		}
		
		boolean isActive = this.gameState.isActive();
		if (!isActive) {
			generateResult(this.result);
		}
		return isActive;
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
	
	public za.co.entelect.challenge.Game getEGame() {
		
		if (this.eGame == null) {
			this.eGame = new za.co.entelect.challenge.Game();
			
			//za.co.entelect.challenge.Player[] players = new za.co.entelect.challenge.Player[2];
			
			
			for (int i = 0; i < 2; i++) {
				za.co.entelect.challenge.Player ePlayer = new za.co.entelect.challenge.Player();
				
				Base xBase = getGameState().getBases()[i];
				if (xBase.isAlive()) {
					za.co.entelect.challenge.Base eBase = new za.co.entelect.challenge.Base();
					eBase.setX(xBase.getPosition().x);
					eBase.setY(xBase.getPosition().y);				
					ePlayer.setBase(eBase);
				}
				
				for (int tankIdx = 0; tankIdx < getGameState().getTanks().length; tankIdx++) {
					Tank xTank = getGameState().getTanks()[tankIdx];
					if (xTank.isAlive() && tankIdx/2 == i) {
						za.co.entelect.challenge.Unit eUnit = new za.co.entelect.challenge.Unit();
						eUnit.setId(tankIdx);
						eUnit.setX(xTank.getPosition().x);
						eUnit.setY(xTank.getPosition().y);						
						
						Direction eDirection = null;
						switch (xTank.getRotation()) {
							case GameAction.NORTH:	eDirection = Direction.UP;		break;
							case GameAction.EAST:	eDirection = Direction.RIGHT;	break;
							case GameAction.SOUTH:	eDirection = Direction.DOWN;	break;
							case GameAction.WEST:	eDirection = Direction.LEFT;	break;
						}
						eUnit.setDirection(eDirection);
						
						Action eAction = Action.NONE;
						if (xTank.getNextAction() != null) {
							switch (xTank.getNextAction().type) {
								case GameAction.MOVE:
									switch (xTank.getRotation()) {
										case GameAction.NORTH:	eAction = Action.UP;	break;
										case GameAction.EAST:	eAction = Action.RIGHT;	break;
										case GameAction.SOUTH:	eAction = Action.DOWN;	break;
										case GameAction.WEST:	eAction = Action.LEFT;	break;
									}
									break;
								case GameAction.FIRE:	eAction = Action.FIRE;	break;
								case GameAction.NONE:	eAction = Action.NONE;	break;
							}
						}
						eUnit.setAction(eAction);
						
						ePlayer.addUnits(eUnit);
					}
				}
				
				for (int bulletIdx = 0; bulletIdx < getGameState().getBullets().length; bulletIdx++) {
					Bullet xBullet = getGameState().getBullets()[bulletIdx];
					if (xBullet.isAlive() && bulletIdx/2 == i) {
						za.co.entelect.challenge.Bullet eBullet = new za.co.entelect.challenge.Bullet();
						eBullet.setId(bulletIdx);
						eBullet.setX(xBullet.getPosition().x);
						eBullet.setY(xBullet.getPosition().y);						
						
						Direction eDirection = null;
						switch (xBullet.getRotation()) {
							case GameAction.NORTH:	eDirection = Direction.UP;		break;
							case GameAction.EAST:	eDirection = Direction.RIGHT;	break;
							case GameAction.SOUTH:	eDirection = Direction.DOWN;	break;
							case GameAction.WEST:	eDirection = Direction.LEFT;	break;
						}
						eBullet.setDirection(eDirection);
						
						ePlayer.addBullets(eBullet);
					}
				}
				this.eGame.addPlayers(ePlayer);
			}		
									
			this.eGame.setCurrentTick(getGameState().getTickCount());
			System.out.println("this.eGame.getCurrentTick(): " + this.eGame.getCurrentTick());
			
			za.co.entelect.challenge.Events events = new za.co.entelect.challenge.Events();
			
			//BlockEvent b = new BlockEvent();
			//b.setNewState(State.EMPTY);			
			//za.co.entelect.challenge.Point ePoint = new za.co.entelect.challenge.Point();
			//ePoint.setX(0);
			//ePoint.setY(0);
			//b.setPoint(ePoint);
			//events.addBlockEvents(b);		
			
			//UnitEvent u = new UnitEvent();
			//events.addUnitEvents(u);			
			this.eGame.setEvents(events);
			
			this.getEBoard();
			
			Calendar c = Calendar.getInstance();
			c.add(Calendar.SECOND, 3);	
			this.eGame.setNextTickTime(c);			
		}
		
		return this.eGame;
	}
	
	public Board getEBoard() {
		if (this.eBoard == null) {
			this.eBoard = new Board();
			
			int[][] map = this.getGameState().getMap();
			for (int y = 0; y < map.length; y++) {
				StateArray eStateArray = new StateArray();
				for (int x = 0; x < map[0].length; x++) {
					State eState = null;
					switch (map[y][x]) {
						case Unit.WALL:		eState = State.FULL;	break;
						default:			eState = State.EMPTY;	break;
					}
					eStateArray.addItem(eState);
				}
				this.eBoard.addStates(eStateArray);
			}
		}
		return this.eBoard;
	}
	public void setAction(SetAction action, int playerIdx) {
//		ArrayList<Tank> tanks = new ArrayList<Tank>();
//		for (int tankIdx = 0; tankIdx < this.getGameState().getTanks().length; tankIdx++) {
//			Tank xTank = getGameState().getTanks()[tankIdx];
//			if (xTank.isAlive() && tankIdx/2 == playerIdx) {
//				tanks.add(xTank);
//			}
//		}
		
		
		GameAction xGameAction = null;
		
		switch (action.getArg1().getValue()) {
			case "UP":		xGameAction = new GameAction(GameAction.MOVE, GameAction.NORTH);	break;
			case "RIGHT":	xGameAction = new GameAction(GameAction.MOVE, GameAction.EAST);		break;
			case "DOWN":	xGameAction = new GameAction(GameAction.MOVE, GameAction.SOUTH);	break;
			case "LEFT":	xGameAction = new GameAction(GameAction.MOVE, GameAction.WEST);		break;
			case "FIRE":	xGameAction = new GameAction(GameAction.FIRE, GameAction.NORTH);	break;
			case "NONE":	xGameAction = new GameAction(GameAction.NONE, GameAction.NORTH);	break;
		}
		
		//tanks.get(action.getArg0()).setNextAction(xGameAction);
		if (playerIdx == 0) {
			GameAction[] gameAction = this.getPlayer1().getGameActions();
			if (gameAction == null) {
				gameAction = new GameAction[2];
				this.getPlayer1().setGameActions(gameAction);
			}
			gameAction[action.getArg0()] = xGameAction;
		} else if (playerIdx == 1) {
			GameAction[] gameAction = this.getPlayer2().getGameActions();
			if (gameAction == null) {
				gameAction = new GameAction[2];
				this.getPlayer2().setGameActions(gameAction);
			}
			gameAction[action.getArg0()] = xGameAction;
		}
		eGame.getPlayers()[playerIdx].getUnits()[action.getArg0()].setAction(action.getArg1());
	}
}
