package game;

import game.entity.Player;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.gui.Gui;
import game.gui.GuiFocus;
import game.gui.GuiMainMenu;
import game.level.Level;
import game.level.LevelLoader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable, FocusListener {

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public static final String NAME = "Game";
	public static final Dimension DIMENSIONS = new Dimension(WIDTH * SCALE,
			HEIGHT * SCALE);

	public JFrame frame;
	private Thread thread;

	private boolean running = false;
	public int ticks = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private int[] colors = new int[6 * 6 * 6];

	public Screen screen;
	public InputHandler input;

	public Level level;
	public Player player;
	public Gui gui;
	public Gui hud;

	private boolean isGamePaused;
	public static boolean isApplet = false;
	public boolean isFocused = true;

	public boolean debug;

	public static boolean DEBUG = true;

	public static String homeDir;

	public void init() {
		File f = new File(Game.homeDir);
		Game.debug(Game.DebugLevel.INFO, "Game directory set to "
				+ Game.homeDir);
		if (!f.exists()) {
			f.mkdir();
			Game.debug(Game.DebugLevel.INFO, "Directory created!");
		}
		if (isApplet) {
			addFocusListener(this);
		}
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

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);

		gui = new GuiMainMenu(this, WIDTH, HEIGHT);

		if (!new File(homeDir, File.separator + "level" + File.separator
				+ "tile_test.dat").exists()) {
			Level level = new Level("tile_test", 0, 0);
			level.loadLevelFromFile("/levels/tile_test.png");
			LevelLoader.writeLevelToNBT(level);
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticksPS = 0;
		int framesPS = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;

			boolean shouldRender = true;

			while (delta >= 1) {
				ticksPS++;
				tick();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(5L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				framesPS++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				debug(DebugLevel.INFO, framesPS + " frames, " + ticksPS
						+ " ticks");
				framesPS = 0;
				ticksPS = 0;
			}
		}
	}

	public void tick() {
		input.tick();
		if (!isGamePaused) {
			ticks++;
			if (gui != null) {
				gui.tick(ticks);
				if (gui != null && !gui.pausesGame()) {
					if (level != null) {
						tickLevel();
					}
				}
			} else {
				if (level != null) {
					tickLevel();
				}
			}
		}
	}

	private void tickLevel() {
		level.tick();
		hud.tick(ticks);
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (gui != null) {

			gui.render();

			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					int colorCode = gui.pixels[x + y * gui.width];
					pixels[x + y * WIDTH] = colorCode + (0xFF << 24);
				}
			}
		} else {
			if (level != null) {
				int xOffset = 0;
				int yOffset = 0;
				if (player != null) {
					xOffset = player.x - (screen.width / 2);
					yOffset = player.y - (screen.height / 2);
				}

				level.renderTiles(screen, xOffset, yOffset);
				level.renderEntities(screen);

				for (int y = 0; y < screen.height; y++) {
					for (int x = 0; x < screen.width; x++) {
						int colorCode = screen.pixels[x + y * screen.width];
						if (colorCode < 255) {
							pixels[x + y * WIDTH] = colors[colorCode];
						}
					}
				}
				if (hud != null) {
					hud.pixels = pixels;
					hud.render();
				}
			}
		}

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		g.dispose();
		bs.show();
	}

	public void setLevel(String path) {
		// this.level = new Level("non", 0, 0);
		level = LevelLoader.readLevelFromNBT(path);
		// this.level.loadLevelFromFile(path);
	}

	public void showGui(Gui gui) {
		if (this.gui == null) {
			this.gui = gui;
		}
	}

	public void hideGui() {
		this.gui = null;
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, Game.NAME + "_main");
		thread.start();
	}

	public synchronized void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void debug(DebugLevel level, String msg) {
		switch (level) {
		default:
		case INFO:
			if (Game.DEBUG) {
				System.out.println("[" + NAME + "] " + msg);
			}
			break;
		case WARNING:
			if (Game.DEBUG) {
				System.out.println("[" + NAME + "] WARNING: " + msg);
			}
			break;
		case ERROR:
			if (Game.DEBUG) {
				System.out.println("[" + NAME + "] CRIT. ERROR: " + msg);
				System.out.println("System forced to exit!");
			}
			break;
		}
	}

	public static enum DebugLevel {
		INFO, WARNING, ERROR;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		isFocused = true;
		if (gui != null && gui instanceof GuiFocus) {
			gui.closeGui();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		if (arg0.getID() == FocusEvent.FOCUS_LOST) {
			isFocused = false;
			if ((gui != null && !gui.pausesGame()) || gui == null) {
				gui = new GuiFocus(this, Game.WIDTH, Game.HEIGHT);
			}
			Game.debug(DebugLevel.INFO, "Lost the focus!");
		}
	}
}
