package game.gui;

import game.Game;
import game.InputHandler.InputEvent;
import game.gui.elements.ChooseList;
import game.gui.elements.FontRenderer;

public class GuiPause extends Gui{

	private ChooseList list;
	private String splash = "Grave Robber Test v1.0";
	
	public GuiPause(Game game, int width, int height) {
		super(game, width, height);
		this.pauseGame = true;
		list = new ChooseList(0, this);
		list.addOption(list.new Option(0, "Resume"));
		list.addOption(list.new Option(1, "Save"));
		list.addOption(list.new Option(2, "Options"));
		list.addOption(list.new Option(3, "Exit"));
	}

	public void actionPerformed(InputEvent event) {
		list.actionPerformed(event);
	}

	public void render() {
		this.drawDefaultBackground();
		FontRenderer.drawCenteredString("Paused", this, width / 2 + 2, 5, 222, 2);
		FontRenderer.drawString(splash, this, 2, height - 10, 000, 1);
		list.render(this, 20, 30, 555);
	}

	public void tick(int ticks) {
		
	}

	public void guiActionPerformed(int elementId, int action) {
		if(elementId == list.getId()) {
			switch (action) {
			case 0 :
				closeGui();
				break;
			case 1:
				game.world.saveToFile();
				closeGui();
				break;
			case 2:
				splash = "Nope! Still no options.";
				break;
			case 3:
				System.out.println("The game has been quit!");
				closeGui();
				System.exit(0);
				break;
			}
		}
	}

}
