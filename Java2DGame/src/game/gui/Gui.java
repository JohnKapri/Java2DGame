package game.gui;

import game.Game;
import game.InputHandler;
import game.InputHandler.GameActionListener;
import game.gfx.Colors;
import game.gfx.SpriteSheet;

public abstract class Gui implements GameActionListener{

	private static int DEFAULT_BKG_COLOR = 0x555555;
	private double red;
	private double green;
	private double blue;

	protected Game game;
	public InputHandler input;
	
	public SpriteSheet font;

	public int[] pixels;
	public int[] bkgPixels;
	public int width;
	public int height;

	protected boolean pauseGame;

	public Gui(Game game, int width, int height) {
		this.game = game;
		this.input = game.input;
		input.addListener(this);
		this.font = new SpriteSheet("/sprite_sheet.png");
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
		this.setTint(0.3D, 0.3D, 0.3D);
		if (game.getWorld() != null && game.screen.pixels.length == pixels.length) {
			bkgPixels = game.pixels.clone();
		}
	}

	public abstract void render();

	public abstract void tick(int ticks);
	
	public abstract void guiActionPerformed(int elementId, int action);

	public boolean pausesGame() {
		return pauseGame;
	}

	public void drawDefaultBackground() {
		if (bkgPixels != null) {
			for (int i = 0; i < bkgPixels.length; i++) {
				pixels[i] = Colors.tint(bkgPixels[i], red, green, blue);
			}
		} else {
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = DEFAULT_BKG_COLOR;
			}
		}
	}

	public void drawRect(int xPos, int yPos, int width, int height, int color) {
		if (xPos > this.width)
			xPos = this.width - 1;
		if (yPos > this.height)
			yPos = this.height - 1;
		if (xPos + width > this.width)
			width = this.width - xPos;
		if (yPos + height > this.height)
			height = this.height - yPos;
		width -= 1;
		height -= 1;
		for (int x = xPos; x < xPos + width; x++) {
			pixels[x + yPos * this.width] = color;
		}
		for (int y = yPos; y < yPos + height; y++) {
			pixels[xPos + y * this.width] = color;
		}
		for (int x = xPos; x < xPos + width; x++) {
			pixels[x + (yPos + height) * this.width] = color;
		}
		for (int y = yPos; y < yPos + height; y++) {
			pixels[(xPos + width) + y * this.width] = color;
		}
	}

	public void fillRect(int xPos, int yPos, int width, int height, int color) {
		if (xPos > this.width)
			xPos = this.width;
		if (yPos > this.height)
			yPos = this.height;
		if (xPos + width > this.width)
			width = this.width - xPos;
		if (yPos + height > this.height)
			height = this.height - yPos;
		for (int y = yPos; y < yPos + height; y++) {
			for (int x = xPos; x < xPos + width; x++) {
				pixels[x + y * this.width] = color;
			}
		}
	}
	
	public void setTint(double r, double g, double b) {
		red = r;
		green = g;
		blue = b;
	}
	
	public void closeGui() {
		game.input.removeListener(this);
		game.hideGui(this);
	}
}
