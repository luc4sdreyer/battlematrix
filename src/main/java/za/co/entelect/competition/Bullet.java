package za.co.entelect.competition;

import java.awt.Point;

public class Bullet extends Unit {
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