package game.entity;

import game.Game;
import game.InputHandler;
import game.InputHandler.GameActionListener;
import game.gfx.Colors;
import game.gfx.Font;
import game.gfx.Screen;
import game.gui.GuiDead;
import game.gui.GuiHUD;
import game.gui.GuiPause;
import game.level.Level;
import game.level.tile.Tile;

public class Player extends Mob implements GameActionListener {

	private InputHandler input;
	private Level level;
	private Game game;
	private int health;
	private int maxHealth = 5;
	private int color = Colors.get(-1, 222, 145, 543);
	private int hurtTime = 0;
	private boolean gameOver = true;
	private boolean display = true;
	private int walkAnimStat = 0;
	private boolean canHandle = false;
	private boolean isActing = false;
	private Entity performActionOn = null;

	public Player(Game game, Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		input.addListener(this);
		this.level = level;
		this.game = game;
		health = 3;
		this.input = input;
		this.renderLayer = 3;
		game.hud = new GuiHUD(game, this, Game.WIDTH, Game.HEIGHT);
	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}

	public void tick() {
		if (hurtTime % 3 == 0) {
			display = !display;
		}
		if (hurtTime == 0) {
			display = true;
		}
		if (hurtTime > 0) {
			hurtTime--;
		}

		if (numSteps % 10 == 0 && isMoving) {
			if (walkAnimStat == 0) {
				walkAnimStat = 1;
			} else {
				walkAnimStat = 0;
			}
		}

		int xa = 0;
		int ya = 0;

		if (input.up.isPressed()) {
			ya--;
		}
		if (input.down.isPressed()) {
			ya++;
		}
		if (input.left.isPressed()) {
			xa--;
		}
		if (input.right.isPressed()) {
			xa++;
		}

		if (input.action.isPressed() && !isActing) {
			if (performActionOn != null) {
				performActionOn.action(this);
			}
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}

		if (level.getTile((x - 4) / 8, (y - 4) / 8).getId() == Tile.SPIKES
				.getId()
				|| level.getTile((x + 4) / 8, (y + 4) / 8).getId() == Tile.SPIKES
						.getId()
				|| level.getTile((x - 4) / 8, (y + 4) / 8).getId() == Tile.SPIKES
						.getId()
				|| level.getTile((x + 4) / 8, (y - 4) / 8).getId() == Tile.SPIKES
						.getId())
			hurt();

		for (int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if (e.x > this.x - 8 && e.x < this.x + 8 && e.y > this.y - 8
					&& e.y < this.y + 8) {
				if (e instanceof Chest) {
					canHandle = true;
					performActionOn = e;
				} else {
					canHandle = false;
					performActionOn = null;
				}
			}
		}

		if (health <= 0) {
			color = Colors.get(-1, 222, 145, 232);
		}
	}

	public void render(Screen screen) {

		int xTile = (movingDir * 2 + walkAnimStat) * 2;
		int yTile = 28;

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		if (display) {
			screen.render(xOffset, yOffset, xTile + yTile * 32, color, 0x00,
					scale);
			screen.render(xOffset + modifier, yOffset,
					(xTile + 1) + yTile * 32, color, 0x00, scale);

			screen.render(xOffset, yOffset + modifier,
					xTile + (yTile + 1) * 32, color, 0x00, scale);
			screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
					+ (yTile + 1) * 32, color, 0x00, scale);
		}

//		for (int i = 0; i < maxHealth; i++) {
//			int color;
//			if (i < health) {
//				color = Colors.get(-1, -1, 400, 400);
//				if (!display)
//					color = Colors.get(-1, -1, 555, 400);
//			} else {
//				color = Colors.get(-1, -1, 222, 222);
//				if (!display)
//					color = Colors.get(-1, -1, 555, 222);
//			}
//
//			screen.render(screen.xOffset + i * 8, screen.yOffset, 32, color,
//					0x00, 1);
//		}

		if (canHandle) {
			screen.render(x, y - 18, 40, Colors.get(-1, 500, 440, 550), 0x00, 1);
		}

		if (gameOver)
			Font.drawCenteredString("Press <ESC> for menu", screen, 1,
					screen.xOffset + screen.width / 2, screen.yOffset
							+ screen.height / 2 - 4,
					Colors.get(-1, -1, -1, 000), 1);
	}

	public void hurt() {
		if (hurtTime == 0) {
			hurtTime = 60;
			health--;
			if (health <= 0) {
				game.showGui(new GuiDead(game));
			}
		}
	}

	public void setActing(boolean b) {
		isActing = b;
	}

	public boolean isActing() {
		return isActing;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getHealth () {
		return health;
	}
	
	public boolean shouldDisplay() {
		return display;
	}

	public void actionPerformed(InputHandler input) {
		if (input.esc.gotPressed()) {
			gameOver = false;
			game.showGui(new GuiPause(game, Game.WIDTH, Game.HEIGHT));
		}
	}
}
