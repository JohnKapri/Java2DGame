package game;

import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.gui.Gui;
import game.gui.GuiFocus;
import game.gui.GuiMainMenu;
import game.gui.GuiPause;
import game.level.World;

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

	// public Level level;
	private World world;
	// public Player player;
	private Gui gui;
	public Gui hud;

	public static boolean isApplet = false;
	public boolean isFocused = true;

	public boolean debug;

	public static boolean DEBUG = true;

	public static Game instance;
	public static String homeDir;

	public void init() {
		File f = new File(Game.homeDir);
		Game.debug(Game.DebugLevel.INFO, "Game directory set to "
				+ Game.homeDir);
		if (!f.exists()) {
			f.mkdir();
			Game.debug(Game.DebugLevel.INFO, "Directory " + f.getAbsolutePath()
					+ " created!");
		}
		File w = new File(World.WORLD_DIR);
		if (!w.exists()) {
			w.mkdir();
			Game.debug(Game.DebugLevel.INFO, "Directory " + w.getAbsolutePath()
					+ " created!");
		}
		addFocusListener(this);
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

		showGui(new GuiMainMenu(this, WIDTH, HEIGHT));
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
		ticks++;
		if (gui != null) {
			gui.tick(ticks);
			if (gui != null && !gui.pausesGame()) {
				if (world != null) {
					tickLevel();
				}
			}
		} else {
			if (world != null) {
				tickLevel();
			}
		}
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	private void tickLevel() {
		world.tick();
		if (hud != null) {
			hud.tick(ticks);
		}
	}

	private void render() {
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
			if (world != null) {
				world.render(screen);

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

	/**
	 * Opens a GUI to display.
	 * 
	 * @param gui
	 *            The GUI that should be displayed. Usually a new instance.
	 */
	public void showGui(Gui gui) {
		if(this.gui != null) {
			input.removeListener(this.gui);
		}
		this.gui = null;
		this.gui = gui;
		input.addListener(this.gui);
	}

	/**
	 * Closes the currently displayed GUI.
	 * 
	 * @param gui The GUI that should be closed.
	 */
	public void hideGui(Gui gui) {
		input.removeListener(gui);
		if (gui.getParentGui() != null) {
			showGui(gui.getParentGui());
		}
		if (this.gui == gui) {
			this.gui = null;
		}
	}

	/**
	 * Forces any menu to close. THIS IS ONLY USED IN THE LEVEL EDITOR.
	 */
	public void forceGuiClose() {
		if (gui != null) {
			gui.close();
		}
		gui = null;
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

	/**
	 * Sends a debug message to the console.
	 * 
	 * @param level
	 *            The debug level (what type of message, "INFO", "WARNING",
	 *            "ERROR")
	 * @param msg
	 *            The text to output.
	 */
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
				System.out.println("[" + NAME + "] CRITICAL ERROR: " + msg);
				System.out.println("System forced to exit!");
			}
			break;
		}
	}

	/**
	 * A list of debug levels. INFO: Some information useful for programmers
	 * WARNING: If something isn't performing as expected but is not critical
	 * for the runtime. ERROR: Something bad happened and the game can no longer
	 * run.
	 * 
	 * @author John Kapri
	 */
	public static enum DebugLevel {
		INFO, WARNING, ERROR;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		isFocused = true;
		if (gui != null && gui instanceof GuiFocus) {
			gui.last();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		if (arg0.getID() == FocusEvent.FOCUS_LOST) {
			if (isApplet) {
				isFocused = false;
				if ((gui != null && !gui.pausesGame()) || gui == null) {
					gui = new GuiFocus(this, Game.WIDTH, Game.HEIGHT);
				}
			} else {
				if ((gui != null && !gui.pausesGame()) || gui == null) {
					gui = new GuiPause(this, Game.WIDTH, Game.HEIGHT);
				}
			}
			Game.debug(DebugLevel.INFO, "Lost the focus!");
		}
	}
}
