package game;

import game.entity.Player;
import game.gfx.Colors;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.gfx.Tint;
import game.gui.Gui;
import game.gui.GuiMainMenu;
import game.level.Level;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 180;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	public static final String NAME = "Game";

	public JFrame frame;

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

	public Time time;
	private int timeDurationLeft;
	private int lastTimeSwitchTicks;
	private Tint currentTint;

	private boolean isGamePaused;

	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus();
	}

	public void init() {
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

		time = Time.MORNING;

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);

		gui = new GuiMainMenu(this, WIDTH, HEIGHT);
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
				Thread.sleep(2L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				framesPS++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(framesPS + " frames, " + ticksPS + " ticks"
						+ " weight: " + (double) (ticks - lastTimeSwitchTicks)
						/ (double) time.getDuration());
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
		timeDurationLeft--;
		if (timeDurationLeft <= 0) {
			switchTime();
		}
		currentTint = Tint.getWeightedAverage(
				time.getTint(),
				(this.getTime(time.getId() - 1)).getTint(),
				(double) (ticks - lastTimeSwitchTicks)
						/ (double) time.getDuration());
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
							// pixels[x + y * WIDTH] = colors[colorCode];
							// pixels[x + y * WIDTH] =
							// Colors.tint(colors[colorCode], 0.2D, 0.2D, 0.6D);
							pixels[x + y * WIDTH] = Colors.tint(
									colors[colorCode], currentTint);
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
		this.level = new Level();
		this.level.loadLevelFromFile(path);
	}

	public void showGui(Gui gui) {
		if (this.gui == null) {
			this.gui = gui;
		}
	}

	public void hideGui() {
		this.gui = null;
	}

	public void switchTime() {
		switch (time) {
		case NIGHT:
			time = Time.MORNING;
			break;
		case EVEN:
			time = Time.NIGHT;
			break;
		case NOON:
			time = Time.EVEN;
			break;
		case MORNING:
			time = Time.NOON;
			break;
		}
		timeDurationLeft = time.getDuration();
		lastTimeSwitchTicks = ticks;
		System.out.println("Time set to: " + time);
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public static void main(String[] args) {
		new Game().start();
	}

	public enum Time {
		MORNING(0, new Tint(1.0D, 0.9D, 0.7D), 150), NOON(1, new Tint(1.0D,
				1.0D, 1.0D), 700), EVEN(2, new Tint(1.0D, 0.8D, 0.4D), 150), NIGHT(
				3, new Tint(0.3D, 0.3D, 0.8D), 500);

		private Tint tint;
		private int duration;
		private int id;

		private Time(int id, Tint tint, int duration) {
			this.tint = tint;
			this.id = id;
			this.duration = duration;
		}

		public int getId() {
			return id;
		}

		public Tint getTint() {
			return tint;
		}

		public int getDuration() {
			return duration;
		}
	}

	public Time getTime(int id) {
		Time time = Time.NIGHT;
		for (int i = 0; i < Time.values().length; i++) {
			if (Time.values()[i].id == id) {
				time = Time.values()[i];
			}
		}
		return time;
	}

}
