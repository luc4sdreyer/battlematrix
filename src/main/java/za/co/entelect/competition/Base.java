package za.co.entelect.competition;

import java.awt.Point;

/**
 * Represents the Base unit.
 */
public class Base extends Unit {
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
