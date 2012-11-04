package game.gfx;

import java.util.Random;

public class Screen {

	public static int MAP_WIDTH = 64;
	public static int MAP_WIDTH_MASK = MAP_WIDTH - 1;

	static int BIT_MIRROR_X = 0x01;
	static int BIT_MIRROR_Y = 0x02;

	public int[] pixels;

	public int xOffset = 0;
	public int yOffset = 0;

	public int width;
	public int height;

	public SpriteSheet sheet;

	public Random rand;

	public Screen(int width, int height, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		this.sheet = sheet;

		pixels = new int[width * height];
		rand = new Random();
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void render(int xPos, int yPos, int tile, int color, int mirrorDir,
			int scale) {
		xPos -= xOffset;
		yPos -= yOffset;

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
						if (yPixel + yScale < 0 || yPixel + yScale >= height)
							continue;
						for (int xScale = 0; xScale < scale; xScale++) {
							if (xPixel + xScale < 0 || xPixel + xScale >= width)
								continue;
							pixels[(xPixel + xScale) + (yPixel + yScale)
									* width] = col;
						}
					}
				}
			}
		}
	}

	public void renderFromSheet(SpriteSheet sheet, int xPos, int yPos,
			int tile, int color, int mirrorDir, int scale) {
		xPos -= xOffset;
		yPos -= yOffset;

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
						if (yPixel + yScale < 0 || yPixel + yScale >= height)
							continue;
						for (int xScale = 0; xScale < scale; xScale++) {
							if (xPixel + xScale < 0 || xPixel + xScale >= width)
								continue;
							pixels[(xPixel + xScale) + (yPixel + yScale)
									* width] = col;
						}
					}
				}
			}
		}
	}

	public void renderDirect(int xPos, int yPos, int width, int height,
			int[] pixels) {
		if (width > this.width)
			width = this.width;
		if (height > this.height)
			height = this.height;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int col = pixels[x + y * width];
				if ((col >> 24) != 0)
					this.pixels[(x + xPos) + (y + yPos) * this.width] = col;
			}
		}
	}
}
