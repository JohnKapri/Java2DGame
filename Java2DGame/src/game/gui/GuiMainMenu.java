package game.gui;

import game.Game;
import game.InputHandler.InputEvent;
import game.gui.elements.ChooseList;
import game.gui.elements.FontRenderer;
import game.level.World;

public class GuiMainMenu extends Gui {

	public ChooseList list;
	private String splash = "Grave Robber Test v1.0";

	public GuiMainMenu(Game game, int width, int height) {
		super(game, width, height);
		this.pauseGame = true;
		list = new ChooseList(0, this);
		list.setMaximumDisplayed(10);
		list.addOption(list.new Option(0, "Create"));
		list.addOption(list.new Option(1, "Load"));
		list.addOption(list.new Option(2, "Options"));
		list.addOption(list.new Option(3, "Exit"));
	}

	public void render() {
		this.drawDefaultBackground();
		FontRenderer.drawCenteredString("Main Menu", this, width / 2 + 1, 5,
				225, 2);
		list.render(this, 10, 30, 225);
		FontRenderer.drawString(splash, this, 2, height - 10, 000, 1);
	}

	public void tick(int ticks) {

	}

	public void guiActionPerformed(int elementId, int action) {
		if (elementId == list.getId()) {
			switch (action) {
			case 0:
				game.world = new World(game);
				closeGui();
				break;
			case 1:
				game.showGui(new GuiLoad(game, Game.WIDTH, Game.HEIGHT));
				closeGui();
				break;
			case 2:
				splash = "No option, yet!";
				break;
			case 4:
				System.out.println("The game has been quit!");
				System.exit(0);
				break;
			}
		}
	}

	public void actionPerformed(InputEvent event) {
		list.actionPerformed(event);
	}
}
