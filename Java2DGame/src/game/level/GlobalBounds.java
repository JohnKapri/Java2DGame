package game.level;

public class GlobalBounds {

	int x;
	int y;
	int x1;
	int y1;

	public GlobalBounds(int x, int y, int x1, int y1) {
		this.x = x;
		this.y = y;
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public GlobalBounds(int x, int y, int a) {
		this(x, y, x + a, y + a);
	}
	
	public GlobalBounds (int x, int y, LocalBounds bounds) {
		this(x, y, x + bounds.width, y + bounds.height);
	}
	
	public boolean doesCollideWith(GlobalBounds bounds) {
		return GlobalBounds.doBoundsCollide(this, bounds);
	}
	
	public static boolean doBoundsCollide(GlobalBounds b1, GlobalBounds b2) {
		//if(gba.x + )
		return false;
	}
}
