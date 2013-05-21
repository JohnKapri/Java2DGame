package game.level;

import game.Game;
import game.Game.DebugLevel;
import game.entity.Entity;
import game.gfx.Screen;
import game.level.tile.Tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Level {

	private byte[] tiles;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();

	private String filePath;
	private BufferedImage image;

	private static Random rand = new Random();

	public Level() {
	}

	public void tick() {
		/*for (Entity e : entities) {
			if (e != null)
				e.tick();
		}*/
		for(int i = 0; i < entities.size(); i++) {
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
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}
		}
	}

	public void renderEntities(Screen screen) {
		for (int renderLayer = 1; renderLayer <= 3; renderLayer++) {
			for (Entity e : entities) {
				if (e.gerRenderLayer() == renderLayer) {
					e.render(screen);
				}
			}
		}
	}

	public void loadLevelFromFile(String path) {
		this.filePath = path;
		try {
			this.image = ImageIO
					.read(Level.class.getResourceAsStream(filePath));
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new byte[this.width * this.height];
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveLevelToFile() {
		try {
			ImageIO.write(image, "png",
					new File(Level.class.getResource(filePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateTileSheet(int x, int y, Tile newTile) {
		this.tiles[x + y * width] = newTile.getId();
		this.image.setRGB(x, y, newTile.getLevelColor());
	}

	private void loadTiles() {
		long time = System.currentTimeMillis();

		int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0,
				width);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tileCheck: for (Tile t : Tile.tiles) {
					if (t != null
							&& t.getLevelColor() == tileColors[x + y * width]) {
						if (t.isParent() && rand.nextInt(100) < 8) {
							this.tiles[x + y * width] = t.getChildren()[rand
									.nextInt(t.getChildren().length)];
						} else {
							this.tiles[x + y * width] = t.getId();
						}
						break tileCheck;
					}
				}
			}
		}

		Game.debug(DebugLevel.INFO,
				"Level loaded in " + (System.currentTimeMillis() - time));
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
}
