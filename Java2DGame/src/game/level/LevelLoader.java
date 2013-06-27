package game.level;

import game.Game;
import game.Tag;
import game.Game.DebugLevel;
import game.level.tile.Tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class LevelLoader {

	private static Random rand = new Random();

	public static Level loadLevel(String name) {
		BufferedImage tileSheet = null;
		try {
			tileSheet = ImageIO.read(LevelLoader.class
					.getResourceAsStream(name));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (loadTilesRandomly(tileSheet));
	}

	// private static Level loadTiles(BufferedImage tileSheet) {
	// int width = tileSheet.getWidth();
	// int height = tileSheet.getHeight();
	// Level level = new Level("", width, height);
	//
	// int[] tileData = tileSheet.getRGB(0, 0, width, height, null, 0, width);
	//
	// for (int i = 0; i < tileData.length; i++) {
	// tileData[i] = (tileData[i] >> 16) & 0xFF;
	// tileData[i] = 255 - tileData[i];
	// tileData[i] = tileData[i] / 32 + 1;
	// }
	//
	// for (int i = 0; i < tileData.length; i++) {
	// System.out.println(tileData[i]);
	// if (Tile.tiles[tileData[i]] != null) {
	// level.setTile((byte) tileData[i], i % width, i / width);
	// } else {
	// level.setTile((byte) 0, i % width, i / width);
	// }
	// }
	//
	// return level;
	// }

	private static Level loadTilesRandomly(BufferedImage tileSheet) {
		int width = tileSheet.getWidth();
		int height = tileSheet.getHeight();
		Level level = new Level("", width, height);

		int[] tileData = tileSheet.getRGB(0, 0, width, height, null, 0, width);

		for (int i = 0; i < tileData.length; i++) {
			tileData[i] = (tileData[i] >> 16) & 0xFF;
			tileData[i] = 255 - tileData[i];
			tileData[i] = tileData[i] / 32 + 1;
		}

		for (int i = 0; i < tileData.length; i++) {
			System.out.println(tileData[i]);
			if (Tile.tiles[tileData[i]] != null) {
				if (Tile.tiles[tileData[i]].isParent()
						&& rand.nextInt(100) < 20) {
					level.setTile(
							Tile.tiles[tileData[i]].getChildren()[rand
									.nextInt(Tile.tiles[tileData[i]]
											.getChildren().length)], i % width,
							i / width);
				} else {
					level.setTile((byte) tileData[i], i % width, i / width);
				}
			} else {
				level.setTile(Tile.VOID.getId(), i % width, i / width);
			}
		}
		return level;
	}

	public static void writeLevelToNBT(Level level) {
		try {
			File path = new File(Game.homeDir + "level" + File.separator);
			if (!path.exists()) {
				path.mkdir();
				Game.debug(Game.DebugLevel.INFO, "Level path created!");
			}
			File f = new File(path, level.getName() + ".dat");
			Game.debug(Game.DebugLevel.INFO,
					"Writing inventory \"" + level.getName() + "\" to file "
							+ f.getAbsoluteFile().toString());
			if (!f.exists()) {
				f.createNewFile();
				Game.debug(Game.DebugLevel.INFO, "File created!");
			}
			Tag tag = new Tag(Tag.Type.TAG_Compound, "LEVEL", new Tag[] {
					new Tag(Tag.Type.TAG_String, "NAME", level.getName()),
					// new Tag(Tag.Type.TAG_String, "NEXT_LEVEL",
					// level.getNextLevelName()),
					new Tag(Tag.Type.TAG_String, "NEXT_LEVEL", "none"),
					new Tag(Tag.Type.TAG_Int, "WIDTH", level.getWidth()),
					new Tag(Tag.Type.TAG_Int, "HEIGHT", level.getHeight()),
					new Tag(Tag.Type.TAG_Byte_Array, "TILES",
							level.getTileIdArray()),
					new Tag(Tag.Type.TAG_End, null, null) });
			tag.writeTo(new FileOutputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Level readLevelFromNBT(String p) {
		try {
			File path = new File(Game.homeDir + "level" + File.separator);
			File f = new File(path, p + ".dat");
			if (!f.exists()) {
				Game.debug(DebugLevel.ERROR, "File \"" + f.getAbsolutePath()
						+ "\" does not exist!");
				return null;
			}
			Tag tag = Tag.readFrom(new FileInputStream(f));
			String name = tag.findTagByName("NAME").getValue().toString();
			String nextLevel = tag.findTagByName("NAME").getValue().toString();
			int width = (int) tag.findTagByName("WIDTH").getValue();
			int height = (int) tag.findTagByName("HEIGHT").getValue();
			Level l = new Level(name, width, height);
			byte[] t = (byte[]) tag.findTagByName("TILES").getValue();
			l.setTiles(t);
			l.setNextLevel(nextLevel);
			return l;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
