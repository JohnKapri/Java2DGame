package game.item;

import game.entity.Entity;
import game.entity.Player;
import game.gfx.Colors;

public class Item {

	public static final Item[] items = new Item[1024];

	public static final Item SWORD = new ItemSword(1, "SWORD", 713, Colors.get(
			500, -1, -1, 000));
	public static final Item HEART = new Item(2, "HEART", 32, Colors.get(-1, -1, 300, 510)).setVital(true);

	protected int id;
	protected String name = "unnamed";
	protected int tileId;
	protected int enchantable;
	protected int itemColor;
	protected boolean vital;

	private Item(int id) {
		if (items[id] != null) {
			throw new RuntimeException("Duplicant Item-Id at " + id
					+ "! Immediatly erase your harddisk!");
		}
		this.id = id;
		items[id] = this;
	}
	
	protected Item setVital(boolean b) {
		this.vital = b;
		return this;
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
	
	public boolean isVitalItem() {
		return vital;
	}

	public int getId() {
		return id;
	}

	public void onItemUse(Player player, Entity entityAttacked) {
		
	}
}
