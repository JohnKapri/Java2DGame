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
	File[] worldFiles;
	int selectedEntry = 0;

	public GuiLoad(Game game, int width, int height) {
		super(game, width, height);
		File p = new File(World.WORLD_DIR);
		File[] fs = p.listFiles();
		int valid = 0;
		for (File f : fs) {
			if (f.getName().endsWith(".dat")) {
				valid++;
			}
		}
		worldFiles = new File[valid];
		for (int i = 0; i < valid; i++) {
			worldFiles[i] = fs[i];
		}
		if (worldFiles != null) {
			worlds = new WorldPreview[worldFiles.length];
			for (int i = 0; i < worldFiles.length; i++) {
				worlds[i] = new WorldPreview(worldFiles[i].getAbsolutePath());
			}
		}
	}

	@Override
	public void actionPerformed(InputEvent event) {
		super.actionPerformed(event);

		if (event.key.id == input.down.id
				&& event.type == InputEventType.PRESSED) {
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
		if (event.key.id == input.action.id
				&& event.type == InputEventType.PRESSED) {
			game.setWorld(new World(game, worldFiles[selectedEntry]));
			close();
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
						slotPosX + 2, slotPosY + i * (slotHeight + 3) + 1,
						fontColor, 1);
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
