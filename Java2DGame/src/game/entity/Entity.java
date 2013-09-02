package game.entity;

import game.Tag;
import game.gfx.Screen;
import game.level.Level;
import game.level.LocalBounds;
import game.level.NBTCapable;

public abstract class Entity implements NBTCapable{
	
	public int x, y;
	protected int id;
	protected int renderLayer = 1;
	protected Level level;
	protected int tickCount;
	protected LocalBounds bounds;

	public Entity(Level world, int x2, int y2) {
		this.level = world;
		this.x = x2;
		this.y = y2;
	}
	
	public Entity(Level level2, Tag nbt) {
		this.level = level2;
		this.loadFromNBT(nbt);
	}

//	public Entity(World w, Region r, Tag tag) {
//		
//		loadFromNBT(tag);
//	}

	public void tick() {
		tickCount++;
	}
	
	public abstract void render(Screen screen);
	
	/**
	 * Returns the layer at which the Entity should be rendered. 0 is bottom 3 is top.
	 * @return
	 */
	public int getRenderLayer() {
		return renderLayer;
	}
	
	public int getId() {
		return id;
	}
	
	public void action(Player player) {
		
	}

	public void atEntityRemoved(Level l) {
		
	}
	
	public void onCollideWithPlayer(Player p) {
		
	}
	
	public Tag saveToNBT(Tag tag) {
		tag.addTag(new Tag(Tag.Type.TAG_Int, "POS_X", this.x));
		tag.addTag(new Tag(Tag.Type.TAG_Int, "POS_Y", this.y));
		tag.addTag(new Tag(Tag.Type.TAG_Int, "TICKS", this.tickCount));
		return tag;
	}
	
	public void loadFromNBT(Tag tag) {
		this.x = (int) tag.findTagByName("POS_X").getValue();
		this.y = (int)tag.findTagByName("POS_Y").getValue();
		this.tickCount = (int) tag.findTagByName("TICKS").getValue();
	}
}
