package game.entity;

import game.Tag;
import game.gfx.Screen;
import game.item.Item;
import game.level.Level;

public class EntityItem extends Entity{
	
	private Item item;

	public EntityItem(Level world, Item item, int x2, int y2) {
		super(world, x2, y2);
		this.item = item;
	}

	@Override
	public void render(Screen screen) {
		screen.render(x, y, item.getSpriteIndex(), item.getSpriteColor(), 0x00,
				1);
	}
	
	@Override
	public Tag saveToNBT(Tag notused) {
		Tag tag = new Tag(Tag.Type.TAG_Compound, "ENTITY_" + id, new Tag[1]);
		super.saveToNBT(tag);
		tag.addTag(new Tag(Tag.Type.TAG_Byte, "ITEM_ID", item.getId()));
		return tag;
	}

	@Override
	public void loadFromNBT(Tag tag) {
		super.loadFromNBT(tag);
		this.item = Item.items[(int) tag.findTagByName("HEALTH").getValue()];
	}
}
