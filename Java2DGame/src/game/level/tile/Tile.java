package game.level.tile;

import game.entity.Player;
import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

public class Tile {

	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new Tile(0, 0, 0, Colors.get(005, -1, -1, 555), 0xFF000000).setSolid(true);
	public static final Tile STONE = new Tile(1, 1, 0, Colors.get(-1, 333, 444, -1), 0xFF555555).setSolid(true);
	public static final Tile GRASS_FLOWER_WHITE = new Tile(8, 5, 0, Colors.get(-1, 541, 141, 554), 0xFF);
	public static final Tile GRASS_FLOWER_RED = new Tile(9, 5, 0, Colors.get(-1, 555, 141, 510), 0xFF);
	public static final Tile GRASS = new Tile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00).setIsParent(true).setCildren(new byte[] {Tile.GRASS_FLOWER_WHITE.getId(), Tile.GRASS_FLOWER_RED.getId()});
	public static final Tile SPIKES = new Tile(3, 3, 0, Colors.get(-1, 333, 444, -1), 0xFFFF8800).setHarmfull(true);
	public static final Tile WATER = new AnimatedTile(4, new int [][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 400).setFriction(2);
	public static final Tile SWAMP = new AnimatedTile(5, new int [][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 132, 143, -1), 0xFF008080, 800).setFriction(3);
	public static final Tile SAND = new Tile(6, 2, 0, Colors.get(-1, 541, 551, -1), 0xFFFFFF00);
	public static final Tile DIRT = new Tile(7, 2, 0, Colors.get(-1, 210, 321, -1), 0xFF805030);
	
	protected byte id;
	protected int tileId;
	protected int tileColor;
	protected boolean solid;
	protected boolean emitter;
	private int levelColor;
	protected int friction = 1;
	protected boolean harmfull = false;
	protected boolean parent;
	protected byte[] children;
	
	public Tile(int id, int x, int y, int tileColor, int levelColor) {
		this.id = (byte)id;
		if(tiles[id] != null) throw new RuntimeException("Duplicant tile id at " + id + "! Immediatly smash your display!");
		this.levelColor = levelColor;
		this.tileId = x + y;
		this.tileColor = tileColor;
		tiles[id] = this;
	}
	
	public byte getId()
	{
		return id;
	}
	
	protected Tile setFriction(int i) {
		this.friction = i;
		return this;
	}
	
	protected Tile setEmitter(boolean b) {
		this.emitter = b;
		return this;
	}
	
	protected Tile setSolid(boolean b) {
		this.solid = b;
		return this;
	}
	
	protected Tile setHarmfull(boolean b) {
		this.harmfull = b;
		return this;
	}
	
	protected Tile setIsParent(boolean b) {
		this.parent = true;
		return this;
	}
	
	protected Tile setCildren(byte[] children) {
		this.children = children;
		return this;
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
	
	public boolean isHarmfull () {
		return harmfull;
	}
	
	public boolean isParent() {
		return parent;
	}
	
	public byte[] getChildren() {
		return children;
	}
	
	public void applyPlayerModifier(Player player) {
		player.friction = friction;
		if(harmfull) {
			player.hurt();
		}
	}
	
	public void tick() {
		
	}

	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileId, tileColor, 0x00, 1);
	}
}
