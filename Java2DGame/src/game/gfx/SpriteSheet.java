package game.gfx;

import game.Tag;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	public String path;
	public String name;
	public int width;
	public int height;

	public int[] pixels;

	public SpriteSheet(String path) {
		if (path.toLowerCase().endsWith(".dat")) {
			Tag tag = null;
			try {
				tag = Tag.readFrom(SpriteSheet.class.getResourceAsStream(path));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (tag == null) {
				return;
			}

			name = tag.findTagByName("NAME").getValue().toString();
			pixels = (int[]) tag.findTagByName("PIXELS").getValue();
		} else {
			BufferedImage image = null;
			try {
				image = ImageIO.read(SpriteSheet.class
						.getResourceAsStream(path));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (image == null) {
				return;
			}

			this.path = path;
			this.width = image.getWidth();
			this.height = image.getHeight();

			pixels = image.getRGB(0, 0, width, height, null, 0, width);

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = (pixels[i] & 0xff) / 64;
			}
			name = "legacy";
		}
	}

	public SpriteSheet(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
}
