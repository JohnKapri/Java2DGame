package game.item;

import game.gfx.Colors;

public abstract class ItemPotion extends Item{
	
	public ItemPotion(int id, String name, PotionType type) {
		super(id, name, 32 * 3, Colors.get(555, type.getPotionColor(), 431, -1));
	}
	
	public enum PotionType {
		HEAL(511),
		HARM(031),
		EFFECT_GOOD(330),
		EFFECT_BAD(033),
		OTHER(555);
		
		private int color;
		
		PotionType (int color) {
			this.color = color;
		}
		
		public int getPotionColor() {
			return color;
		}
	}
}
