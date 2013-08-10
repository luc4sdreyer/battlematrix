package za.co.entelect.competition;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Util {
	public final static SimpleDateFormat milli = new SimpleDateFormat("mm:ss.SSS");
	public final static SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	public final static Random javaRandom = new Random();	
	public final static cern.jet.random.engine.MersenneTwister64 random = 
			new cern.jet.random.engine.MersenneTwister64(new java.util.Date());
	
	public final static String zTableFile = "assets\\zobristTable.csv";
	public final static long[][] zTable = loadZobristTable();

	public final static int Z_EMPTY          = 0;
	public final static int Z_WALL           = 1;
	public final static int Z_TANK1A         = 2;
	public final static int Z_TANK1B         = Z_TANK1A + 4;
	public final static int Z_TANK2A         = Z_TANK1B + 4;
	public final static int Z_TANK2B         = Z_TANK2A + 4;
	public final static int Z_BULLET_TANK1A  = Z_TANK2B + 4;
	public final static int Z_BULLET_TANK1B  = Z_BULLET_TANK1A + 4;
	public final static int Z_BULLET_TANK2A  = Z_BULLET_TANK1B + 4;
	public final static int Z_BULLET_TANK2B  = Z_BULLET_TANK2A + 4;
	public final static int Z_BASE1          = Z_BULLET_TANK2B + 4;
	public final static int Z_BASE2          = Z_BASE1 + 1;
	public final static int Z_LENGTH         = Z_BASE2 + 1;
	
	public static Point movePoint(Point p, int direction) {
		//return new Point(p.x + (int)Math.sin(Math.PI/2*direction), p.y + (int)Math.cos(Math.PI/2*direction));
		if (direction == 0) {
			return new Point(p.x, p.y+1);
		} else if (direction == 1) {
			return new Point(p.x+1, p.y);
		} else if (direction == 2) {
			return new Point(p.x, p.y-1);
		} else if (direction == 3) {
			return new Point(p.x-1, p.y);
		} else {
			return null;
		}
	}	
	
	public static int getDirection(Point oldP, Point newP) {
		if (mDist(oldP, newP) != 1) {
			System.err.println("FATAL ERROR: mDist(p1, p2) != 1");
			return -1;
		} else {
			return getDirectionUnbounded(oldP, newP);
		}
	}
	
	public static int getDirectionUnbounded(Point oldP, Point newP) {
		if (mDist(oldP, newP) < 1) {
			System.err.println("FATAL ERROR: mDist(p1, p2) < 1");
			return -1;
		} 
		
		if (oldP.y < newP.y) {
			return GameAction.ACTION_MOVE_NORTH;
		} else if (oldP.y > newP.y) {
			return GameAction.ACTION_MOVE_SOUTH;
		} else if (oldP.x < newP.x) {
			return GameAction.ACTION_MOVE_EAST;
		} else if (oldP.x > newP.x) {
			return GameAction.ACTION_MOVE_WEST;
		} else {
			System.err.println("FATAL ERROR: getDirection(Point oldP, Point newP)");
			return -1;
		}
	}

	public static Point movePointDist(Point p, int direction, int distance, GameState gameState) {
		//return new Point(p.x + (int)Math.sin(Math.PI/2*direction), p.y + (int)Math.cos(Math.PI/2*direction));
		int d = 0;
		if (direction == 0) {
			while (d < distance && (p.y + d < gameState.getMap().length - 1)) {
				d++;
			}
			return new Point(p.x, p.y+d);
		} else if (direction == 1) {
			while (d < distance && (p.x + d < gameState.getMap()[0].length - 1)) {
				d++;
			}
			return new Point(p.x+d, p.y);
		} else if (direction == 2) {
			while (d < distance && (p.y - d > 0)) {
				d++;
			}
			return new Point(p.x, p.y-d);
		} else if (direction == 3) {
			while (d < distance && (p.x - d > 0)) {
				d++;
			}
			return new Point(p.x-d, p.y);
		} else {
			return null;
		}
	}

	public static Point movePointDist(Point p, int direction, int distance) {
		//return new Point(p.x + (int)Math.sin(Math.PI/2*direction), p.y + (int)Math.cos(Math.PI/2*direction));
		int d = 0;
		if (direction == 0) {
			while (d < distance) {
				d++;
			}
			return new Point(p.x, p.y+d);
		} else if (direction == 1) {
			while (d < distance) {
				d++;
			}
			return new Point(p.x+d, p.y);
		} else if (direction == 2) {
			while (d < distance) {
				d++;
			}
			return new Point(p.x, p.y-d);
		} else if (direction == 3) {
			while (d < distance) {
				d++;
			}
			return new Point(p.x-d, p.y);
		} else {
			return null;
		}
	}

	public static int mDist(Point p1, Point p2) {
		return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
	}

	public static void print(String out) {
		System.out.println(milli.format(Calendar.getInstance().getTime())+" "+out);
	}

	public static void generateZobristTable() throws IOException {
		System.out.println("Generating Zobrist table...");

		long[][] newZTable = new long[GameState.maxNumBlocks][Z_LENGTH];
		for (int i = 0; i < newZTable.length; i++) {
			for (int j = 0; j < newZTable[0].length; j++) {
				newZTable[i][j] = random.nextLong();
			}
		}

		File tableFile = new File(zTableFile);
		BufferedWriter tableWriter = null;
		tableWriter = new BufferedWriter(new FileWriter(tableFile));

		for (int i = 0; i < newZTable.length; i++) {
			for (int j = 0; j < newZTable[0].length; j++) {
				tableWriter.write(Long.toString(newZTable[i][j]));
				if (j == newZTable[0].length - 1) {
					tableWriter.write('\n');
				} else {
					tableWriter.write(',');
				}
			}
		}

		tableWriter.close();
	}

	private static long[][] loadZobristTable() {
		long[][] newZTable = new long[GameState.maxNumBlocks][Z_LENGTH];

		File tableFile = new File(zTableFile);
		if (!tableFile.canRead()) {
			try {
				generateZobristTable();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Scanner in = null;
		try {
			in = new Scanner(new File(zTableFile));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();			
		}

		for (int i = 0; i < newZTable.length; i++) {
			StringTokenizer st = new StringTokenizer(in.nextLine(), ",");
			for (int j = 0; j < newZTable[0].length; j++) {
				newZTable[i][j] = Long.parseLong(st.nextToken());
			}
		}

		in.close();

//		for (int i = 0; i < newZTable.length; i++) {
//			for (int j = 0; j < newZTable[0].length; j++) {
//				System.out.print(newZTable[i][j]+",");
//			}
//			System.out.println();
//		}

		return newZTable;
	}

	public static void main(String args[]) {
//		try {
//			generateZobristTable();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
