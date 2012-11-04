package game.gui.elements;

import game.InputHandler.GameActionListener;
import game.gui.Gui;

public abstract class GuiElement implements GameActionListener{
	
	protected Gui parent;
	protected int id;

	public GuiElement(int elementId, Gui gui) {
		this.parent = gui;
	}
		
	public abstract void render(Gui gui, int x, int y, int color);
}
