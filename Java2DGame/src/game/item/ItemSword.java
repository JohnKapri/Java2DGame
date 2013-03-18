package game.item;

import game.entity.Entity;
import game.entity.Player;

public class ItemSword extends Item{

	public ItemSword(int id, int tile, int color) {
		super(id, tile, color);
	}

	@Override
	public void onItemUse(Player player, Entity entityAttacked) {
		
	}

}
