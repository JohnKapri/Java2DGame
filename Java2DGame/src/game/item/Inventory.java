package game.item;

import game.Game;
import game.Tag;

public class Inventory {

	public Item[] contents;

	public int id;
	public String name;
	public int size;

	/***
	 * Creates a new inventory that can hold a certain amount of items.
	 * @param id The ID of the new inventory.
	 * @param name The name of the inventory. Will be displayed in GUIs.
	 * @param size The amount of items it can hold.
	 */
	public Inventory(int id, String name, int size) {
		contents = new Item[size];
		this.id = id;
		this.name = name;
		this.size = size;
	}

	/**
	 * Get the item at the specified index.
	 * @param index Which item to look for.
	 * @return Item in the slot "index".
	 */
	public Item getItem(int index) {
		return contents[index];
	}

	/**
	 * Add item to the inventory.
	 * @param item The new Item that should be added.
	 * @return Returns "false" if the item couldn't be added because the inventory is already full.
	 */
	public boolean addItem(Item item) {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null) {
				contents[i] = item;
				return true;
			}
		}
		return true;
	}

	/**
	 * Searches the inventory for the specified Item.
	 * @param item The Item to look for.
	 * @return "true" if the Inventory contains the specified Item, "false" otherwise.
	 */
	public boolean hasItem(Item item) {
		for (Item i : contents) {
			if (i == item) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Looks for the specified Item and removes it if possible.
	 * @param item The Item to look for.
	 * @return "true" if the Item was removed, "false" otherwise.
	 */
	public boolean consume(Item item) {
		for (Item i : contents) {
			if (i == item) {
				i = null;
				return true;
			}
		}
		return false;
	}

	/**
	 * Prints the contents of the Inventory to the console.
	 */
	public void print() {
		for (Item i : contents) {
			if (i != null) {
				System.out.print(i.getName() + " ");
			} else {
				System.out.print("EMPTY ");
			}
		}
		System.out.println();
	}

	private static int[] getIdList(Item[] items) {
		int[] list = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				list[i] = items[i].getId();
			}
		}
		return list;
	}

	private static Item[] getItemList(int[] list) {
		Item[] items = new Item[list.length];
		for (int i = 0; i < list.length; i++) {
			items[i] = Item.items[list[i]];
		}
		return items;
	}

	public void saveToNBT(Tag tag) {
		tag.addTag(new Tag(Tag.Type.TAG_Compound, "INVENTORY", new Tag[] {
				new Tag(Tag.Type.TAG_Int, "ID", this.id),
				new Tag(Tag.Type.TAG_String, "NAME", this.name),
				new Tag(Tag.Type.TAG_Int, "SIZE", this.size),
				new Tag(Tag.Type.TAG_Int_Array, "CONTENTS", Inventory
						.getIdList(this.contents)),
				new Tag(Tag.Type.TAG_End, null, null) }));
	}

	public void loadFromNBT(Tag tag) {
		tag = tag.findTagByName("INVENTORY");
		Game.debug(Game.DebugLevel.INFO, "Tag name is: " + tag.getName());
		int id = (int) tag.findTagByName("ID").getValue();
		String name = tag.findTagByName("NAME").getValue().toString();
		int size = (int) tag.findTagByName("SIZE").getValue();
		Inventory inv = new Inventory(id, name, size);
		inv.contents = Inventory.getItemList((int[]) tag.findTagByName(
				"CONTENTS").getValue());
	}
}
