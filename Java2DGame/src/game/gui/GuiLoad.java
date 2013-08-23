package game.gui;

import game.Game;
import game.InputHandler.InputEvent;
import game.InputHandler.InputEventType;
import game.gui.elements.FontRenderer;
import game.gui.elements.WorldPreview;
import game.level.World;

import java.io.File;

public class GuiLoad extends Gui {

	WorldPreview[] worlds;
	int selectedEntry = 0;

	public GuiLoad(Game game, int width, int height) {
		super(game, width, height);
		File f = new File(World.WORLD_DIR);
		File[] saves = f.listFiles();
		if (saves != null) {
			worlds = new WorldPreview[saves.length];
			for (int i = 0; i < saves.length; i++) {
				worlds[i] = new WorldPreview(saves[i].getAbsolutePath());
			}
		}
	}

	@Override
	public void actionPerformed(InputEvent event) {
		if (event.key.id == input.down.id && event.type == InputEventType.PRESSED) {
			selectedEntry++;
		}
		if (event.key.id == input.up.id && event.type == InputEventType.PRESSED) {
			selectedEntry--;
		}
		if (selectedEntry < 0) {
			selectedEntry = 0;
		}
		if (selectedEntry >= worlds.length) {
			selectedEntry = worlds.length - 1;
		}
		if (event.key.id == input.action.id && event.type == InputEventType.PRESSED) {
			game.setWorld(new World(game, worlds[selectedEntry].name));
			closeGui();
		}
	}

	@Override
	public void render() {
		this.drawDefaultBackground();
		FontRenderer.drawCenteredString("Load", this, this.width / 2 + 1, 5,
				522, 2);

		int slotPosX = 20;
		int slotPosY = 30;
		int slotHeight = 27;
		int slotWidth = 160;

		if (worlds != null) {
			for (int i = 0; i < worlds.length; i++) {
				int frameColor = 0xa0a0a0;
				int fontColor = 333;

				if (i == selectedEntry) {
					frameColor = 0xdd5555;
					fontColor = 555;
				}

				this.drawRect(slotPosX, slotPosY + i * (slotHeight + 3),
						slotWidth, slotHeight, frameColor);

				FontRenderer.drawString("Name: " + worlds[i].name, this,
						slotPosX + 2, slotPosY + i * (slotHeight + 3) + 1, fontColor,
						1);
				FontRenderer.drawString("Time played: " + worlds[i].timePlayed,
						this, slotPosX + 2,
						slotPosY + i * (slotHeight + 3) + 9, fontColor, 1);
				FontRenderer.drawString("Player name: " + worlds[i].playerName,
						this, slotPosX + 2, slotPosY + i * (slotHeight + 3)
								+ 17, fontColor, 1);
			}
		}
	}

	@Override
	public void tick(int ticks) {

	}

	@Override
	public void guiActionPerformed(int elementId, int action) {

	}

}
