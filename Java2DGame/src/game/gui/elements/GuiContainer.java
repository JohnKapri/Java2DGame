package game.gui.elements;

import game.item.Item;
import game.item.ItemSlot;

public class GuiContainer{

	protected int rows;
	protected int columns;
	
	private ItemSlot[] slots;
	
	public GuiContainer(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		slots = new ItemSlot[rows * columns];
	}
	
	public boolean addItemSlot(ItemSlot slot) {
		for(ItemSlot s : slots) {
			if(s == null) {
				s = slot;
				return true;
			}
		}
		return false;
	}
	
	public boolean addSlotAt(ItemSlot slot, int row, int column) {
		if(slots[row * columns + column] == null) {
			slots[row * columns + column] = slot;
			return true;
		}
		return false;
	}

	public ItemSlot getSlot(int id) {
		return slots[id];
	}
	
	public Item getItemInSlot(int id) {
		return slots[id].getItem();
	}
}
