package smartspace.data;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
	private double x;
	private double y;

	public Location() {
	}

	public Location(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
