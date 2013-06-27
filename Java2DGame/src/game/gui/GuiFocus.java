package game.gui;

import game.Game;
import game.InputHandler.InputEvent;
import game.gui.elements.FontRenderer;

public class GuiFocus extends Gui {

	public GuiFocus(Game game, int width, int height) {
		super(game, width, height);
		this.pauseGame = true;
	}

	@Override
	public void actionPerformed(InputEvent event) {

	}

	@Override
	public void render() {
		this.drawDefaultBackground();
		FontRenderer.drawCenteredString("Focus Lost!", this, width / 2, 50,
				444, 4);
		FontRenderer.drawCenteredString("Click to focus...", this, width / 2,
				80, 555, 1);
	}

	@Override
	public void tick(int ticks) {

	}

	@Override
	public void guiActionPerformed(int elementId, int action) {

	}
}
