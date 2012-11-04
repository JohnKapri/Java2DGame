package game.level;

import game.level.tile.Tile;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LevelLoader {
		
	public LevelLoader() {		
	}

	public static Level loadLevel(String name) {
		BufferedImage tileSheet = null;
		try {
			tileSheet = ImageIO.read(LevelLoader.class.getResourceAsStream(name+".png"));
		} catch (IOException e) {e.printStackTrace();}
		
		return(loadTiles(tileSheet));
	}
	
	private static Level loadTiles(BufferedImage tileSheet) {
		int width = tileSheet.getWidth();
		int height = tileSheet.getHeight();
		Level level = new Level();
		
		int[] tileData = tileSheet.getRGB(0, 0, width, height, null, 0, width);
		
		for(int i = 0; i < tileData.length; i++) {
			tileData[i] = (tileData[i] >> 16) & 0xFF;
			tileData[i] = 255 - tileData[i];
			tileData[i] = tileData[i] / 32 + 1;
		}
		
		for(int i = 0; i < tileData.length; i++) {
			System.out.println(tileData[i]);
			if(Tile.tiles[tileData[i]] != null) {
				level.setTile((byte)tileData[i], i % width, i / width);
			} else {
				level.setTile((byte)0, i % width, i / width);
			}
		}
				
		return level;
	}
}
