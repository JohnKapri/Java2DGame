package game.level;

import game.Game;
import game.Game.DebugLevel;
import game.Tag;
import game.entity.Entity;
import game.gfx.Screen;
import game.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Level implements NBTCapable {

	private byte[] tiles;
	private byte[] meta;
	private byte[] overlay;
	private int width;
	private int height;
	private String name;
	private List<Entity> entities = new ArrayList<Entity>();

	public Level(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.tiles = new byte[width * height];
	}

	public Level(Tag tag) {
		this.name = "LEVEL";
		this.loadFromNBT(tag);
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}

		for (Tile t : Tile.tiles) {
			if (t == null) {
				break;
			}
			t.tick();
		}
	}

	public void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x * y % 10 < 5) {
					tiles[x + y * width] = Tile.GRASS.getId();
				} else {
					tiles[x + y * width] = Tile.STONE.getId();
				}
			}
		}
	}

	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		if (xOffset < 0)
			xOffset = 0;
		if (xOffset > ((width << 3) - screen.width))
			xOffset = (width << 3) - screen.width;
		if (yOffset < 0)
			yOffset = 0;
		if (yOffset > ((height << 3) - screen.height))
			yOffset = (height << 3) - screen.height;

		screen.setOffset(xOffset, yOffset);

		for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
				getTile(x, y).render(screen, this, x * 8, y * 8);
			}
		}
	}

	public void renderEntities(Screen screen) {
		for (int renderLayer = 1; renderLayer <= 3; renderLayer++) {
			for (Entity e : entities) {
				if (e.getRenderLayer() == renderLayer) {
					e.render(screen);
				}
			}
		}
	}

	public boolean removeEntity(Entity entity) {
		entity.atEntityRemoved(this);
		return entities.remove(entity);
	}

	public Entity[] getEntityWithin(int x1, int y1, int x2, int y2) {
		ArrayList<Entity> match = new ArrayList<Entity>();
		for (Entity e : entities) {
			if ((e.x >= x1 && e.x < x2) && (e.y >= y1 && e.y < y2)) {
				match.add(e);
			}
		}
		Entity[] result = new Entity[match.size()];
		int i = 0;
		for (Entity e : match) {
			result[i] = e;
			i++;
		}
		return result;
	}

	public byte[] getTileIdArray() {
		return tiles;
	}

	public void setTiles(byte[] t) {
		if (t.length == width * height) {
			for (int i = 0; i < t.length; i++) {
				tiles[i] = t[i];
			}
		} else {
			Game.debug(DebugLevel.ERROR,
					"Level file corrupted! Failed to load level \"" + name
							+ "\"!");
		}
	}

	public void setTile(byte id, int x, int y) {
		tiles[x + y * width] = id;
	}

	public Tile getTile(int x, int y) {
		if (0 > x || x >= width || 0 > y || y >= height)
			return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];
	}

	public void addEntity(Entity eintity) {
		entities.add(eintity);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	@Override
	public void loadFromNBT(Tag tag) {
		this.name = tag.findTagByName("NAME").getValue().toString();
		this.width = (int) tag.findTagByName("WIDTH").getValue();
		this.height = (int) tag.findTagByName("HEIGHT").getValue();
		this.tiles = (byte[]) tag.findTagByName("TILES").getValue();
		this.meta = (byte[]) tag.findTagByName("META").getValue();
		this.overlay = (byte[]) tag.findTagByName("OVERLAY").getValue();
		if(tiles.length != width * height) {
			Game.debug(Game.DebugLevel.WARNING, "Tile data corrupted!");
			Game.debug(Game.DebugLevel.ERROR, "Error while loading level \"" + name + "\"!");
		}
		if(meta.length != width * height) {
			Game.debug(Game.DebugLevel.WARNING, "Meta data corrupted!");
			Game.debug(Game.DebugLevel.ERROR, "Error while loading level \"" + name + "\"!");
		}
		if(overlay.length != width * height) {
			Game.debug(Game.DebugLevel.WARNING, "Overlay data corrupted!");
			Game.debug(Game.DebugLevel.ERROR, "Error while loading level \"" + name + "\"!");
		}
	}

	@Override
	public Tag saveToNBT(Tag notused) {
		Tag tag = new Tag(Tag.Type.TAG_Compound, name.toUpperCase(),
				new Tag[] {
						new Tag(Tag.Type.TAG_String, "NAME", this.getName()),
						new Tag(Tag.Type.TAG_String, "NEXT_LEVEL", "none"),
						new Tag(Tag.Type.TAG_Int, "WIDTH", this.getWidth()),
						new Tag(Tag.Type.TAG_Int, "HEIGHT", this.getHeight()),
						new Tag(Tag.Type.TAG_Byte_Array, "TILES",
								this.getTileIdArray()),
						new Tag(Tag.Type.TAG_Byte_Array, "META", this.meta),
						new Tag(Tag.Type.TAG_Byte_Array, "OVERLAY",
								this.overlay),
						new Tag(Tag.Type.TAG_End, null, null) });
		return tag;
	}
}
