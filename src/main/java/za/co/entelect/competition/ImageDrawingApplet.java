package za.co.entelect.competition;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.*;
import javax.swing.*;



class Scale {
	private double scale;
	public Scale(double scale) {
		super();
		this.scale = scale;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
}

class GameAction {
	final static int NORTH = 0;
	final static int EAST = 1;
	final static int SOUTH = 2;
	final static int WEST = 3;
	
	final static int MOVE = 10;
	final static int FIRE = 11;
	final static int NONE = 12;
	
	public final int type;
	public final int direction;
	
	public GameAction(int type, int direction) {
		super();
		this.type = type;
		this.direction = direction;
	}	
}

abstract class Unit {
	protected Point position;
	protected int rotation;
	protected boolean alive;
	
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
	public Collision(Point position, int rotation, boolean isAlive) {
		super(position, rotation, isAlive);
	}
	public Collision clone() {
		return new Collision(new Point(this.position), this.rotation, this.alive);
	}
}

class GameState {
	private boolean[][] map;
	private ArrayList<Bullet> bullets;
	private ArrayList<Collision> collisions;
	private Tank[] tanks;
	private Base[] bases;
	public GameState(boolean[][] map, ArrayList<Bullet> bullets, Tank[] tanks, Base[] bases, ArrayList<Collision> collisions) {
		super();
		this.map = map;
		this.bullets = bullets;
		this.collisions = collisions;
		this.tanks = tanks;
		this.bases = bases;
	}
	public boolean[][] getMap() {
		return map;
	}
	public void setMap(boolean[][] map) {
		this.map = map;
	}
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}
	public Tank[] getTanks() {
		return tanks;
	}
	public void setTanks(Tank[] tanks) {
		this.tanks = tanks;
	}	
	public Base[] getBases() {
		return bases;
	}
	public void setBases(Base[] bases) {
		this.bases = bases;
	}
	
	public ArrayList<Collision> getCollisions() {
		return collisions;
	}
	public void setCollisions(ArrayList<Collision> collisions) {
		this.collisions = collisions;
	}
	public GameState clone() {
		boolean[][] newMap = new boolean[this.map.length][this.map[0].length];
		for (int y = 0; y < newMap.length; y++) {
			newMap[y] = Arrays.copyOf(this.map[y], this.map[0].length);
		}
		
		ArrayList<Bullet> newBullets = new ArrayList<Bullet>();
		for (Bullet bullet : this.bullets) {
			newBullets.add(bullet.clone());
		}
		
		ArrayList<Collision> newCollisions = new ArrayList<Collision>();
		for (Collision collision : this.collisions) {
			newCollisions.add(collision.clone());
		}
		
		Tank[] newTanks = new Tank[this.tanks.length];
		for (int i = 0; i < this.tanks.length; i++) {
			newTanks[i] = tanks[i].clone();
		}
		
		Base[] newBases = new Base[this.bases.length];
		for (int i = 0; i < this.bases.length; i++) {
			newBases[i] = bases[i].clone();
		}
		
		return new GameState(newMap, newBullets, newTanks, newBases, newCollisions);
	}
	public ArrayList<Point> getMoves(Point p) {
		ArrayList<Point> moves = new ArrayList<Point>();
		Point newP;
		newP = new Point(p.x, p.y+1);
		if (newP.y < this.map.length && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		newP = new Point(p.x+1, p.y);
		if (newP.x < this.map[0].length && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		newP = new Point(p.x, p.y-1);
		if (newP.y >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		newP = new Point(p.x-1, p.y);
		if (newP.x >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(newP);
		}
		return moves;
	}
	public ArrayList<Integer> getMovesDirection(Point p) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		Point newP;
		newP = new Point(p.x, p.y+1);
		if (newP.y < this.map.length && this.map[newP.y][newP.x] == false) {
			moves.add(0);
		}
		newP = new Point(p.x+1, p.y);
		if (newP.x < this.map[0].length && this.map[newP.y][newP.x] == false) {
			moves.add(1);
		}
		newP = new Point(p.x, p.y-1);
		if (newP.y >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(2);
		}
		newP = new Point(p.x-1, p.y);
		if (newP.x >= 0 && this.map[newP.y][newP.x] == false) {
			moves.add(3);
		}
		return moves;
	}
	public boolean canMove(Point oldP, Point newP) {
		if (isInMap(newP)
				&& this.map[newP.y][newP.x] == false
				&& (isInMap(newP) == true)
				&& (Util.mDist(oldP, newP) == 1)) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isInMap(Point p) {
		if ((p.x < this.map[0].length)
				&& (p.x >= 0)
				&& (p.y < this.map.length)
				&& (p.y >= 0)) {
			return true;
		} else {
			return false;
		}
	}
	public void nextTick() {
		// Clear all collisions, they last only one tick
		collisions.clear();
		
		// Update Tank positions
		Tank t;
		for (int i = 0; i < this.tanks.length; i++) {
			t = this.tanks[i];
			if (!t.isAlive()) {
				continue;
			}
			if (!t.hasNextAction()) {
				System.err.println("TANK[" + i + "] has no action set, doing NONE");
				t.setNextAction(new GameAction(GameAction.NONE, 0));
			}
			if (t.getNextAction().type < GameAction.MOVE || t.getNextAction().type > GameAction.NONE) {
				System.err.println("TANK[" + i + "] has invalid action set, doing NONE");
				t.setNextAction(new GameAction(GameAction.NONE, 0));
			}
			if (t.getNextAction().type != GameAction.NONE) {
				if (t.getNextAction().type == GameAction.MOVE) {
					Point nextP = Util.movePoint(t.getPosition(), t.getNextAction().direction);
					if (canMove(t.getPosition(), nextP)) {
						t.setPosition(nextP);
						t.setRotation(t.getNextAction().direction);
					}
				} else if (t.getNextAction().type == GameAction.FIRE) {
					bullets.add(new Bullet(new Point(t.getPosition()), t.getNextAction().direction));
				}
				t.clearNextAction();
			}
		}
		
		// Update Bullet positions
		Bullet b;
		for (int i = 0; i < bullets.size(); i++) {
			b = bullets.get(i);
			Point nextP = Util.movePoint(b.getPosition(), b.getRotation());
			if (isInMap(nextP)) {
				b.setPosition(nextP);
			} else {
				// Bullet is outside map and will cause collision
				bullets.remove(i--);
				collisions.add(new Collision(new Point(b.getPosition()), 0));
			}
		}
		
		// Now all positions are updated, create list of all units and check for collisions
		HashMap<Point, Unit> hitmap = new HashMap<Point, Unit>();
		Unit me;
		for (int i = 0; i < bases.length; i++) {
			me = bases[i];
			if (hitmap.containsKey(me.position)) {
				Unit obstacle = hitmap.get(me.position);				
				me.setAlive(false);
				obstacle.setAlive(false);				
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else {
				hitmap.put(me.position, me);
			}
		}
		for (int i = 0; i < tanks.length; i++) {
			me = tanks[i];
			if (hitmap.containsKey(me.position)) {
				Unit obstacle = hitmap.get(me.position);				
				me.setAlive(false);
				obstacle.setAlive(false);				
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else {
				hitmap.put(me.position, me);
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			me = bullets.get(i);
			if (hitmap.containsKey(me.position)) {
				Unit obstacle = hitmap.get(me.position);				
				me.setAlive(false);
				obstacle.setAlive(false);				
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else if (this.map[me.getPosition().y][me.getPosition().x] == true) {				
				me.setAlive(false);
				this.map[me.getPosition().y][me.getPosition().x] = false;
				collisions.add(new Collision(new Point(me.getPosition()), 0));
			} else {
				hitmap.put(me.position, me);
			}
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			me = bullets.get(i);
			if (!me.isAlive()) {
				bullets.remove(i--);
			}
		}
		
		// End-game conditions
		for (int i = 0; i < bases.length; i++) {
			me = bases[i];
			if(me.isAlive() == false) {
				System.out.println("GAME OVER, player " + ((i+1)%bases.length) + " wins!");				
			}
		}
	}
}

//class EImage {
//	private BufferedImage image;
//	private int x;
//	private int y;
//	private int rotation;
//	private Scale scale;
//	private int currentFrame;
//	private int numFrames;
//	private File path;
//	private String filename;
//	
//	public EImage(BufferedImage image, int x, int y, int rotation, Scale scale,
//			int currentFrame, int numFrames, File path) {
//		super();
//		this.image = image;
//		this.x = x;
//		this.y = y;
//		this.rotation = rotation;
//		this.scale = scale;
//		this.currentFrame = currentFrame;
//		this.numFrames = numFrames;
//		this.path = path;
//	}
//}

class ImageDrawingComponent extends Component {

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
	private int blockSize = 16;
	private int timercount = 0;
	private GameState previousGameState = null;
	private GameState currentGameState = null;
	private double drawScale = 2;

	public ImageDrawingComponent() {
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

	public void updateGameState(GameState newGameState) {
		if (this.currentGameState != null) {
			this.previousGameState = this.currentGameState.clone();
		}
		this.currentGameState = newGameState.clone();
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
		return new Dimension((int)this.drawScale*16*13, (int)this.drawScale*16*13);
	}
	
	private void drawTank(Graphics g, BufferedImage img, int idx, GameState prevGS,	GameState currGS, 
			double drawScale, int numFrames) {
		if (currGS.getTanks()[idx].isAlive()) {
			Point internalOffset = new Point(0,0);
			if (currGS.getTanks()[idx].getRotation() == 2) {
				internalOffset.translate(1, 0);
			}
			int frame = 0;
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
		
		if (rotation != 0) {		
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
	}

	/* In this example the image is recalculated on the fly every time
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
				if (currGS.getMap()[y][x] == true) {
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
		for (int i = 0; i < currGS.getBullets().size(); i++) {
			Bullet b = currGS.getBullets().get(i);
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
}

public class ImageDrawingApplet extends JApplet {
	
	private static final long serialVersionUID = 5023415827458014290L;
	JButton b1;
	final ImageDrawingComponent id;
	final int frames = 16;
	int fps = 16;
	int updateDelay;
	Timer timer;

	public ImageDrawingApplet () {
		id = new ImageDrawingComponent();
	}
	
	public int getUpdateDelay() {
		return updateDelay;
	}
	public void setUpdateDelay(int updateDelay) {
		this.updateDelay = updateDelay;
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
	


	public void updateGameState(GameState currentGameState, int updateDelay) {
		Util.print(" 1. updateGameState()");
		id.setTimercount(-1);
		id.updateGameState(currentGameState);
		setUpdateDelay(updateDelay);
		if (timer != null) {
			//System.out.println("timer.isRunning(): "+timer.isRunning());
			while (timer.isRunning()) {
//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
				//System.out.println("waiting for timer");
			} 
		}
		final long currentNano = System.nanoTime();
	    timer = new Timer(this.updateDelay/fps/2,new ActionListener(){
	        @Override
	        public void actionPerformed(ActionEvent e){
//	           if (id.getTimercount() >= frames-1) {
//	            	((Timer)e.getSource()).stop();
//	            	id.setTimercount(0);
//	        		System.out.println(getMilliTime()+" 4. Timer stopped after "+(System.nanoTime()-currentNano)/1000000);
//	            } else {
	            	id.setTimercount(id.getTimercount()+1);
	            	id.repaint();
		            //System.out.println("id.repaint("+id.getTimercount()+")");

	 	           if (id.getTimercount() >= frames-1) {
	 	            	((Timer)e.getSource()).stop();
	 	            	//id.setTimercount(0);
	 	            	Util.print(" 4. Timer stopped after "+(System.nanoTime()-currentNano)/1000000);
	 	            //} else {
	            }
	        }
	    });
    	timer.start();
    	Util.print(" 2. Start new timer");
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
				
				
//				final int frames = 16;
//				int fps = 16;
//		        Timer timer = new Timer(1000/fps,new ActionListener(){
//		            @Override
//		            public void actionPerformed(ActionEvent e){
//		            	id.setTimercount(id.getTimercount()+1);
//			            //id.setTank1Ax(id.getTank1Ax() + 1/(double)frames);
//			            id.repaint();
//			            if (id.getTimercount() == frames) {
//			            	((Timer)e.getSource()).stop();
//			            	id.setTimercount(0);
//			            }
//		            }
//		        });
//	        	timer.start();
	        	
	        	
				
				
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
		int updateDelay = 1000;
		ImageDrawingApplet id = new ImageDrawingApplet();
		id.init();
		f.add("Center", id);
		f.pack();
		f.setVisible(true);
		GameState gameState = newGame();
		//gameState.getTanks()[1].setAlive(false);
		//gameState.getTanks()[2].setAlive(false);
		//gameState.getTanks()[3].setAlive(false);
		
		id.setPreviousGameState(gameState.clone());
		id.setCurrentGameState(gameState.clone());
		id.repaint();
		
		//id.updateGameState(gameState, updateDelay);
		Random rand = new Random();
		
		
		while(true) {
			//get moves
			try {
				Thread.sleep(updateDelay);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			//System.out.println("Hello");
			for (int i = 0; i < 4; i++) {
				int action = rand.nextInt(3)+10;
				int direction = rand.nextInt(4);
				if (action == GameAction.MOVE) {
					ArrayList<Integer> moves = gameState.getMovesDirection(gameState.getTanks()[i].getPosition());
					direction = moves.get(rand.nextInt(moves.size()));
				}
				
				gameState.getTanks()[i].setNextAction(new GameAction(action, direction));
				
			}
			gameState.nextTick();
			id.updateGameState(gameState, updateDelay);
			Util.print(" 3. Completed update");
		}
	}
	
	public static GameState newGame() {
		Scanner in = null;
		try {
			in = new Scanner(new File(".\\assets\\map.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> file = new ArrayList<String>(); 
		while (in.hasNext()) {
			String line = in.nextLine();
			file.add(line);
		}
		in.close();
		
		boolean[][] map = new boolean[file.size()][file.get(0).length()];
		Tank[] tanks = new Tank[4];
		Base[] bases = new Base[2];
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		ArrayList<Collision> collisions = new ArrayList<Collision>();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				map[y][x] = false;
				if (file.get(y).charAt(x) == '1') {
					map[y][x] = true;
				} else if (file.get(y).charAt(x) == 'A') {
					tanks[0] = new Tank(new Point(x,y), 0, null);
				} else if (file.get(y).charAt(x) == 'B') {
					tanks[1] = new Tank(new Point(x,y), 0, null);
				} else if (file.get(y).charAt(x) == 'X') {
					tanks[2] = new Tank(new Point(x,y), 0, null);
				} else if (file.get(y).charAt(x) == 'Y') {
					tanks[3] = new Tank(new Point(x,y), 0, null);
				} else if (file.get(y).charAt(x) == 'C') {
					bases[0] = new Base(new Point(x,y), 0);
				} else if (file.get(y).charAt(x) == 'Z') {
					bases[1] = new Base(new Point(x,y), 0);
				} else if (file.get(y).charAt(x) == '*') {
					bullets.add(new Bullet(new Point(x,y), 0));
				}
			}
		}
		GameState newGame = new GameState(map, bullets, tanks, bases, collisions);
		return newGame;
	}
}