package game.entity;

import game.Tag;
import game.level.Level;
import game.level.NBTCapable;
import game.level.tile.Tile;

public abstract class Mob extends Entity implements NBTCapable{

	protected String name;
	protected int speed;
	public int friction;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;
	protected boolean isSwimming = false;

	/**
	 * The type Mob cannot be instanciated due to it being an abstract type.
	 * @param level The Level the Mob is in.
	 * @param name The name of the mob.
	 * @param x The x coordinate at which the Mob will be.
	 * @param y The y coordinate at which the Mob will be.
	 * @param speed The maximum speed of the Mob. Usually this is 1.
	 */
	public Mob(Level level, String name, int x, int y, int speed) {
		super(level, x, y);
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
//	public Mob(World w, Region r, Tag tag) {
//		super(w, r, tag);
//	}

	public Mob(Level level, Tag nbt) {
		super(level, nbt);
		this.loadFromNBT(nbt);
	}

	/**
	 * Handles the movement of the Mob and prevents it from moving to fast. Also sets the direction in which the Mob is moving.
	 * @param xa The difference to the last x coordinate.
	 * @param ya The differende to the last y coordinate.
	 */
	public void move(int xa, int ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;
		if (!hasCollided(xa, ya)) {
			if (ya < 0) {
				movingDir = 0;
			}
			if (ya > 0) {
				movingDir = 1;
			}
			if (xa < 0) {
				movingDir = 2;
			}
			if (xa > 0) {
				movingDir = 3;
			}
			if (tickCount % friction == 0) {
				x += xa * speed;
				y += ya * speed;
			}
		}
	}

	public Tile getTileUnder() {
		return (level.getTile(this.x >> 3, this.y >> 3));
	}

	public abstract boolean hasCollided(int xa, int ya);

	public boolean isSolidTile(int xa, int ya, int x, int y) {
		if (level == null) {
			return false;
		}
		Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		Tile newTile = level.getTile((this.x + x + xa) >> 3,
				(this.y + y + ya) >> 3);
		if (!lastTile.equals(newTile) && newTile.isSolid()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the name of the Mob.
	 * @return Name of the Mob. If not specified, this will be the type of mob, like "Monster", "Villager".
	 */
	public String getName() {
		return name;
	}

	@Override
	public Tag saveToNBT(Tag tag) {
		super.saveToNBT(tag);
		tag.addTag(new Tag(Tag.Type.TAG_String, "NAME", this.name));
		tag.addTag(new Tag(Tag.Type.TAG_Int, "SPEED", this.speed));
		return tag;
	}
	
	@Override
	public void loadFromNBT(Tag tag) {
		super.loadFromNBT(tag);
		this.name = tag.findTagByName("NAME").getValue().toString();
		this.speed = (int) tag.findTagByName("SPEED").getValue();
	}
}
