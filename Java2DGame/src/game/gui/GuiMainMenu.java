package game.gui;

import game.Game;
import game.InputHandler;
import game.entity.Heart;
import game.entity.Player;
import game.gui.elements.ChooseList;
import game.gui.elements.FontRenderer;

public class GuiMainMenu extends Gui {

	public ChooseList list;
	private String splash = "Grave Robber Test v1.0";

	public GuiMainMenu(Game game, int width, int height) {
		super(game, width, height);
		this.pauseGame = true;
		list = new ChooseList(0, this);
		list.setMaximumDisplayed(10);
		list.addOption(list.new Option(0, "Start"));
		list.addOption(list.new Option(1, "Options"));
		list.addOption(list.new Option(2, "Highscore"));
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
				game.setLevel("/levels/tile_test.png");
				game.player = new Player(game, game.level, 32, 32, game.input);
				game.level.addEntity(game.player);
				game.level.addEntity(new Heart(game.level, 25*8, 30*8));
				closeGui();
				break;
			case 1:
				System.out.println("I think there are no options yet...");
				splash = "<Imaginary volume control>";
				break;
			case 2:
				System.out.println("Highscore? There aren't even points.");
				splash = "Highscore? There aren't even points...";
				break;
			case 4:
				System.out.println("The game has been quit!");
				System.exit(0);
				break;
			}
		}
	}

	public void actionPerformed(InputHandler input) {
		list.actionPerformed(input);
	}
}
