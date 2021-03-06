package za.co.entelect.competition;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.*;
import javax.swing.*;

/**
 * The main class (GUI) plays and displays a game between two bots.
 * The game is configured by the file configGUI.properties:
 * 		player1:		The fully qualified name of any bot in the za.co.entelect.competition.bots package.
 * 						Example: za.co.entelect.competition.bots.BruteV2 or za.co.entelect.competition.bots.Random.
 * 		player2:		Same as player1.
 * 		mapName:		The filename of the map. Maps are loaded from the assets folder.
 * 						Example: "mapE1.txt" for Entelect's first map.
 * 		useMoveList:	Load \assets\movelist.txt to overwrite any or all of any bot's actions. The movelist follows 
 * 						the mapping used in the GameAction class. A bot will ignore all actions after an 'X' character. 						
 * 						The order of actions is: P1 bot1, P1 bot2, P2 bot1, P2 bot2. The Simulator class can generate
 * 						movelists based on certain conditions. The following example will make P1 bot1 move down, 
 * 						P1 bot2 will not be overwritten, P2 bot1 will keep firing and P2 bot2 will do nothing:
 * 
 * 						v X F .
 * 						v . F . 
 * 						v . F .
 * 						v . F .
 * 						  
 */

class UserInput {
	private Calendar timePressed;
	private int action;
	private boolean pressed;
	public UserInput(Calendar timePressed, int action, boolean pressed) {
		super();
		this.timePressed = timePressed;
		this.action = action;
		this.pressed = pressed;
	}
	public Calendar getTimePressed() {
		return timePressed;
	}
	public void setTimePressed(Calendar timePressed) {
		this.timePressed = timePressed;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public boolean isPressed() {
		return pressed;
	}
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
}

class Collision extends Unit {
	public Collision(Point position, int rotation) {
		super(position, rotation);
	}
	public Collision(Point position, int rotation, boolean alive) {
		super(position, rotation, alive);
	}
	public Collision clone() {
		return new Collision(new Point(this.position), this.rotation, this.alive);
	}
}

class ImageDrawingComponent extends Component implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage[] tank1A;
	private BufferedImage[] tank1B;
	private BufferedImage[] base1;
	private BufferedImage[] tank2A;
	private BufferedImage[] tank2B;
	private BufferedImage[] base2;
	private BufferedImage[] wall;
	private BufferedImage[] bullet;
	private BufferedImage[] empty;
	private BufferedImage[] collision;
	private int blockSize = 3;
	private int timercount = 0;
	private GameState previousGameState = null;
	private GameState currentGameState = null;
	public GameState nextGameState = null;
	private double drawScale = 3;
    private Timer timer;
    private final int DELAY;
    private Object syncObject;
    
    public Point preferredSize;

	public ImageDrawingComponent(int delay, Object syncObject) {
		tank1A = initImage(".\\assets\\Tank1A.png");
		tank1B = initImage(".\\assets\\Tank1B.png");
		base1 = initImage(".\\assets\\Base1.png");
		tank2A = initImage(".\\assets\\Tank2A.png");
		tank2B = initImage(".\\assets\\Tank2B.png");
		base2 = initImage(".\\assets\\Base2.png");
		wall = initImage(".\\assets\\Wall.png");
		bullet = initImage(".\\assets\\Bullet.png");
		empty = initImage(".\\assets\\Empty.png");
		collision = initImage(".\\assets\\Collision.png");		

		DELAY = delay;
        timer = new Timer(DELAY, this);
        this.syncObject = syncObject;
        //addKeyListener(new TAdapter());
        setFocusable(true);
	}
	
	public double getScale() {
		return drawScale;
	}

	public void setScale(double scale) {
		this.drawScale = scale;
	}

	public GameState getPreviousGameState() {
		return previousGameState;
	}
	public void setPreviousGameState(GameState previousGameState) {
		this.previousGameState = previousGameState;
	}
	public GameState getCurrentGameState() {
		return currentGameState;
	}
	public void setCurrentGameState(GameState currentGameState) {
		this.currentGameState = currentGameState;
	}
	public int getTimercount() {
		return timercount;
	}
	public void setTimercount(int timercount) {
		this.timercount = timercount;
	}
	
	public void setNextGameState(GameState nextGameState) {
		if (this.nextGameState == null) {
			//Util.print("nextGameState updated");
			this.nextGameState = nextGameState.clone();
		} else {
			System.err.println("nextGameState overwritten!");
			int[] a = new int[1]; a[1] = 1;
		}
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	private BufferedImage[] initImage(String path) {
		BufferedImage[] image = new BufferedImage[4];
		try {
			image[0] = ImageIO.read(new File(path));
			if (image[0].getType() != BufferedImage.TYPE_INT_ARGB) {
				BufferedImage bi2 = new BufferedImage(image[0].getWidth(), image[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics big = bi2.getGraphics();
				big.drawImage(image[0], 0, 0, null);
				image[0] = bi2;
			}
		} catch (IOException e) {
			System.out.println("Image could not be read");
			//            System.exit(1);
		}
		
		for (int i = 1; i < 4; i++) {
			if (image[0].getHeight() != 3) {
				double rotationRequired = i*Math.PI/2;
				double locationX = 0;
				double locationY = 0;
				if (i == 1) {
					locationX = image[0].getHeight() / 2;
					locationY = image[0].getHeight() / 2;
				} else if (i == 3) {
					locationX = image[0].getWidth() / 2;
					locationY = image[0].getWidth() / 2;
				} else {
					locationX = image[0].getWidth() / 2;
					locationY = image[0].getHeight() / 2;					
				}
				AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);		
				image[i] = op.filter(image[0], null);
			} else {
				image[i] = deepCopy(image[0]);
			}
		}
		for (int i = 0; i < 4; i++) {
			if (this.drawScale != 1) {
				AffineTransform tx = AffineTransform.getScaleInstance(this.drawScale, this.drawScale);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				image[i] = op.filter(image[i], null);
			}
		}
		
		return image;	
	}

	public Dimension getPreferredSize() {
		if (preferredSize != null) {
			return new Dimension((int)this.drawScale*this.blockSize*(preferredSize.x+1), (int)this.drawScale*this.blockSize*(preferredSize.y+1));
		} else {
			return new Dimension((int)this.drawScale*this.blockSize*13*5, (int)this.drawScale*this.blockSize*12*5);
		}
	}
	
	private void drawTank(Graphics g, BufferedImage[] img, int idx, GameState prevGS,	GameState currGS, 
			double drawScale, int numFrames) {
		if (currGS.getTanks()[idx].isAlive()) {
			Point internalOffset = new Point(0,0);
			if (currGS.getTanks()[idx].getRotation() == 2) {
				internalOffset.translate(1, 0);
			}
			int frame = 0;
			// Don't animate the tank if it didn't move
			if (prevGS.getTanks()[idx].getPosition().equals(currGS.getTanks()[idx].getPosition())) {
				frame = 0;
			} else {
				frame = (timercount+1)%2;
			}
			drawGrid(g, img, prevGS.getTanks()[idx].getPosition(), currGS.getTanks()[idx].getPosition(), internalOffset,
					currGS.getTanks()[idx].getRotation(), this.drawScale, frame, 2);
		}		
	}

	private void drawGrid(Graphics g, BufferedImage[] img, Point previousPos, Point currentPos, Point internalOffset,
			int rotation, double scale, int frame, int numFrames) {
		int x = ((currentPos.x - previousPos.x) * (this.timercount+1)) + (previousPos.x * this.blockSize); // + internalOffset.x;
		int y = ((currentPos.y - previousPos.y) * (this.timercount+1)) + (previousPos.y * this.blockSize); // + internalOffset.y;
		drawGrid(g, img, x, y, rotation, scale, frame, numFrames);
	}	
	
	private void drawGrid(Graphics g, BufferedImage[] img, int pixelX, int pixelY,
			int rotation, double scale, int frame, int numFrames) {	
		Graphics2D g2 = (Graphics2D) g;	
		
		if (rotation == 0 || rotation == 2) {
			rotation = (rotation + 2) % 4;
		}
		if (img[0].getHeight() == 3) {
			rotation = 0;
		}
		
		BufferedImage toDraw = img[rotation];
		
		if (numFrames != 1) {
			if (rotation == 0) {
				toDraw = toDraw.getSubimage(img[rotation].getWidth()*frame/numFrames, 0, img[rotation].getWidth()/numFrames, img[rotation].getHeight());
			} else if (rotation == 1) {
				toDraw = toDraw.getSubimage(0, img[rotation].getHeight()*frame/numFrames, img[rotation].getWidth(), img[rotation].getHeight()/numFrames);
			} else if (rotation == 2) {
				toDraw = toDraw.getSubimage(img[rotation].getWidth()*(numFrames - 1)/numFrames - img[rotation].getWidth()*frame/numFrames, 0, img[rotation].getWidth()/numFrames, img[rotation].getHeight());
			} else if (rotation == 3) {
				toDraw = toDraw.getSubimage(0, img[rotation].getHeight()*(numFrames - 1)/numFrames - img[rotation].getHeight()*frame/numFrames, img[rotation].getWidth(), img[rotation].getHeight()/numFrames);
			} 
		}
		
		g2.drawImage(toDraw, (int)Math.round(pixelX*scale), (int)Math.round(pixelY*scale), null);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		GameState currGS = this.currentGameState;
		GameState prevGS = this.previousGameState;
		
		if (currGS == null) {
			return;
		} else if (prevGS == null) {
			prevGS = currGS;
		}
				
		for (int y = 0; y < currGS.getMap().length; y++) {
			for (int x = 0; x < currGS.getMap()[0].length; x++) {
				if (currGS.getMap()[y][x] == 1) {
					drawGrid(g, wall, x*blockSize, y*blockSize, 0, this.drawScale, 0, 1);
				} else {
					drawGrid(g, empty, x*blockSize, y*blockSize, 0, this.drawScale, 0, 1);
				} 
			}
		}
		
		//Graphics2D g2 = (Graphics2D) g;
		int idx = 0;
		drawTank(g, tank1A, idx++, prevGS, currGS, this.drawScale, 2);
		drawTank(g, tank1B, idx++, prevGS, currGS, this.drawScale, 2);
		drawTank(g, tank2A, idx++, prevGS, currGS, this.drawScale, 2);
		drawTank(g, tank2B, idx++, prevGS, currGS, this.drawScale, 2);
		
		Point internalOffset = new Point(0, 0);
		idx = 0;
		if (currGS.getBases()[idx].isAlive()) {
			drawGrid(g, base1, prevGS.getBases()[idx].getPosition(), currGS.getBases()[idx].getPosition(), internalOffset,
					currGS.getBases()[idx].getRotation(), this.drawScale, 0, 1);
		}
		idx++;
		if (currGS.getBases()[idx].isAlive()) {
			drawGrid(g, base2, prevGS.getBases()[idx].getPosition(), currGS.getBases()[idx].getPosition(), internalOffset,
					currGS.getBases()[idx].getRotation(), this.drawScale, 0, 1);
		}
		
		internalOffset = new Point(8,8);
		for (int i = 0; i < currGS.getBullets().length; i++) {
			Bullet b = currGS.getBullets()[i];
			if (!b.isAlive()) {
				continue;
			}
			drawGrid(g, bullet, Util.movePoint(b.getPosition(), (b.getRotation()+2) % 4), b.getPosition(), internalOffset,
					b.getRotation(), this.drawScale, 0, 1);
		}

		for (int i = 0; i < currGS.getCollisions().size(); i++) {
			drawGrid(g, collision, currGS.getCollisions().get(i).getPosition(), currGS.getCollisions().get(i).getPosition(), internalOffset,
					currGS.getCollisions().get(i).getRotation(), this.drawScale, (timercount)/2, 8);
		}
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.timercount++;
		if (timercount >= 3) {
			// Time to update the gameState 
			timercount = 0;

			if (GUI.absoluteTiming) {
				checkAndUpdateGameState();
				
				synchronized(syncObject) {
				    syncObject.notify();
				}
			} else {
//				synchronized(syncObject) {
//				    try {
//				    	System.out.println("Painter waiting");
//				        syncObject.wait();
//				    } catch (InterruptedException e1) {
//				    	e1.printStackTrace();
//				    }
//				}
			}
			
		}
        repaint();
	}

	public void checkAndUpdateGameState() {
		if (this.nextGameState == null) {
			System.err.println("nextGameState == null at frame update");
			int[] a = new int[1]; a[1] = 1;
		} else {
			if (this.currentGameState != null) {
				this.previousGameState = this.currentGameState.clone();
			}
			this.currentGameState = this.nextGameState.clone();
			this.nextGameState = null;
		}
	}

	public void startTimer() {
        timer.start();
	}
}

public class GUI extends JApplet {
	
	//TODO: Just a marker!
	public final static int DELAY = 100;
	public final static boolean absoluteTiming = false;
	
	public static final long serialVersionUID = 5023415827458014290L;
	public JButton b1;
	public final ImageDrawingComponent id;
	public final int frames = 3;
	public int frameTime = DELAY/frames;
	public long sleepTime = (long)(frameTime*frames*1);
	public boolean nextTick = false;
	public long stateTimeNS = 0;
	public Object syncObject;							// syncObject is used to sync the animation and logic threads.
	public BufferedWriter fileWriter;
	public ArrayList<Integer>[] moveList;
    //private GameAction p1reqAction;
    //private GameAction p1reqAction2;
    private UserInput[] userInput;   
	//static GameState gameState;
    Game game;

	public static void main(String s[]) {
		JFrame f = new JFrame("ImageDrawing");
		final GUI mainGUI = new GUI();
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				try {
//					mainGUI.fileWriter.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
				System.exit(0);
			}
		});
		
		//java.lang.Runtime.getRuntime().addShutdownHook(Thread.currentThread());

		
		mainGUI.init();
		f.add("Center", mainGUI);
		
		//GameState gameState = MapGenerator.generateRandom(80, 100, 60, 80, 5, 0.10, true, true, 1, 0, 92555845741342L); //50851925610806L);
		//ArrayList<String> gameList = MapGenerator.generateRandom(80, 100, 80, 100, 5, 0.15, true, true, 1, 0, 92555845741342L).toStringList();
		
		Properties prop = new Properties();		
		String propertiesFile = "configGUI.properties";
		
		String player1 = null;
		String player2 = null;
		String mapName = null;
		boolean useMoveList = false;	
		
		try {
			prop.load(new FileInputStream(propertiesFile));
			if (prop.getProperty("player1") != null) {
				player1 = prop.getProperty("player1");
			}
			if (prop.getProperty("player2") != null) {
				player2 = prop.getProperty("player2");
			}
			if (prop.getProperty("mapName") != null) {
				mapName = prop.getProperty("mapName");
			}
			if (prop.getProperty("useMoveList") != null) {
				useMoveList = Boolean.parseBoolean(prop.getProperty("useMoveList"));
			}
		} catch (IOException ex) {
			System.err.println("Could not read " + propertiesFile);
		}
		
		if (player1 == null) {
			player1 = "za.co.entelect.competition.bots.Random";
		}
		if (player2 == null) {
			player2 = "za.co.entelect.competition.bots.Random";
		}
		if (mapName == null) {
			mapName = "mapE0.txt";
		}
		
		System.out.println("player1: " + player1);
		System.out.println("player2: " + player2);
		System.out.println("mapName: " + mapName);
		System.out.println();
		
		mainGUI.game = new Game(
							//"za.co.entelect.competition.bots.Random",
							//"za.co.entelect.competition.bots.Minimax",
							//"za.co.entelect.competition.bots.MinimaxFixedDepth4",
							//"za.co.entelect.competition.bots.Endgame",
							//"za.co.entelect.competition.bots.Random",
							//"za.co.entelect.competition.bots.Brute",
							//"za.co.entelect.competition.bots.BruteV2",
							//"za.co.entelect.competition.bots.MCTS",
							//"za.co.entelect.competition.bots.DoNothing",
							//"za.co.entelect.competition.bots.Random",
							player1,
							player2,
							//"map.txt",
							//"mapE8.txt"
							mapName
							//"mapBattle0.txt",
							//gameState,
							//gameList,
							);
		//mainGUI.game.getGameState().setRules(GameState.RULES_TOTAL_DESTRUCTION);
		//GameState.maxTurns = 200;
		
//		gameState.getTanks()[1].setAlive(false);
//		gameState.getTanks()[2].setAlive(false);
//		gameState.getTanks()[3].setAlive(false);
		mainGUI.game.getGameState().setDebugMode(true);
		mainGUI.game.getGameState().setMapType();

		mainGUI.id.preferredSize = new Point(mainGUI.game.getGameState().getMap()[0].length, mainGUI.game.getGameState().getMap().length);
		f.pack();
		f.setVisible(true);
		
		if (useMoveList) {
			mainGUI.moveList = loadMoveList();
		} else {
			mainGUI.moveList = null;
		}
		
		mainGUI.setPreviousGameState(mainGUI.game.getGameState().clone());
		mainGUI.setCurrentGameState(mainGUI.game.getGameState().clone());
		mainGUI.repaint();
		
		if (GUI.absoluteTiming) {
			mainGUI.stateTimeNS = System.nanoTime();
			mainGUI.generateNextGameState();
		}

		
		while (true) {
			if (GUI.absoluteTiming) {
				synchronized(mainGUI.syncObject) {
				    try {
				        // Calling wait() will block this thread until another thread
				        // calls notify() on the object.
				        mainGUI.syncObject.wait();
				    } catch (InterruptedException e) {
				        // Happens if someone interrupts your thread.
				    	e.printStackTrace();
				    }
				}
				mainGUI.stateTimeNS = System.nanoTime();
				mainGUI.generateNextGameState();
			} else {
				//synchronized(id.syncObject) {
		    	//System.out.println("Engine notify.");
				//id.syncObject.notify();
				mainGUI.stateTimeNS = System.nanoTime();
				mainGUI.generateNextGameState();
				mainGUI.repaint();
				mainGUI.id.checkAndUpdateGameState();
			}
		}
		
	}
	 
	
	public void generateNextGameState() {
		//Util.print("Applet got timer update!");
		//System.out.println(game.getGameState().toString());	
		int[] overrideActions = new int[4];
		Arrays.fill(overrideActions, -1);

		for (int i = 0; i < 4; i++) {
			if (moveList != null && this.game.getGameState().getTickCount() < moveList[i].size()) {
				overrideActions[i] = moveList[i].get(this.game.getGameState().getTickCount());
			}		
		}

		int p1A = -1;
//		if (p1reqAction2 != null) {
//			p1A = p1reqAction2;
//		} else {
//			p1A = p1reqAction;
//		}
		Calendar last = null;
		int userAction = -1;
		for (int i = 0; i < this.userInput.length; i++) {			
			if (this.userInput[i].isPressed()) {
				if (i == 4) {
					//
					// If there is a Bullet for Tank 0 do not try to fire.
					//
					if (this.game.getGameState().getBullets()[0].isAlive()) {
						continue;
					}
				}
				if (last == null) {
					last = this.userInput[i].getTimePressed();
					userAction = this.userInput[i].getAction();
				} else if (this.userInput[i].getTimePressed().after(last)) {
					last = this.userInput[i].getTimePressed();
					userAction = this.userInput[i].getAction();
				}
			}
		}
		p1A = userAction;
		if (p1A != -1) {
			overrideActions[0] = p1A;
		}
		
		int[] realActions = new int[4];
		Arrays.fill(realActions, -1);
		
		int timeLeft = (int) ((sleepTime*1000000 - (System.nanoTime() - stateTimeNS)) * 0.8 / 1000000);
		this.game.nextTick(overrideActions, realActions, timeLeft);

		if (this.game.getGameState().getStatus() == GameState.STATUS_ACTIVE) {
			try {
				for (int i = 0; i < realActions.length; i++) {
					fileWriter.write(GameAction.toString(realActions[i]));
				}
				fileWriter.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		//long timeLeft = (sleepTime*1000000 - (System.nanoTime() - stateTimeNS))/1000000 - 100;
//		if (timeLeft > 0) {
//			try {
//				Thread.sleep(timeLeft);
//			} catch (InterruptedException e1) {
//				e1.printStackTrace();
//			}	
//		}
		if (!GUI.absoluteTiming) {			
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		id.setNextGameState(this.game.getGameState());//, updateDelay);
		timeLeft = (int) ((sleepTime*1000000 - (System.nanoTime() - stateTimeNS))/1000000);
		
		if (this.game.getGameState().getStatus() != GameState.STATUS_ACTIVE) {
			System.out.println(this.game.getGameState().getStatusString());
			if (this.game.getGameState().getStatus() != GameState.STATUS_PLAYER1_WINS) {
				System.out.print("");
			}
		}
		System.out.println("Current H: " + this.game.getGameState().getHeuristicValue(GameState.H_MINIMAX));
		//System.out.println("timeLeft: "+timeLeft);
		//Util.print("NextGameState Generated successfullly");
	}



	public GUI () {
		syncObject = new Object();
		id = new ImageDrawingComponent(frameTime, syncObject);
        addKeyListener(new TAdapter());
        //p1reqAction = new GameAction(GameAction.NONE, GameAction.GUI_NORTH);
        this.userInput = new UserInput[5];
       
        this.userInput[0] = new UserInput(Calendar.getInstance(), GameAction.ACTION_MOVE_GUI_NORTH, false);
        this.userInput[1] = new UserInput(Calendar.getInstance(), GameAction.ACTION_MOVE_EAST, false);
        this.userInput[2] = new UserInput(Calendar.getInstance(), GameAction.ACTION_MOVE_GUI_SOUTH, false);
        this.userInput[3] = new UserInput(Calendar.getInstance(), GameAction.ACTION_MOVE_WEST, false);
        this.userInput[4] = new UserInput(Calendar.getInstance(), GameAction.ACTION_FIRE, false);
        

		setFocusable(true);
        
        File currentMoveList = new File("movelists\\" + Util.date.format(Calendar.getInstance().getTime()) + ".txt");
        try {
			fileWriter = new BufferedWriter(new FileWriter(currentMoveList));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setPreviousGameState(GameState gameState) {
		id.setPreviousGameState(gameState);
	}
	public void setCurrentGameState(GameState gameState) {
		id.setCurrentGameState(gameState);
	}
	
	public void repaint() {
		id.repaint();
	}


	public void init() {
		add("Center", id);

		b1 = new JButton("Start Game!");
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		//b1.setMnemonic(KeyEvent.VK_D);
		b1.setActionCommand("doTask");
		add("South", b1);
		b1.setFocusable(false);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				//System.out.println("doTask");
				if (GUI.absoluteTiming) {
					id.startTimer();
				}
			};
		});

	}

	public static ArrayList<Integer>[] loadMoveList() {
		Scanner in = null;
		try {
			in = new Scanner(new File(".\\assets\\movelist.txt"));
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("Movelist not found!");
			return null;
		}
		
		ArrayList<String> file = new ArrayList<String>(); 
		while (in.hasNext()) {
			String line = in.nextLine();
			file.add(line);
		}
		in.close();
		
		boolean rowFormat = false;
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] moves = new ArrayList[4]; 
		for (int i = 0; i < moves.length; i++) {
			moves[i] = new ArrayList<Integer>();
		}
		
		if (rowFormat) {		
			for (int row = 0; row < file.size(); row++) {
				if (file.get(row).charAt(0) == 'X') {
					continue;
				}
				StringTokenizer st = new StringTokenizer(file.get(row), ",");
				while(st.hasMoreTokens()) {
					String token = st.nextToken();
					int action = 0;
					switch (token.charAt(1)) {
						case '^': 	action = GameAction.ACTION_MOVE_GUI_NORTH;	break;
						case '>': 	action = GameAction.ACTION_MOVE_EAST;		break;
						case 'v': 	action = GameAction.ACTION_MOVE_GUI_SOUTH;	break;
						case '<': 	action = GameAction.ACTION_MOVE_WEST;		break;
						case 'F': 	action = GameAction.ACTION_FIRE;			break;
						case '.': 	action = GameAction.ACTION_NONE;			break;
						default:	action = -1;
					}
					int newAction = action;
					moves[row].add(newAction);
				}
			}
		} else {
			HashSet<Integer> skippedColumns = new HashSet<Integer>();
			for (int row = 0; row < file.size(); row++) {
				StringTokenizer st = new StringTokenizer(file.get(row), " ");
				int column = 0;
				while(st.hasMoreTokens()) {
					String token = st.nextToken();
					if (skippedColumns.contains(column)) {
						column++;
						continue;
					}
					int action = 0;
					switch (token.charAt(0)) {
						case '^': 	action = GameAction.ACTION_MOVE_GUI_NORTH;	break;
						case '>': 	action = GameAction.ACTION_MOVE_EAST;		break;
						case 'v': 	action = GameAction.ACTION_MOVE_GUI_SOUTH;	break;
						case '<': 	action = GameAction.ACTION_MOVE_WEST;		break;
						case 'F': 	action = GameAction.ACTION_FIRE;			break;
						case '.': 	action = GameAction.ACTION_NONE;			break;
						case 'X':
							skippedColumns.add(column++);
							continue;
						default:	action = -1;
					}
					int newAction = action;
					moves[column++].add(newAction);
				}
			}
		}
		return moves;
	}
	
	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			//System.out.println("keyPressed");
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_UP)
			{
				userInput[0].setPressed(true);
				userInput[0].setTimePressed(Calendar.getInstance());
			}
			else if (key == KeyEvent.VK_RIGHT)
			{
				userInput[1].setPressed(true);
				userInput[1].setTimePressed(Calendar.getInstance());
			}
			else if (key == KeyEvent.VK_DOWN)
			{
				userInput[2].setPressed(true);
				userInput[2].setTimePressed(Calendar.getInstance());
			}
			else if (key == KeyEvent.VK_LEFT)
			{
				userInput[3].setPressed(true);
				userInput[3].setTimePressed(Calendar.getInstance());
			}
			else if (key == KeyEvent.VK_SPACE)
			{
				userInput[4].setPressed(true);
				userInput[4].setTimePressed(Calendar.getInstance());
			}
			
			//System.out.println("p1reqAction: "+p1reqAction);
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_UP)
			{
				userInput[0].setPressed(false);
			}
			else if (key == KeyEvent.VK_RIGHT)
			{
				userInput[1].setPressed(false);
			}
			else if (key == KeyEvent.VK_DOWN)
			{
				userInput[2].setPressed(false);
			}
			else if (key == KeyEvent.VK_LEFT)
			{
				userInput[3].setPressed(false);
			}
			else if (key == KeyEvent.VK_SPACE)
			{
				userInput[4].setPressed(false);
			}
		}
	}
}