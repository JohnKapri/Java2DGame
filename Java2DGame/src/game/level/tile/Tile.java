package game.level.tile;

import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

public abstract class Tile {

	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colors.get(005, -1, -1, 555), 0xFF000000);
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colors.get(-1, 333, 444, -1), 0xFF555555);
	public static final Tile GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00);
	public static final Tile SPIKES = new BasicTile(3, 3, 0, Colors.get(-1, 333, 444, -1), 0xFFFF8800);
	public static final Tile WATER = new AnimatedTile(4, new int [][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 400);
//	public static final Tile ARROW = new AnimatedTile(4, new int [][] {{0, 6}, {1, 6}, {2, 6}, {3, 6}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 20);
	
	protected byte id;
	protected boolean solid;
	protected boolean emitter;
	private int levelColor;
	
	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor) {
		this.id = (byte)id;
		if(tiles[id] != null) throw new RuntimeException("Duplicat tile id at " + id);
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColor = levelColor;
		tiles[id] = this;
	}
	
	public byte getId()
	{
		return id;
	}
	
	public int getLevelColor() {
		return levelColor;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isEmitter() {
		return emitter;
	}
	
	public abstract void tick();

	public abstract void render(Screen screen, Level level, int x, int y);
}
