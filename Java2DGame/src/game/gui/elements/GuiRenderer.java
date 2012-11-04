package game.gui.elements;

import game.gfx.SpriteSheet;
import game.gui.Gui;

public class GuiRenderer {

	static int BIT_MIRROR_X = 0x01;
	static int BIT_MIRROR_Y = 0x02;

	public static void render(Gui gui, SpriteSheet sheet, int xPos, int yPos,
			int tile, int color, int mirrorDir, int scale) {

		if (gui == null) {
			return;
		}

		if (sheet == null) {
			return;
		}
		
		int[] colors = new int[6 * 6 * 6];
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);

					colors[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

		int scaleMap = scale - 1;

		int xTile = tile % 32;
		int yTile = tile / 32;

		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;

		for (int y = 0; y < 8; y++) {
			int ySheet = y;
			if (mirrorY)
				ySheet = 7 - y;

			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);

			for (int x = 0; x < 8; x++) {
				int xSheet = x;
				if (mirrorX)
					xSheet = 7 - x;
				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
				int col = (color >> (sheet.pixels[xSheet + ySheet * sheet.width
						+ tileOffset] * 8)) & 255;

				if (col < 255) {
					for (int yScale = 0; yScale < scale; yScale++) {
						if (yPixel + yScale < 0
								|| yPixel + yScale >= gui.height)
							continue;
						for (int xScale = 0; xScale < scale; xScale++) {
							if (xPixel + xScale < 0
									|| xPixel + xScale >= gui.width)
								continue;
							gui.pixels[(xPixel + xScale) + (yPixel + yScale)
									* gui.width] = colors[col];
						}
					}
				}
			}
		}
	}
}
