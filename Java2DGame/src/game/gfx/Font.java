package game.gfx;


public class Font {

	private static String chars2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ���>< "
			+ "abcdefghijklmnopqrstuvwxyz���{} "
			+ "1234567890+-/*^!.,?#()߀$&%\"':;";
	private static String width5 = "ABCDEFGHJLMNOPQRSTUVWXYZ���mvw234567890+-^?#�$&%{}";
	private static String width4 = "Kabcdefghnopqrstuyz���/=�\"";
	private static String width3 = "Iktx1*><";
	private static String width2 = "jl,()'; ";
	private static String width1 = "i!.:";

	public static void drawString(String msg, Screen screen, int type, int x, int y,
			int color, int scale) {
		int textWidth = 0;

		for (int i = 0; i < msg.length(); i++) {
			int charIndex = chars2.indexOf(msg.charAt(i));
			int charWidth = getCharWidth(msg.charAt(i));
			if (charIndex < 0)
				charIndex = 29;
			screen.render(x + textWidth + scale * 2, y, charIndex + 23 * 32, color,
					0x00, scale);
			textWidth = textWidth + charWidth * scale + 1;
		}
	}

	public static void drawCenteredString(String msg, Screen screen, int type, int x,
			int y, int color, int scale) {
		Font.drawString(msg, screen, type, x - getStringWidth(msg, scale) / 2, y,
				color, scale);
	}
	
	public static int getStringWidth(String msg, int scale) {
		int textWidth = 0;
		for (int i = 0; i < msg.length(); i++) {
			int charWidth = getCharWidth(msg.charAt(i));
			textWidth = textWidth + charWidth * scale + 1;
		}
		return textWidth;
	}
	
	private static int getCharWidth(char c) {
		int charWidth = 6;
		if(width5.indexOf(c) >= 0) {
			charWidth = 5;
		}
		if(width4.indexOf(c) >= 0) {
			charWidth = 4;
		}
		if(width3.indexOf(c) >= 0) {
			charWidth = 3;
		}
		if(width2.indexOf(c) >= 0) {
			charWidth = 2;
		}
		if(width1.indexOf(c) >= 0) {
			charWidth = 1;
		}
		return charWidth;
	}
}
