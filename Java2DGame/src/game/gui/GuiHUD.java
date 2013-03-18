package game.gui;

import game.Game;
import game.InputHandler;
import game.entity.Player;
import game.gfx.Colors;
import game.gui.elements.GuiContainer;
import game.gui.elements.GuiItemSlot;
import game.gui.elements.GuiRenderer;
import game.item.Item;
import game.item.ItemSlot;

public class GuiHUD extends Gui{
	
	private Player player;
	private GuiContainer hotbar;
	private int hotbarSize = 5;
	private GuiItemSlot hotbarSlot;

	public GuiHUD(Game game, Player player, int width, int height) {
		super(game, width, height);
		this.player = player;
//		hotbar = new GuiContainer(1, hotbarSize);
//		for(int i = 0; i < hotbarSize-1; i++) {
//			hotbar.addSlotAt(new ItemSlot(), 1, i);
//		}
//		hotbar.getSlot(0).setItem(Item.sword);
//		hotbarSlot = new GuiItemSlot(0, this, hotbar, 0);
	}

	public void actionPerformed(InputHandler input) {
		
	}

	public void render() {
		for (int i = 0; i < player.getMaxHealth(); i++) {
			int color;
			if (i < player.getHealth()) {
				color = Colors.get(-1, -1, 400, 400);
				if (!player.shouldDisplay())
					color = Colors.get(-1, -1, 555, 400);
			} else {
				color = Colors.get(-1, -1, 222, 222);
				if (!player.shouldDisplay())
					color = Colors.get(-1, -1, 555, 222);
			}

			GuiRenderer.render(this, font, 2 + i * 8, 2, 32, color,
					0x00, 1);
		}
//		for(int j = 0; j < hotbarSize-1; j++) {
//			hotbarSlot.setSlotIndex(j);
//			hotbarSlot.render(this, 20+j*10, this.height - 10, 0);
//		}
	}

	public void tick(int ticks) {
		
	}

	public void guiActionPerformed(int elementId, int action) {
		
	}
}
