package game.entity;

import game.Tag;
import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;
import game.level.LocalBounds;

public class Heart extends Entity {

	private int animState;
	private boolean animDecr;

	public Heart(Level level, int x, int y) {
		super(level, x, y);
	}

	public Heart(Level level, Tag tag) {
		super(level, tag);
		this.bounds = new LocalBounds(8, 8);
	}

	@Override
	public void tick() {
		super.tick();

		if (tickCount % 6 == 0) {
			if (animDecr) {
				animState--;
			} else {
				animState++;
			}
			if (animState >= 3 || animState <= 0) {
				animDecr = !animDecr;
			}
		}
	}

	@Override
	public void onCollideWithPlayer(Player player) {
		if (player.heal(1, this)) {
			level.removeEntity(this);
		}
	}

	@Override
	public void render(Screen screen) {
		screen.render(x, y, 32 + animState, Colors.get(-1, -1, 300, 510), 0x00,
				1);
	}
}
