package game.gui;

import game.Game;
import game.InputHandler.InputEvent;
import game.gui.elements.ChooseList;
import game.gui.elements.FontRenderer;

public class GuiDead extends Gui {

	int selectedEntry = 0;
	ChooseList list;

	public GuiDead(Game game) {
		super(game, Game.WIDTH, Game.HEIGHT);
		setTint(0.5D, 0.2D, 0.2D);
		this.pauseGame = true;
		list = new ChooseList(0, this);
		list.addOption(list.new Option(0, "Retry"));
		list.addOption(list.new Option(1, "Main Menu"));
		list.addOption(list.new Option(2, "Exit"));
	}

	public void render() {
		this.drawDefaultBackground();
		this.drawRect(0, 0, this.width, this.height, 0xFF0000);
		FontRenderer.drawCenteredString("YOU DIED!", this, width / 2 + 2, 20,
				500, 2);
		list.renderCentered(this, width / 2, height / 2, 444);
	}

	public void tick(int ticks) {

	}

	public void actionPerformed(InputEvent event) {
		list.actionPerformed(event);
	}

	public void guiActionPerformed(int elementId, int action) {
		if (elementId == list.getId()) {
			switch (action) {
			case 0:
				System.out.println("Reload");
				closeGui();
				break;
			case 1:
				System.out.println("Main Menu");
				closeGui();
				game.showGui(new GuiMainMenu(game, Game.WIDTH, Game.HEIGHT));
				break;
			case 2:
				System.out.println("The game has been quit!");
				System.exit(0);
				break;
			}
		}
	}
}
