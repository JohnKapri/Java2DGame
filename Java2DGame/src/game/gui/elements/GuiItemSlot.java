package game.gui.elements;

import game.InputHandler.InputEvent;
import game.gui.Gui;
import game.item.Item;

public class GuiItemSlot extends GuiElement {

	private GuiContainer container;
	private int slotIndex;

	public GuiItemSlot(int elementId, Gui gui, GuiContainer container,
			int slotIndex) {
		super(elementId, gui);
		this.container = container;
		this.slotIndex = slotIndex;
	}

	@Override
	public void actionPerformed(InputEvent event) {

	}
	
	public void setSlotIndex(int i) {
		slotIndex = i;
	}

	@Override
	public void render(Gui gui, int x, int y, int color) {
		Item item = container.getItemInSlot(this.slotIndex);
		if (item != null) {
			GuiRenderer.render(gui, gui.font, x, y, item.getSpriteIndex(),
					item.getSpriteColor(), 0x00, 1);
		}
	}
}
