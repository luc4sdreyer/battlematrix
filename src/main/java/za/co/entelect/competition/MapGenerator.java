package za.co.entelect.competition;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
	private static Random r = new Random(); 
	
	//TODO: Assume that the base will always be on the wall that is farthest from the mirror axis.
	
	public static GameState generateRandom(int minWidth, int maxWidth, int minHeight, int maxHeight,
			int blockSize, double percentageWall, boolean basesOnSides, boolean mirrored, 
			int numTanksP1, int numTanksP2, long seed) {
		System.out.println("Using preset seed: " + seed);
		r.setSeed(seed);
		return generate(minWidth, maxWidth, minHeight, maxHeight, blockSize, percentageWall, basesOnSides, mirrored, numTanksP1, numTanksP2);				
	}
	
	public static GameState generateRandom(int minWidth, int maxWidth, int minHeight, int maxHeight,
			int blockSize, double percentageWall, boolean basesOnSides, boolean mirrored,
			int numTanksP1, int numTanksP2) {

		long nanoTime = System.nanoTime();
		System.out.println("Using random seed: " + nanoTime);
		r.setSeed(nanoTime);
		
		return generate(minWidth, maxWidth, minHeight, maxHeight, blockSize, percentageWall, basesOnSides, mirrored, numTanksP1, numTanksP2);
	}
	
	public static GameState generate(int minWidth, int maxWidth, int minHeight, int maxHeight,
			int blockSize, double percentageWall, boolean basesOnSides, boolean mirrored,
			int numTanksP1, int numTanksP2) {		
		boolean yAxisMirror = false;
		if (mirrored) {
			yAxisMirror = r.nextBoolean();
		}
		yAxisMirror = true;
		int[][] newMap = new int[r.nextInt(maxHeight - minHeight + 1) + minHeight][r.nextInt(maxWidth - minWidth + 1) + minWidth];
		for (int y = 0; y < newMap.length; y++) {
			//newMap[y] = Arrays.copyOf(this.map[y], this.map[0].length);
			for (int x = 0; x < newMap[0].length; x++) {
				newMap[y][x] = Unit.EMPTY;
			}
		}
		
		int numWalls = 0;
		double currentPercentageWall = numWalls / (newMap.length * newMap[0].length); 
		while(currentPercentageWall < percentageWall) {
			for (int y = 0; y < newMap.length; y++) {
				for (int x = 0; x < newMap[0].length; x++) {
					if (newMap[y][x] == Unit.WALL) {
						numWalls++;
					}
				}
			}
			currentPercentageWall = numWalls / (double)(newMap.length * newMap[0].length); 
			
			int numBlocksToPlace = (int) Math.ceil((percentageWall - currentPercentageWall) * (newMap.length * newMap[0].length) / (blockSize*blockSize) );
			
			for (int i = 0; i < numBlocksToPlace; i++) {
				Point corner = new Point(r.nextInt(newMap[0].length - blockSize + 1), r.nextInt(newMap.length - blockSize + 1));
				while (newMap[corner.y][corner.x] == Unit.WALL) {
					corner = new Point(r.nextInt(newMap[0].length - blockSize + 1), r.nextInt(newMap.length - blockSize + 1));
				}
				for (int y2 = corner.y; y2 < corner.y + blockSize; y2++) {
					for (int x2 = corner.x; x2 < corner.x + blockSize; x2++) {
						newMap[y2][x2] = Unit.WALL;
					}
				}
			}
		}
		
		if (mirrored) {
			if (yAxisMirror) {
				for (int y = 0; y < newMap.length; y++) {
					for (int x = 0; x < newMap[0].length/2; x++) {
						newMap[y][x] = newMap[y][newMap[0].length - x - 1];
					}
				}
			} else {
				for (int y = 0; y < newMap.length/2; y++) {
					for (int x = 0; x < newMap[0].length; x++) {
						newMap[y][x] = newMap[newMap.length - y - 1][x];
					}
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
				if (mirrored) {
					if (yAxisMirror) {
						p = new Point(r.nextInt(newMap[0].length/2 - GameState.tankSize + 1), r.nextInt(newMap.length - GameState.tankSize + 1));
					} else {
						p = new Point(r.nextInt(newMap[0].length - GameState.tankSize + 1), r.nextInt(newMap.length/2 - GameState.tankSize + 1));
					}
				} else {
					p = new Point(r.nextInt(newMap[0].length - GameState.tankSize + 1), r.nextInt(newMap.length - GameState.tankSize + 1));
				}
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
			//if (r.nextInt(4) == 0) {
			//	alive = false;
			//}
			
			newTanks[i] = new Tank(p, r.nextInt(4), null, alive);			
			
			Tank t = newTanks[i];
			if (!t.isAlive()) {
				continue;
			}
			if (mirrored && i >= 2) {
				Point t1middle = new Point(newTanks[i - 2].getPosition());
				t1middle.translate(GameState.tankSize/2, GameState.tankSize/2);
				Point t2middle = null;
				if (yAxisMirror) {
					t2middle = new Point(newMap[0].length - t1middle.x - 1, t1middle.y);
				} else {
					t2middle = new Point(t1middle.x, newMap.length - t1middle.y - 1);
				}
				t2middle.translate(-GameState.tankSize/2, -GameState.tankSize/2);
				t.setPosition(t2middle);
				
				int t1rotation = newTanks[i - 2].getRotation();
				if (t1rotation == 1 || t1rotation == 3) {
					t1rotation = (t1rotation + 2) % 4;
				}
				t.setRotation(t1rotation);	
			}
			
			//
			// Clean the area around the tank.
			//
			for (int y2 = -1; y2 < GameState.tankSize + 1; y2++) {
				for (int x2 = -1; x2 < GameState.tankSize + 1; x2++) {
					if ((y2 == -1 || y2 == GameState.tankSize || x2 == -1 || x2 == GameState.tankSize)
							&& (t.getPosition().y+y2 >= 0 && t.getPosition().y+y2 < newMap.length)
							&& (t.getPosition().x+x2 >= 0 && t.getPosition().x+x2 < newMap[0].length)) {
						if (newMap[t.getPosition().y+y2][t.getPosition().x+x2] == Unit.WALL) {
							newMap[t.getPosition().y+y2][t.getPosition().x+x2] = Unit.EMPTY;
						}
					}
				}
			}

			if (i < 2 && i >= numTanksP1) {
				t.setAlive(false);
			} else if (i >= 2 && i-2 >= numTanksP2) {
				t.setAlive(false);
			}
			
			for (int y2 = 0; y2 < GameState.tankSize; y2++) {
				for (int x2 = 0; x2 < GameState.tankSize; x2++) {
					if (t.isAlive()) {
						newMap[t.getPosition().y+y2][t.getPosition().x+x2] = Unit.TANK1A+i;
					} else {
						newMap[t.getPosition().y+y2][t.getPosition().x+x2] = Unit.EMPTY;
					}
				}
			}
		}
		
		for (int i = 0; i < newBases.length; i++) {
			Point p = null;
			while (p == null || !Unit.isEmptyOrWall(newMap[p.y][p.x])) {
				if (basesOnSides) {
					switch(r.nextInt(4)) {
					case 0: 
						p = new Point(r.nextInt(newMap[0].length - 4) + 2, 0);
						break;
					case 1: 
						p = new Point(r.nextInt(newMap[0].length - 4) + 2, newMap.length-1);
						break;
					case 2: 
						p = new Point(0, r.nextInt(newMap.length - 4) + 2);
						break;
					case 3: 
						p = new Point(newMap[0].length-1, r.nextInt(newMap.length - 4) + 2);
						break;
					}				
				} else {
					p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
				}
				
				if (mirrored) {
					if (yAxisMirror) {
						//if (p.x >= newMap[0].length / 2) {
						if (p.x != 0) {
							p = null;
						}
					} else {
						//if (p.y >= newMap.length / 2) {
						if (p.y != 0) {
							p = null;
						}
					}
				}
			}
			boolean alive = true;
			if (r.nextInt(4) == -1) {
				alive = false;
			}
			newBases[i] = new Base(p, r.nextInt(4), alive);
			Base b = newBases[i]; 
			if (!b.isAlive()) {
				continue;
			}
			if (mirrored && i >= 1) {
				Point b1middle = new Point(newBases[i - 1].getPosition());
				Point b2middle = null;
				if (yAxisMirror) {
					b2middle = new Point(newMap[0].length - b1middle.x - 1, b1middle.y);
				} else {
					b2middle = new Point(b1middle.x, newMap.length - b1middle.y - 1);
				}
				b.setPosition(b2middle);
			}
			newMap[b.getPosition().y][b.getPosition().x] = Unit.BASE1+i;
		}
		
		for (int i = 0; i < newBullets.length; i++) {
			Point p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
			while (p == null || newMap[p.y][p.x] != Unit.EMPTY) {
				p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
				
				if (mirrored) {
					if (yAxisMirror) {
						p = new Point(r.nextInt(newMap[0].length/2), r.nextInt(newMap.length));
					} else {
						p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length/2));
					}
				} else {
					p = new Point(r.nextInt(newMap[0].length), r.nextInt(newMap.length));
				}
			}
			boolean alive = false;
//			if (r.nextInt(4) == 0) {
//				alive = false;
//			}
			newBullets[i] = new Bullet(p, r.nextInt(4), alive);
			Bullet b = newBullets[i]; 
			if (!b.isAlive()) {
				continue;
			}
			if (mirrored && i >= 2) {
				Point t1middle = new Point(newBullets[i - 2].getPosition());
				Point t2middle = null;
				if (yAxisMirror) {
					t2middle = new Point(newMap[0].length - t1middle.x - 1, t1middle.y);
				} else {
					t2middle = new Point(t1middle.x, newMap.length - t1middle.y - 1);
				}
				b.setPosition(t2middle);
				
				int t1rotation = newBullets[i - 2].getRotation();
				if (t1rotation == 1 || t1rotation == 3) {
					t1rotation = (t1rotation + 2) % 4;
				}
				b.setRotation(t1rotation);	
			}
			newMap[b.getPosition().y][b.getPosition().x] = Unit.BULLET_TANK1A+i;
		}
		
		ArrayList<Collision> newCollisions = new ArrayList<Collision>();		
		
		return new GameState(newMap, newBullets, newTanks, newBases, newCollisions, 0);
	}
	
	public static void main(String[] args) {
		
		
		GameState gameState = generateRandom(100, 120, 20, 20, 5, 0.15, true, true, 1, 0, 50851925610806L); //, 47515685604563L
		
		System.out.println(gameState);

	}
}
