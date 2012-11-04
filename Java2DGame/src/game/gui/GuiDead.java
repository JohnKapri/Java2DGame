package game.gui;

import game.Game;
import game.InputHandler;
import game.entity.Player;
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

	public void actionPerformed(InputHandler input) {
		list.actionPerformed(input);
	}

	public void guiActionPerformed(int elementId, int action) {
		if (elementId == list.getId()) {
			switch (action) {
			case 0:
				System.out.println("Retry");
				game.player = null;
				game.level = null;
				game.setLevel("/levels/test3.png");
				game.player = new Player(game, game.level, 30, 30, game.input);
				game.level.addEntity(game.player);
				closeGui();
				break;
			case 1:
				System.out.println("Main Menu");
				game.player = null;
				game.level = null;
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
