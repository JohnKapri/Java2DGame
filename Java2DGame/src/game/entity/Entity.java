package game.entity;

import game.Tag;
import game.gfx.Screen;
import game.level.GlobalBounds;
import game.level.Level;
import game.level.LocalBounds;
import game.level.NBTCapable;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity implements NBTCapable {

	public int x, y;
	protected int id;
	protected int renderLayer = 1;
	protected Level level;
	protected int tickCount;
	protected LocalBounds bounds = new LocalBounds(8, 8);

	public Entity(Level level, int x2, int y2) {
		if (level != null) {
			this.level = level;
			this.id = level.getUniqueEntityId();
		} else {
			this.id = 0;
		}
		this.x = x2;
		this.y = y2;
	}

	public Entity(Level level2, Tag nbt) {
		this.level = level2;
		this.loadFromNBT(nbt);
	}

	public void tick() {
		tickCount++;
	}

	public abstract void render(Screen screen);

	/**
	 * Returns the layer at which the Entity should be rendered. 0 is bottom 3
	 * is top.
	 * 
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
	
	public void onCollide(Entity e1) {
		
	}
	
	public GlobalBounds getGlobalBounds() {
		return new GlobalBounds(x, y, bounds);
	}
	
	public LocalBounds getLocalBounds() {
		return bounds;
	}
	
	public boolean doesCollideWith(Entity e2) {
		return doEntitiesCollide(this, e2);
	}
	
	public static boolean doEntitiesCollide(Entity e1, Entity e2) {
		List<Shape> e1mask = new ArrayList<Shape>();
		List<Shape> e2mask = new ArrayList<Shape>();
		
		e1mask.add(new Rectangle2D.Float(0, 0, e1.bounds.width, e1.bounds.height));
		e2mask.add(new Rectangle2D.Float(0, 0, e2.bounds.width, e2.bounds.height));
		
		for (Shape s1 : e1mask) {
			Area a = new Area(s1);
			AffineTransform at = new AffineTransform();
			at.translate(e1.x, e1.y);
			a.transform(at);
			for(Shape s2 : e2mask) {
				Area a1 = new Area(s2);
				AffineTransform at1 = new AffineTransform();
				at1.translate(e2.x, e2.y);
				a1.transform(at1);
				if(a.getBounds2D().intersects(a1.getBounds2D())) {
					return true;
				}
			}
		}
		return false;
	}

	public Tag saveToNBT(Tag tag) {
		tag.addTag(new Tag(Tag.Type.TAG_Int, "ID", this.id));
		tag.addTag(new Tag(Tag.Type.TAG_Int, "POS_X", this.x));
		tag.addTag(new Tag(Tag.Type.TAG_Int, "POS_Y", this.y));
		tag.addTag(new Tag(Tag.Type.TAG_Int, "TICKS", this.tickCount));
		return tag;
	}

	public void loadFromNBT(Tag tag) {
		this.id = (int) tag.findTagByName("ID").getValue();
		this.x = (int) tag.findTagByName("POS_X").getValue();
		this.y = (int) tag.findTagByName("POS_Y").getValue();
		this.tickCount = (int) tag.findTagByName("TICKS").getValue();
	}
}
