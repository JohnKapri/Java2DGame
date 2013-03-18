package game.level;

import game.level.tile.Tile;

import java.awt.image.BufferedImage;
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

	private static Level loadTiles(BufferedImage tileSheet) {
		int width = tileSheet.getWidth();
		int height = tileSheet.getHeight();
		Level level = new Level();

		int[] tileData = tileSheet.getRGB(0, 0, width, height, null, 0, width);

		for (int i = 0; i < tileData.length; i++) {
			tileData[i] = (tileData[i] >> 16) & 0xFF;
			tileData[i] = 255 - tileData[i];
			tileData[i] = tileData[i] / 32 + 1;
		}

		for (int i = 0; i < tileData.length; i++) {
			System.out.println(tileData[i]);
			if (Tile.tiles[tileData[i]] != null) {
				level.setTile((byte) tileData[i], i % width, i / width);
			} else {
				level.setTile((byte) 0, i % width, i / width);
			}
		}

		return level;
	}

	private static Level loadTilesRandomly(BufferedImage tileSheet) {
		int width = tileSheet.getWidth();
		int height = tileSheet.getHeight();
		Level level = new Level();

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
											.getChildren().length)], i
									% width, i / width);
				} else {
					level.setTile((byte) tileData[i], i % width, i / width);
				}
			} else {
				level.setTile(Tile.VOID.getId(), i % width, i / width);
			}
		}

		return level;
	}
}
