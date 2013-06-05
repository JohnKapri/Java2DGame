package game.level.tile;

public class AnimatedTile extends Tile {

	private int[][] animationTileCoords;
	private int currentAnimationIndex;
	private long lastIterationTime;
	private int animationSwitchDelay;

	public AnimatedTile(int id, String name, int[][] animationCoords, int tileColor,
			int levelColor, int animationSwitchDelay) {
		super(id, name, animationCoords[0][0], animationCoords[0][1], tileColor,
				levelColor);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}

	@Override
	public void tick() {
		if ((System.currentTimeMillis() - lastIterationTime) >= animationSwitchDelay) {
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = ((currentAnimationIndex + 1) % animationTileCoords.length);
			this.tileId = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1] * 32));
		}
	}
}
