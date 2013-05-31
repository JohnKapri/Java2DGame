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
import game.level.GlobalBounds;
import game.level.Level;
import game.level.LocalBounds;
import game.level.tile.Tile;

public class Player extends Mob implements GameActionListener {

	private InputHandler input;
	private Level level;
	private Game game;
	private LocalBounds bounds;
	private int health;
	private int maxHealth = 5;
	private int color = Colors.get(-1, 222, 145, 543);
	private int hurtTime = 0;
	private boolean display = true;
	private int walkAnimStat = 0;
	private boolean canHandle = false;
	private boolean isActing = false;
	private boolean showHeart = false;
	private int heartDisplayTime = 0;
	private Entity performActionOn = null;

	public Player(Game game, Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		input.addListener(this);
		this.level = level;
		this.game = game;
		this.bounds = new LocalBounds(8, 4, 4, 12);
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
		super.tick();

		if (hurtTime % 3 == 0) {
			display = !display;
		}
		if (hurtTime == 0) {
			display = true;
		}
		if (hurtTime > 0) {
			hurtTime--;
		}

		if (numSteps % 10 == 0 && isMoving && tickCount % friction == 0) {
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

		if (getTileUnder().getId() == Tile.WATER.getId()
				|| getTileUnder().getId() == Tile.SWAMP.getId()) {
			this.isSwimming = true;
		} else {
			if (isSwimming) {
				isSwimming = false;
			}
		}

		getTileUnder().applyPlayerModifier(this);

		if (getTileUnder().getId() == Tile.SPIKES.getId()) {
			hurt();
		}

		if (health <= 0) {
			color = Colors.get(-1, 222, 145, 232);
		}

		if (heartDisplayTime > 0) {
			heartDisplayTime--;
			if (heartDisplayTime % 5 == 0) {
				showHeart = !showHeart;
			}
		}
	}

	public void render(Screen screen) {

		int xTile = (movingDir * 2 + walkAnimStat) * 2;
		int yTile = 28;

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		if (isSwimming) {
			int waterColor = 0;
			yOffset += 4;
			if (getTileUnder().getId() == Tile.WATER.getId()) {
				if (tickCount % 60 < 15) {
					waterColor = Colors.get(-1, -1, 225, -1);
				} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
					yOffset -= 1;
					waterColor = Colors.get(-1, 225, 115, -1);
				} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
					waterColor = Colors.get(-1, 115, -1, 225);
				} else {
					yOffset -= 1;
					waterColor = Colors.get(-1, 225, 115, -1);
				}
			}
			if (getTileUnder().getId() == Tile.SWAMP.getId()) {
				if (tickCount % 60 < 15) {
					waterColor = Colors.get(-1, -1, 254, -1);
				} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
					yOffset -= 1;
					waterColor = Colors.get(-1, 254, 243, -1);
				} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
					waterColor = Colors.get(-1, 243, -1, 254);
				} else {
					yOffset -= 1;
					waterColor = Colors.get(-1, 254, 243, -1);
				}
			}
			screen.render(xOffset, yOffset + 3, 5 * 32 + 3, waterColor, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 5 * 32 + 3, waterColor,
					0x01, 1);
		}

		if (display) {
			screen.render(xOffset, yOffset, xTile + yTile * 32, color, 0x00,
					scale);
			screen.render(xOffset + modifier, yOffset,
					(xTile + 1) + yTile * 32, color, 0x00, scale);
			if (!isSwimming) {
				screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1)
						* 32, color, 0x00, scale);
				screen.render(xOffset + modifier, yOffset + modifier,
						(xTile + 1) + (yTile + 1) * 32, color, 0x00, scale);
			}
		}

		if (canHandle) {
			screen.render(x, y - 18, 40, Colors.get(-1, 500, 440, 550), 0x00, 1);
		}

		if (showHeart) {
			screen.render(x, y - 18, 32, Colors.get(-1, -1, 300, 510), 0x00, 1);
		}
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

	public int getHealth() {
		return health;
	}
	
	public GlobalBounds getGlobalBounds() {
		return new GlobalBounds(x, y, bounds);
	}

	public boolean shouldDisplay() {
		return display;
	}

	public void actionPerformed(InputHandler input) {
		if (input.esc.gotPressed()) {
			game.showGui(new GuiPause(game, Game.WIDTH, Game.HEIGHT));
		}
	}

	public boolean heal(int i, Entity e) {
		if (health + i <= maxHealth) {
			health += i;
			if(e instanceof Heart) {
				heartDisplayTime = 40;
			}
			return true;
		}
		return false;
	}
}
