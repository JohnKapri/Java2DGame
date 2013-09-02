package game.gui.elements;

import game.gfx.Colors;
import game.gfx.SpriteSheet;
import game.gui.Gui;

public class FontRenderer {
	
	public static SpriteSheet FONT1 = new SpriteSheet("/font1.png");

//	private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ���>< "
//			+ "abcdefghijklmnopqrstuvwxyz���{} "
//			+ "1234567890+-/*^!.,?#()=߀$&%\"':;";
//	private static String width5 = "ABCDEFGHJLMNOPQRSTUVWXYZ���mvw234567890+-^?#�$&%{}";
//	private static String width4 = "Kabdefghnopqrstuyz���/=�\"";
//	private static String width3 = "Icktx1*><";
//	private static String width2 = "jl,()'; ";
//	private static String width1 = "i!.:";
	
	private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ>< "
			+ "abcdefghijklmnopqrstuvwxyzäöü{} "
			+ "1234567890+-*/,.()[]<>!?=ß\"':;^ ";
	private static String width5 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdeghkmnopqrsuvwxyz1234567890+-=?{}^";
	private static String width4 = "ftß\"";
	private static String width3 = "><ij*/ ";
	private static String width2 = "l,.()[]';";
	private static String width1 = "!:";

	public static void drawString(String msg, Gui gui, int x, int y,
			int color, int scale) {
		int textWidth = 0;

		for (int i = 0; i < msg.length(); i++) {
			int charIndex = chars.indexOf(msg.charAt(i));
			int charWidth = getCharWidth(msg.charAt(i));
			if (charIndex < 0)
				charIndex = 29;
			GuiRenderer.render(gui, FONT1, x + textWidth + scale * i, y, charIndex, Colors.get(-1, -1, -1, color),
					0x00, scale);
			textWidth = textWidth + charWidth * scale;
		}
	}

	public static void drawCenteredString(String msg, Gui gui, int x,
			int y, int color, int scale) {
		FontRenderer.drawString(msg, gui, x - FontRenderer.getStringWidth(msg, scale) / 2, y,
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
		int charWidth = 5;
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
