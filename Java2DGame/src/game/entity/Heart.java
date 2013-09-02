package game.entity;

import game.Tag;
import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

public class Heart extends Entity{

	private int animState;
	private boolean animDecr;

	public Heart(Level level, int x, int y) {
		super(level, x, y);
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

	public Tag saveToNBT(Tag tag) {
		tag.addTag(new Tag(Tag.Type.TAG_End, null, null));
		return tag;
	}
}
