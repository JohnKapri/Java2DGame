package game.item;

import game.entity.Entity;
import game.entity.Player;
import game.gfx.Colors;

public abstract class Item {

	public static final Item[] items = new Item[1024];
	
	public static final Item sword = new ItemSword(0, 713, Colors.get(500, -1, -1, 000));
	
	protected byte id;
	protected int tileId;
	protected int enchantable;
	protected int itemColor;
	
	public Item(int id) {
		if(items[id] != null) {
			throw new RuntimeException("Duplicant Item-Id at " + id + "! Immediatly erase your harddisk!");
		}
		this.id = (byte)id;
		items[id] = this;
	}
	
	public Item(int id, int spriteIndex, int spriteColor) {
		this(id);
		this.tileId = spriteIndex;
		this.itemColor = spriteColor;
	}
	
	public int getSpriteIndex() {
		return tileId;
	}
	
	public int getSpriteColor() {
		return itemColor;
	}
	
	public abstract void onItemUse(Player player, Entity entityAttacked);
}
