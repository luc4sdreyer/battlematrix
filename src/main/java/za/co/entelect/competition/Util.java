package za.co.entelect.competition;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
	public static SimpleDateFormat milli = new SimpleDateFormat("mm:ss.SSS");
	public static SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	
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
	
	public static int mDist(Point p1, Point p2) {
		return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
	}
	
	public static void print(String out) {
		System.out.println(milli.format(Calendar.getInstance().getTime())+" "+out);
	}
}
