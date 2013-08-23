package game.level;

import game.Game;
import game.Game.DebugLevel;
import game.Tag;
import game.entity.Player;
import game.gfx.Screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class World implements NBTCapable {

	public static String WORLD_DIR = Game.homeDir + "saves" + File.separator;
	public static int VERSION = 1;

	private Level level;
	private Player player;
	private Game game;
	private long timeStartThisSesson;
	private long timePlayed;
	private Tag worldTag;
	private String name;

	public World(Game game, String name) {
		this.name = name;
		this.game = game;
		File path = new File(Game.homeDir + "saves" + File.separator);
		File f = new File(path, (new StringBuilder().append("WORLD_").append(
				this.name).toString()).toUpperCase().replaceAll(" ", "_")
				+ ".dat");
		if (!f.exists()) {
			Game.debug(DebugLevel.ERROR, "File \"" + f.getAbsolutePath()
					+ "\" does not exist!");
			return;
		}
		try {
			worldTag = Tag.readFrom(new FileInputStream(f));
			loadFromNBT(worldTag);
		} catch (IOException e) {
			Game.debug(
					Game.DebugLevel.ERROR,
					"An error occured while loading world from \""
							+ f.getAbsolutePath()
							+ "\"! The file may be corrupted or ist not a valid save file.");
			e.printStackTrace();
		}
		timeStartThisSesson = System.currentTimeMillis();
	}
	
	public void tick() {
		if(level != null) {
			level.tick();
		}
	}

	public void render(Screen screen) {
		if(level != null) {
			int xOffset = 0;
			int yOffset = 0;
			if (player != null) {
				xOffset = player.x - (screen.width / 2);
				yOffset = player.y - (screen.height / 2);
			}

			level.renderTiles(screen, xOffset, yOffset);
		}
	}

	public void writeToFile() {
		Tag tag = saveToNBT(null);
		File path = new File(Game.homeDir + "saves" + File.separator);
		if (!path.exists()) {
			Game.debug(Game.DebugLevel.INFO, "Save file directory created.");
			path.mkdir();
		}
		File f = new File(path, (new StringBuilder().append("WORLD_").append(
				this.name).toString()).toUpperCase().replaceAll(" ", "_")
				+ ".dat");
		if (!f.exists()) {
			Game.debug(DebugLevel.ERROR, "File \"" + f.getAbsolutePath()
					+ "\" does not exist! Creating...");
		}
		try {
			tag.writeTo(new FileOutputStream(f));
			this.timeStartThisSesson = System.currentTimeMillis();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFromNBT(Tag tag) {
		this.name = tag.findTagByName("NAME").getValue().toString();
		this.timePlayed = (long) tag.findTagByName("TIME_PLAYED").getValue();
		String startLevel = tag.findTagByName("STARTLEVEL").getValue().toString();
		level = new Level(tag.findTagByName("LEVELS").findTagByName(startLevel));
		player = new Player(game, tag);
	}

	@Override
	public Tag saveToNBT(Tag notused) {
		timePlayed += (System.currentTimeMillis() - this.timeStartThisSesson);
		Tag tag = new Tag(Tag.Type.TAG_Compound, new StringBuilder()
				.append("WORLD_")
				.append((this.name).toUpperCase().replaceAll(" ", "_"))
				.toString(), new Tag[1]);
		tag.addTag(new Tag(Tag.Type.TAG_String, "NAME", this.name));
		tag.addTag(new Tag(Tag.Type.TAG_Long, "TIME_PLAYED", this.timePlayed));
		tag.addTag(new Tag(Tag.Type.TAG_String, "STARTLEVEL", level.getName()));
		tag.addTag(player.saveToNBT(null));
		// tag.addTag(game.level.saveToNBT(null));
		Tag levels = new Tag("LEVELS", Tag.Type.TAG_Compound);
		levels.addTag(level.saveToNBT(null));
		tag.addTag(levels);
		tag.addTag(new Tag(Tag.Type.TAG_End, null, null));
		return tag;
	}
}
