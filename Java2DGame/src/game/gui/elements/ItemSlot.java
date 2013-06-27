package game.gui.elements;

import game.item.Item;

public class ItemSlot {

	private Item item;
	
	public ItemSlot() {
		
	}
	
	public void setItem(Item i) {
		this.item = i;
	}
	
	public Item getItem() {
		return item;
	}
}
