package game.entity;

import game.InputHandler;
import game.gfx.Colors;
import game.gfx.Font;
import game.gfx.Screen;
import game.level.Level;

public class Chest extends Entity{

	int scale = 1;
	boolean showInv = false;
	InputHandler input;
	Player actingPlayer;
	
	public Chest(Level level, int x, int y, InputHandler input) {
		super(level, x, y);
		this.input = input;
	}

	public void tick() {

	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 26;
		
		int color = Colors.get(-1, 321, 111, -1);
		
		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

			screen.render(xOffset, yOffset, xTile + yTile * 32, color, 0x00,
					scale);
			screen.render(xOffset + modifier, yOffset,
					(xTile + 1) + yTile * 32, color, 0x00, scale);

			screen.render(xOffset, yOffset + modifier,
					xTile + (yTile + 1) * 32, color, 0x00, scale);
			screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
					+ (yTile + 1) * 32, color, 0x00, scale);
			
		if(showInv) {
			Font.drawString("Chest", screen, 1, screen.xOffset + 2, 32 + screen.yOffset, Colors.get(-1, -1, -1, 000), 2);
			Font.drawString("Nothing in here!", screen, 1, screen.xOffset + 2, 48 + screen.yOffset, Colors.get(-1, -1, -1, 000), 1);
			Font.drawString("Press Esc to close", screen, 1, screen.xOffset + 2, 56 + screen.yOffset, Colors.get(-1, -1, -1, 000), 1);
		}
	}
	
	@Override
	public void action(Player player) {
		actingPlayer = player;
		actingPlayer.setActing(true);
		showInv = true;
	}
}
