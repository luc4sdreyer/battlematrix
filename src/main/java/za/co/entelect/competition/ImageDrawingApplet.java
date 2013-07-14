package za.co.entelect.competition;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.*;
import javax.swing.*;


abstract class Unit {
	protected Point position;
	protected int rotation;
	protected boolean alive;
	
	public final static int EMPTY            = 0;
	public final static int WALL             = 1;
	public final static int TANK1A           = 2;
	public final static int TANK1B           = 3;
	public final static int TANK2A           = 4;
	public final static int TANK2B           = 5;
	public final static int BULLET_TANK1A    = 6;
	public final static int BULLET_TANK1B    = 7;
	public final static int BULLET_TANK2A    = 8;
	public final static int BULLET_TANK2B    = 9;
	public final static int BASE1            = 10;
	public final static int BASE2            = 11;
	
	public Unit(Point position, int rotation) {
		super();
		this.position = position;
		this.rotation = rotation;
		this.alive = true;
	}
	public Unit(Point position, int rotation, boolean alive) {
		super();
		this.position = position;
		this.rotation = rotation;
		this.alive = alive;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public abstract Unit clone();
	public static boolean isTankOrEmpty(int unitCode) {
		if (unitCode == Unit.EMPTY || (unitCode >= Unit.TANK1A && unitCode <= Unit.TANK2B)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isBulletOrEmpty(int unitCode) {
		if (unitCode == Unit.EMPTY || (unitCode >= Unit.BULLET_TANK1A && unitCode <= Unit.BULLET_TANK2B)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isBase(int unitCode) {
		if (unitCode == Unit.BASE1 || unitCode == Unit.BASE2) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isBullet(int unitCode) {
		if (unitCode >= Unit.BULLET_TANK1A && unitCode <= Unit.BULLET_TANK2B) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isTank(int unitCode) {
		if (unitCode >= Unit.TANK1A && unitCode <= Unit.TANK2B) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isEmptyOrWall(int unitCode) {
		if (unitCode == Unit.EMPTY || unitCode == Unit.WALL) {
			return true;
		} else {
			return false;
		}
	}
}

class Bullet extends Unit {
	public Bullet(Point position, int rotation) {
		super(position, rotation);
	}
	public Bullet(Point position, int rotation, boolean alive) {
		super(position, rotation, alive);
	}
	public Bullet clone() {
		return new Bullet(this.position, this.rotation, this.alive);
	}
}


class Tank extends Unit{
	private GameAction nextAction;
	public Tank(Point position, int rotation, GameAction nextAction) {
		super(position, rotation);
		this.nextAction = nextAction;
	}
	public Tank(Point position, int rotation, GameAction nextAction, boolean alive) {
		super(position, rotation, alive);
		this.nextAction = nextAction;
	}
	public Tank clone() {
		return new Tank(this.position, this.rotation, this.nextAction, this.alive);
	}
	public GameAction getNextAction() {
		return nextAction;
	}
	public void setNextAction(GameAction nextAction) {
		this.nextAction = nextAction;
	}
	public void clearNextAction() {
		this.nextAction = null;
	}
	public boolean hasNextAction() {
		if (this.nextAction != null) {
			return true;
		} else {
			return false;	
		}
	}
}

class Base extends Unit {
	public Base(Point position, int rotation) {
		super(position, rotation);
	}
	public Base(Point position, int rotation, boolean alive) {
		super(position, rotation, alive);
	}
	public Base clone() {
		return new Base(this.position, this.rotation, this.alive);
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

	private BufferedImage tank1A;
	private BufferedImage tank1B;
	private BufferedImage base1;
	private BufferedImage tank2A;
	private BufferedImage tank2B;
	private BufferedImage base2;
	private BufferedImage wall;
	private BufferedImage bullet;
	private BufferedImage empty;
	private BufferedImage collision;
	private int blockSize = 3;
	private int timercount = 0;
	private GameState previousGameState = null;
	private GameState currentGameState = null;
	private GameState nextGameState = null;
	private double drawScale = 3;
    private Timer timer;
    private final int DELAY;
    private Object syncObject;

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
        timer.start();
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
	
	private BufferedImage initImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
			if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
				BufferedImage bi2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics big = bi2.getGraphics();
				big.drawImage(image, 0, 0, null);
				image = bi2;
			}
		} catch (IOException e) {
			System.out.println("Image could not be read");
			//            System.exit(1);
		}
		return image;	
	}

	public Dimension getPreferredSize() {
		return new Dimension((int)this.drawScale*this.blockSize*13*5, (int)this.drawScale*this.blockSize*13*5);
	}
	
	private void drawTank(Graphics g, BufferedImage img, int idx, GameState prevGS,	GameState currGS, 
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

	private void drawGrid(Graphics g, BufferedImage img, Point previousPos, Point currentPos, Point internalOffset,
			int rotation, double scale, int frame, int numFrames) {
		int x = ((currentPos.x - previousPos.x) * (this.timercount+1)) + (previousPos.x * this.blockSize); // + internalOffset.x;
		int y = ((currentPos.y - previousPos.y) * (this.timercount+1)) + (previousPos.y * this.blockSize); // + internalOffset.y;
		if (numFrames == 2) {
			//System.out.println("Current position\tx:"+x+"\ty:"+y+"\ttimercount:"+this.timercount+"\trotation:"+rotation);
			//if (this.timercount == 0) {
			//	int ab = 0; ab = (ab == 0) ? 1 : 2;
			//}
			
		}
		drawGrid(g, img, x, y, rotation, scale, frame, numFrames);
	}	
	
	private void drawGrid(Graphics g, BufferedImage img, int pixelX, int pixelY,
			int rotation, double scale, int frame, int numFrames) {	
		Graphics2D g2 = (Graphics2D) g;	
		
		if (numFrames != 1) {
			if (img.getWidth() == 128) {
				//System.out.println("x: "+(img.getWidth()*frame/numFrames)+"\tw: "+img.getWidth()/numFrames+"\tframe: "+frame);
			}
			img = img.getSubimage((img.getWidth()*frame/numFrames), 0, img.getWidth()/numFrames, img.getHeight());
		}
		
		if (rotation == 0 || rotation == 2) {
			rotation = (rotation + 2) % 4;
		} 
		
		if ((img.getHeight() != 3) && (rotation != 0)) {		
			double rotationRequired = rotation*Math.PI/2;
			double locationX = img.getWidth() / 2;
			double locationY = img.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);		
			img = op.filter(img, null);
		}
		
		if (scale != 1) {
			AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			img = op.filter(img, null);
		}
		
		
		g2.drawImage(img, (int)Math.round(pixelX*scale), (int)Math.round(pixelY*scale), null);
		if (numFrames == 2) {
			//int thickness = 1;
			//Stroke oldStroke = g2.getStroke();
			g2.setColor(Color.WHITE);
			//g2.setStroke(new BasicStroke(thickness));
			g2.drawRect((int)Math.round(pixelX*scale), (int)Math.round(pixelY*scale), img.getWidth(), img.getHeight());
			//g2.setStroke(oldStroke);
		}
	}

	/** In this example the image is recalculated on the fly every time
	 * This makes sense where repaints are infrequent or will use a
	 * different filter/op from the last.
	 * In other cases it may make sense to "cache" the results of the
	 * operation so that unless 'opIndex' changes, drawing is always a
	 * simple copy.
	 * In such a case create the cached image and directly apply the filter
	 * to it and retain the resulting image to be repainted.
	 * The resulting image if untouched and unchanged Java 2D may potentially
	 * use hardware features to accelerate the blit.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		//System.out.println("Repaint: "+timercount);
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
			//System.out.println("timercount: "+timercount+"\ttimercount/2:"+timercount/2);
		}
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if (inGame) {
//            checkApple();
//            checkCollision();
//            move();
//        }
		//System.out.print(". ");
		this.timercount++;
		if (timercount >= 3) {
			// Time to update the gameState 
			timercount = 0;

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
			//System.out.println("previousGameState: "+this.previousGameState);
			//System.out.println("currentGameState: "+this.currentGameState);
			//System.out.println("nextGameState: "+this.nextGameState);
			
			synchronized(syncObject) {
			    syncObject.notify();
			}
			
		}
        repaint();
	}
}

public class ImageDrawingApplet extends JApplet {
	
	private static final long serialVersionUID = 5023415827458014290L;
	JButton b1;
	final ImageDrawingComponent id;
	final int frames = 3;
	//int fps = 3;
	int DELAY = 100/frames;
	long sleepTime = (long)(DELAY*frames*1);
	boolean nextTick = false;
	long stateTimeNS = 0;
	Object syncObject;							// syncObject is used to sync the animation and logic threads.

	ArrayList<GameAction>[] moveList;
    private GameAction p1reqAction;
    private GameAction p1reqAction2;
	
	//static GameState gameState;
    Game game;

	public ImageDrawingApplet () {
		syncObject = new Object();
		id = new ImageDrawingComponent(DELAY, syncObject);
        addKeyListener(new TAdapter());
        p1reqAction = new GameAction(GameAction.NONE, GameAction.GUI_NORTH);

        setFocusable(true);
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

		b1 = new JButton("Disable middle button");
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		//b1.setMnemonic(KeyEvent.VK_D);
		b1.setActionCommand("doTask");
		add("South", b1);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				System.out.println("doTask");            
			};
		});

	}

	public static void main(String s[]) {
		JFrame f = new JFrame("ImageDrawing");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		
		ImageDrawingApplet id = new ImageDrawingApplet();
		id.init();
		f.add("Center", id);
		f.pack();
		f.setVisible(true);
		id.game = new Game(	"za.co.entelect.competition.bots.Random",
							"za.co.entelect.competition.bots.Random",
							"map.txt");
//		gameState.getTanks()[1].setAlive(false);
//		gameState.getTanks()[2].setAlive(false);
//		gameState.getTanks()[3].setAlive(false);
		
		id.moveList = loadMoveList();
		
		id.setPreviousGameState(id.game.getGameState().clone());
		id.setCurrentGameState(id.game.getGameState().clone());
		id.repaint();
		id.stateTimeNS = System.nanoTime();
		id.generateNextGameState();

		
		while (true) {
			synchronized(id.syncObject) {
			    try {
			        // Calling wait() will block this thread until another thread
			        // calls notify() on the object.
			        id.syncObject.wait();
			    } catch (InterruptedException e) {
			        // Happens if someone interrupts your thread.
			    	e.printStackTrace();
			    }
			}
			id.stateTimeNS = System.nanoTime();
			id.generateNextGameState();
		}
		
	}
	
	public void generateNextGameState() {
		//Util.print("Applet got timer update!");
		//System.out.println(gameState.toString());		
		GameAction[] overrideActions = new GameAction[4];

		for (int i = 0; i < 4; i++) {
			if (moveList != null && this.game.getGameState().getTickCount() < moveList[i].size()) {
				overrideActions[i] = moveList[i].get(this.game.getGameState().getTickCount());
			}		
		}

		GameAction p1A = null;
		if (p1reqAction2 != null) {
			p1A = p1reqAction2;
		} else {
			p1A = p1reqAction;
		}
		if (p1A != null) {
			overrideActions[0] = p1A;
		}
		
		this.game.nextTick(overrideActions);

		long timeLeft = (sleepTime*1000000 - (System.nanoTime() - stateTimeNS))/1000000 - 100;
		if (timeLeft > 0) {
			try {
				Thread.sleep(timeLeft);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}	
		}
		id.setNextGameState(this.game.getGameState());//, updateDelay);
		timeLeft = (sleepTime*1000000 - (System.nanoTime() - stateTimeNS))/1000000;
		
		if (this.game.getGameState().getStatus() != GameState.STATUS_ACTIVE) {
			System.out.println(this.game.getGameState().getStatusString());
		}
		//System.out.println("timeLeft: "+timeLeft);
		//Util.print("NextGameState Generated successfullly");
	}

	

	public static ArrayList<GameAction>[] loadMoveList() {
		Scanner in = null;
		try {
			in = new Scanner(new File(".\\assets\\movelist.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> file = new ArrayList<String>(); 
		while (in.hasNext()) {
			String line = in.nextLine();
			file.add(line);
		}
		in.close();
		
		if (file.get(0).charAt(0) == 'X') {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<GameAction>[] moves = new ArrayList[4]; 
		for (int row = 0; row < file.size(); row++) {
			moves[row] = new ArrayList<GameAction>();
			StringTokenizer st = new StringTokenizer(file.get(row), ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				int type = 0;
				int direction = 0;
				switch (token.charAt(0)) {
					case 'm': 	type = GameAction.MOVE;		break;
					case 'f': 	type = GameAction.FIRE;		break;
					case 'n': 	type = GameAction.NONE;		break;
					default:	type = -1;
				}
				switch (token.charAt(1)) {
					case '0': 	direction = GameAction.GUI_NORTH;	break;
					case '1': 	direction = GameAction.EAST;		break;
					case '2': 	direction = GameAction.GUI_SOUTH;	break;
					case '3': 	direction = GameAction.WEST;		break;
					default:	direction = -1;
				}
				GameAction newAction = new GameAction(type, direction);
				moves[row].add(newAction);
			}
		}
		return moves;
	}
	
	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			//System.out.println("keyPressed");
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT)
			{
				p1reqAction = new GameAction(GameAction.MOVE, GameAction.WEST);
			}
			else if (key == KeyEvent.VK_RIGHT)
			{
				p1reqAction = new GameAction(GameAction.MOVE, GameAction.EAST);
			}
			else if (key == KeyEvent.VK_UP)
			{
				p1reqAction = new GameAction(GameAction.MOVE, GameAction.GUI_NORTH);
			}
			else if (key == KeyEvent.VK_DOWN)
			{
				p1reqAction = new GameAction(GameAction.MOVE, GameAction.GUI_SOUTH);
			}
			else if (key == KeyEvent.VK_SPACE)
			{
				p1reqAction2 = new GameAction(GameAction.FIRE, GameAction.GUI_SOUTH);
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || 
					key == KeyEvent.VK_UP ||  key == KeyEvent.VK_DOWN)
			{
				p1reqAction = new GameAction(GameAction.NONE, GameAction.GUI_SOUTH);
			}
			else if (key == KeyEvent.VK_SPACE)
			{
				p1reqAction2 = null;
			}
		}
	}
}