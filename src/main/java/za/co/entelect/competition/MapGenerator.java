package za.co.entelect.competition;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
	private static Random r = new Random(); 
	
	public static GameState generate(int minWidth, int maxWidth, int minHeight, int maxHeight, int blockSize, int numBlocks) {

		return null;
	}
	
	public static GameState generateRandom(int minWidth, int maxWidth, int minHeight, int maxHeight) {
		
		
		int[][] newMap = new int[r.nextInt(maxHeight - minHeight + 1) + minHeight][r.nextInt(maxWidth - minWidth + 1) + minWidth];
		for (int y = 0; y < newMap.length; y++) {
			//newMap[y] = Arrays.copyOf(this.map[y], this.map[0].length);
			for (int x = 0; x < newMap[0].length; x++) {
				if (r.nextInt(5) == 0) {
					newMap[y][x] = Unit.WALL;
				} else {
					newMap[y][x] = Unit.EMPTY;
				}				
			}
		}
		
		Tank[] newTanks = new Tank[4];
		Base[] newBases = new Base[2];
		Bullet[] newBullets = new Bullet[4];

		for (int i = 0; i < newTanks.length; i++) {
			newTanks[i] = new Tank(new Point(0,0), 2, null, false);
		}

		for (int i = 0; i < newTanks.length; i++) {
			Point p = null;
			boolean valid = false;
			while (!valid) {
				valid = true;
				p = new Point(r.nextInt(newMap[0].length - GameState.tankSize + 1), r.nextInt(newMap.length - GameState.tankSize + 1));
				Point[] corners = new Point[4];
				corners[0] = new Point(p.x, p.y);
				corners[1] = new Point(p.x + GameState.tankSize - 1, p.y);
				corners[2] = new Point(p.x, p.y + GameState.tankSize - 1);
				corners[3] = new Point(p.x + GameState.tankSize - 1, p.y + GameState.tankSize - 1);
				for (int j = 0; j < corners.length; j++) {
					if (GameState.getTank(corners[j], newTanks) != -1) {
						valid = false;
					}
				}				
			}
			boolean alive = true;
			if (r.nextInt(4) == 0) {
				alive = false;
			}
			newTanks[i] = new Tank(p, r.nextInt(4), null, true);
			
			Tank t = newTanks[i];
			if (!t.isAlive()) {
				continue;
			}
			for (int y2 = 0; y2 < GameState.tankSize; y2++) {
				for (int x2 = 0; x2 < GameState.tankSize; x2++) {
					newMap[t.getPosition().y+y2][t.getPosition().x+x2] = Unit.TANK1A+i;
				}
			}
		}
		
		for (int i = 0; i < newBases.length; i++) {
			Point p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
			while (p == null || newMap[p.y][p.x] != Unit.EMPTY) {
				p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
			}
			boolean alive = true;
			if (r.nextInt(4) == 0) {
				alive = false;
			}
			newBases[i] = new Base(p, r.nextInt(4), alive);
			Base b = newBases[i]; 
			if (!b.isAlive()) {
				continue;
			}
			newMap[b.getPosition().y][b.getPosition().x] = Unit.BASE1+i;
		}
		
		for (int i = 0; i < newBullets.length; i++) {
			Point p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
			while (p == null || newMap[p.y][p.x] != Unit.EMPTY) {
				p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
			}
			boolean alive = true;
			if (r.nextInt(4) == 0) {
				alive = false;
			}
			newBullets[i] = new Bullet(p, r.nextInt(4), alive);
			Bullet b = newBullets[i]; 
			if (!b.isAlive()) {
				continue;
			}
			newMap[b.getPosition().y][b.getPosition().x] = Unit.BULLET_TANK1A+i;
		}
		
		ArrayList<Collision> newCollisions = new ArrayList<Collision>();		
		
		return new GameState(newMap, newBullets, newTanks, newBases, newCollisions, 0, GameState.STATUS_ACTIVE);
	}
	
	public static void main(String[] args) {
		GameState gameState = generateRandom(50, 60, 10, 10);
		
		System.out.println(gameState);

	}
}
