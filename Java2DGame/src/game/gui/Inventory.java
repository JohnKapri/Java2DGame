package game.gui;


import game.Game;
import game.Tag;
import game.item.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Inventory {

	public Item[] contents;

	public int id;
	public String name;
	public int size;

	public Inventory(int id, String name, int size) {
		contents = new Item[size];
		this.id = id;
		this.name = name;
		this.size = size;
	}

	public Item getItem(int index) {
		return contents[index];
	}

	public boolean addItem(Item item) {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null) {
				contents[i] = item;
				return true;
			}
		}
		return true;
	}

	public boolean hasItem(Item item) {
		for (Item i : contents) {
			if (i == item) {
				return true;
			}
		}
		return false;
	}

	public boolean consume(Item item) {
		for (Item i : contents) {
			if (i == item) {
				i = null;
				return true;
			}
		}
		return false;
	}

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

	public static void saveInventory(Inventory inventory) {
		try {
			File f = new File(Game.homeDir + "inventory_" + inventory.id
					+ ".dat");
			Game.debug(Game.DebugLevel.INFO, "Writing inventory \""
					+ inventory.name + "\" to file "
					+ f.getAbsoluteFile().toString());
			if (!f.exists()) {
				f.createNewFile();
				Game.debug(Game.DebugLevel.INFO, "File created!");
			}
			Tag tag = new Tag(Tag.Type.TAG_Compound, "INVENTORY", new Tag[] {
					new Tag(Tag.Type.TAG_Int, "ID", inventory.id),
					new Tag(Tag.Type.TAG_String, "NAME", inventory.name),
					new Tag(Tag.Type.TAG_Int, "SIZE", inventory.size),
					new Tag(Tag.Type.TAG_Int_Array, "CONTENTS",
							Inventory.getIdList(inventory.contents)),
					new Tag(Tag.Type.TAG_End, null, null) });
			tag.writeTo(new FileOutputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Inventory loadInventory(String fileName) {
		try {
			File f = new File(Game.homeDir + fileName);
			Game.debug(Game.DebugLevel.INFO, "Loading from file "
					+ f.getAbsoluteFile().toString());
			if (!f.exists()) {
				Game.debug(Game.DebugLevel.WARNING,
						"File not found! Skipping...");
				return null;
			}
			Tag tag = Tag.readFrom(new FileInputStream(f));
			Game.debug(Game.DebugLevel.INFO, "Tag name is: " + tag.getName());
			tag.print();
			int id = (int) tag.findTagByName("ID").getValue();
			String name = tag.findTagByName("NAME").getValue().toString();
			int size = (int) tag.findTagByName("SIZE").getValue();
			Inventory inv = new Inventory(id, name, size);
			inv.contents = Inventory.getItemList((int[]) tag.findTagByName(
					"CONTENTS").getValue());
			return inv;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
