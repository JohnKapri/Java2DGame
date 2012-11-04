package game.gfx;

public class Tint {
	
	private double red;
	private double green;
	private double blue;

	public Tint(double r, double g, double b) {
		setRed(r);
		setGreen(g);
		setBlue(b);
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}
	
	public static Tint getWeightedAverage(Tint tint1, Tint tint2, double weight) {
		return new Tint(((tint1.getRed() * weight) + (tint2.getRed() * (1.0D - weight))) / 2, ((tint1.getGreen() * weight) + (tint2.getGreen() * (1.0D - weight))) / 2, ((tint1.getBlue() * weight) + (tint2.getBlue() * (1.0D - weight))) / 2);
	}
}
