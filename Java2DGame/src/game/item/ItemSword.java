package game.item;

import game.entity.Entity;
import game.entity.Player;

public class ItemSword extends Item{

	public ItemSword(int id, String name, int tile, int color) {
		super(id, name, tile, color);
	}

	@Override
	public void onItemUse(Player player, Entity entityAttacked) {
		
	}

}
