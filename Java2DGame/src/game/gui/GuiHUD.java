package game.gui;

import game.Game;
import game.InputHandler;
import game.entity.Player;
import game.gfx.Colors;
import game.gui.elements.GuiRenderer;

public class GuiHUD extends Gui{
	
	private Player player;

	public GuiHUD(Game game, Player player, int width, int height) {
		super(game, width, height);
		this.player = player;
	}

	public void actionPerformed(InputHandler input) {
		
	}

	public void render() {
		for (int i = 0; i < player.getMaxHealth(); i++) {
			int color;
			if (i < player.getHealth()) {
				color = Colors.get(-1, -1, 400, 400);
				if (!player.shouldDisplay())
					color = Colors.get(-1, -1, 555, 400);
			} else {
				color = Colors.get(-1, -1, 222, 222);
				if (!player.shouldDisplay())
					color = Colors.get(-1, -1, 555, 222);
			}

			GuiRenderer.render(this, font, 2 + i * 16, 2, 32, color,
					0x00, 2);
		}
	}

	public void tick(int ticks) {
		
	}

	public void guiActionPerformed(int elementId, int action) {
		
	}
}
