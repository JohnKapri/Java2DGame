package game.level;

import game.Tag;
import game.entity.Entity;

public class Region implements NBTCapable {

	private int id;
	private byte[] tiles = new byte[32 * 32];
	private byte[] meta = new byte[32 * 32];
	private byte[] data = new byte[32 * 32];
	private Entity[] entities = new Entity[32];

	public Region(int id) {
		this.id = id;
	}
	
	public int getEntityCount() {
		int i = 0;
		for(int j = 0; j < entities.length; j++) {
			if(entities[j] != null) {
				i++;
			}
		}
		return i;
	}
	
	public String getNBTName() {
		return new StringBuilder()
				.append("REGION_").append(id).toString();
	}

	@Override
	public void loadFromNBT(Tag tag) {
		tag = tag.findTagByName(getNBTName());
		if(this.id != (int) tag.findTagByName("ID").getValue()) {
			return;
		}
		this.tiles = (byte[]) tag.findTagByName("TILES").getValue();
		this.meta = (byte[]) tag.findTagByName("META").getValue();
		this.data = (byte[]) tag.findTagByName("DATA").getValue();
	}
	
	private void loadEntitiesFromNBT(Tag tag) {
		int entityCount = (int) tag.findTagByName("EINTITY_COUNT").getValue();
		for(int i = 0; i < entityCount; i++) {
			//entities[i] = EntityLoader.
		}
	}

	@Override
	public Tag saveToNBT(Tag notused) {
		Tag tag = new Tag(Tag.Type.TAG_Compound, getNBTName(), new Tag[] {});
		tag.addTag(new Tag(Tag.Type.TAG_Int, "ID", id));
		tag.addTag(new Tag(Tag.Type.TAG_Byte_Array, "TILES", tiles));
		tag.addTag(new Tag(Tag.Type.TAG_Byte_Array, "META", meta));
		tag.addTag(new Tag(Tag.Type.TAG_Byte_Array, "DATA", data));
		tag.addTag(new Tag(Tag.Type.TAG_Int, "ENTITY_COUNT", getEntityCount()));
		tag.addTag(saveEntitiesToNBT(null));
		tag.addTag(new Tag(Tag.Type.TAG_End, null, null));
		return tag;
	}

	private Tag saveEntitiesToNBT(Tag notused) {
		Tag tag = new Tag("ENTITIES", Tag.Type.TAG_List);
		for (int i = 0; i < entities.length; i++) {
			if (entities[i] != null) {
				tag.addTag(entities[i].saveToNBT(null));
			}
		}
		tag.addTag(new Tag(Tag.Type.TAG_End, null, null));
		return tag;
	}
}
