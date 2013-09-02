package game.level;

public class LocalBounds {
	
	public int width;
	public int height;
	int xOffset;
	int yOffset;
	
	/**
	 * Creates new local bounds with the given properties.
	 * @param width The width of the object at which it collides.
	 * @param height The height of the object at which it collides.
	 */
	public LocalBounds(int width, int height) {
		this(width, height, 0, 0);
	}
	
	/**
	 * Creates new local bounds with the given properties.
	 * @param width The width of the object at which it collides.
	 * @param height The height of the object at which it collides.
	 * @param xOffset The x offset of the bounds relative to the object's 0,0 point.
	 * @param yOffset The y offset of the bounds relative to the object's 0,0 point.
	 */
	public LocalBounds(int width, int height, int xOffset, int yOffset) {
		if(width < 0 || height < 0) {
			return;
		}
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
}
