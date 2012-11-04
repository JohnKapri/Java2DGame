package game.gfx;

import java.awt.Color;

public class Colors {

	public static int get(int color1, int color2, int color3, int color4) {
		return (get(color4) << 24) + (get(color3) << 16) + (get(color2) << 8)
				+ get(color1);
	}

	public static int get(int color) {
		if (color < 0)
			return 255;
		int r = color / 100 % 10;
		int g = color / 10 % 10;
		int b = color % 10;

		return r * 36 + g * 6 + b;
	}
	
	public static int brightness(int color, double scale) {
		Color c = new Color(color);
		int r = Math.min(255, (int) (c.getRed() * scale));
		int g = Math.min(255, (int) (c.getGreen() * scale));
		int b = Math.min(255, (int) (c.getBlue() * scale));
		return (b | g << 8 | r << 16);
	}
	
	public static int tint(int color, double red, double green, double blue) {
		Color c = new Color(color);
		int r = Math.min(255, (int) (c.getRed() * red));
		int g = Math.min(255, (int) (c.getGreen() * green));
		int b = Math.min(255, (int) (c.getBlue() * blue));
		return (b | g << 8 | r << 16);
	}
	
	public static int tint(int color, Tint tint) {
		return Colors.tint(color, tint.getRed(), tint.getGreen(), tint.getBlue());
	}
}
