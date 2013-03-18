package game.entity;

import game.gfx.Screen;
import game.level.Level;

public abstract class Entity {

	public int x, y;
	protected int renderLayer = 1;
	protected Level level;
	protected int tickCount;

	public Entity(Level level) {
		init(level);
	}

	public final void init(Level level) {
		this.level = level;
	}
	
	public void tick() {
		tickCount++;
	}
	
	public abstract void render(Screen screen);
	
	public int gerRenderLayer() {
		return renderLayer;
	}
	
	public void action(Player player) {
		
	}
}
