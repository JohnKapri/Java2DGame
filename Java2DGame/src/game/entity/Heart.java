package game.entity;

import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

public class Heart extends Entity {

	private int animState;
	private boolean animDecr;

	public Heart(Level level, int x, int y) {
		super(level);
		this.x = x;
		this.y = y;
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
			if(animState >= 3 || animState <=0) {
				animDecr = !animDecr;
			}
		}
	}

	@Override
	public void render(Screen screen) {
		screen.render(x, y, 32 + animState, Colors.get(-1, -1, 300, 510), 0x00, 1);
	}

}
