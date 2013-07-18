package game.entity;

import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

public class NPC extends Mob{

	public NPC(Level level, int x, int y) {
		super(level, "Hans", x, y, 1);
		
	}

	@Override
	public boolean hasCollided(int xa, int ya) {
		return false;
	}

	@Override
	public void render(Screen screen) {
		int waterColor = Colors.get(-1, 555, 555, 555);
		int walkAnimStat = 0;
		int xTile = (movingDir * 2 + walkAnimStat) * 2;
		int yTile = 28;

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;
		
		screen.render(xOffset, yOffset + 3, 5 * 32 + 3, waterColor, 0x00, 1);
		screen.render(xOffset + 8, yOffset + 3, 5 * 32 + 3, waterColor,
				0x01, 1);

	}
}
