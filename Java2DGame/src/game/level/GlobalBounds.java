package game.level;

import java.awt.geom.Rectangle2D;

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

	public GlobalBounds(int x, int y, LocalBounds bounds) {
		this(x + bounds.xOffset, y + bounds.yOffset, x + bounds.width
				+ bounds.xOffset, y + bounds.height + bounds.yOffset);
	}

	public boolean doesCollideWith(GlobalBounds bounds) {
		return GlobalBounds.doBoundsCollide(this, bounds);
	}

	public static boolean doBoundsCollide(GlobalBounds b1, GlobalBounds b2) {
		int ax1 = b1.x;
		int ax2 = b1.x1;
		int bx1 = b2.x;
		int bx2 = b2.x1;

		int ay1 = b1.y;
		int ay2 = b1.y1;
		int by1 = b2.y;
		int by2 = b2.y1;

		Rectangle2D r1 = new Rectangle2D.Float(ax1, ay1, ax2, ay2);
		Rectangle2D r2 = new Rectangle2D.Float(bx1, by1, bx2, by2);

		if(r1.intersects(r2)) {
			return true;
		}
		
		if (((ax1 >= bx1 && ax1 < bx2) || (ax2 >= bx1 && ax2 < bx2))
				&& ((ay1 >= by1 && ay1 < by2) || (ay2 >= by1 && ay2 < by2))) {
			return true;
		}
		return false;
	}
}
