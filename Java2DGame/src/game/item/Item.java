package game.item;

import game.entity.Entity;
import game.entity.Player;
import game.gfx.Colors;

public abstract class Item {

	public static final Item[] items = new Item[1024];

	public static final Item SWORD = new ItemSword(1, "SWORD", 713, Colors.get(
			500, -1, -1, 000));

	protected int id;
	protected String name = "unnamed";
	protected int tileId;
	protected int enchantable;
	protected int itemColor;

	public Item(int id) {
		if (items[id] != null) {
			throw new RuntimeException("Duplicant Item-Id at " + id
					+ "! Immediatly erase your harddisk!");
		}
		this.id = id;
		items[id] = this;
	}

	public Item(int id, String name, int spriteIndex, int spriteColor) {
		this(id);
		this.name = name;
		this.tileId = spriteIndex;
		this.itemColor = spriteColor;
	}

	public int getSpriteIndex() {
		return tileId;
	}

	public String getName() {
		return name;
	}

	public int getSpriteColor() {
		return itemColor;
	}

	public int getId() {
		return id;
	}

	public abstract void onItemUse(Player player, Entity entityAttacked);
}
