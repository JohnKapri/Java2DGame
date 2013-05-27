package game.level;

public class LocalBounds {
	
	int width;
	int height;
	int xOffset;
	int yOffset;
	
	public LocalBounds(int width, int height) {
		this(width, height, 0, 0);
	}
	
	public LocalBounds(int width, int height, int xOffset, int yOffset) {
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
}
