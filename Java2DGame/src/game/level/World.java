package game.level;

import game.Game;
import game.Game.DebugLevel;
import game.Tag;
import game.entity.Heart;
import game.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class World implements NBTCapable {
	
	public static String WORLD_DIR = Game.homeDir + "saves" + File.separator;

	//private Region[] regions;
	private Player player;
	private Game game;
	private boolean isFirstLoad;
	private long timeStartThisSesson;
	private long timePlayed;
	private Tag worldTag;
	private String name;

	public World(Game game, String name) {
		this.name = name;
			File path = new File(Game.homeDir + "saves" + File.separator);
			File f = new File(path, (new StringBuilder().append("WORLD_")
					.append(this.name).toString()).toUpperCase().replaceAll(
					" ", "_")
					+ ".dat");
			if (!f.exists()) {
				Game.debug(DebugLevel.ERROR, "File \"" + f.getAbsolutePath()
						+ "\" does not exist!");
				return;
			}
			this.game = game;
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

	public World(Game game) {
		this.game = game;
		while (true) {
			this.name = JOptionPane
					.showInputDialog("Give a name to the new Save");
			File f = new File(Game.homeDir, "/saves/"
					+ name.toUpperCase().replace(" ", "_"));
			if (!f.exists()) {
				break;
			}
		}
		Level level = new Level("tile_test", 0, 0);
		level.loadLevelFromFile("/levels/tile_test.png");
		game.level = level;
		player = new Player(game,
				JOptionPane.showInputDialog("Enter the Player's name!"), 32, 32);
		game.player = player;
		game.level.addEntity(game.player);
		game.level.addEntity(new Heart(game.level, 25 * 8, 30 * 8));
		
		timeStartThisSesson = System.currentTimeMillis();
	}

	public void saveToFile() {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadFromNBT(Tag tag) {
		game.level = new Level(worldTag);
		game.player = new Player(game, worldTag.findTagByName("PLAYER"));
		this.player = game.player;
		game.level.addEntity(player);
		game.level.addEntity(new Heart(game.level, 25 * 8, 30 * 8));
	}

	@Override
	public Tag saveToNBT(Tag notused) {
		byte virgin = 0;
		if (isFirstLoad) {
			virgin = 1;
		}
		timePlayed += (System.currentTimeMillis() - this.timeStartThisSesson);
		Tag tag = new Tag(Tag.Type.TAG_Compound, new StringBuilder()
				.append("WORLD_")
				.append((this.name).toUpperCase().replaceAll(" ", "_"))
				.toString(), new Tag[1]);
		tag.addTag(new Tag(Tag.Type.TAG_String, "NAME", this.name));
		tag.addTag(new Tag(Tag.Type.TAG_Byte, "VIRNGIN", virgin));
		tag.addTag(new Tag(Tag.Type.TAG_Long, "TIME_PLAYED", this.timePlayed));
		tag.addTag(player.saveToNBT(null));
		tag.addTag(game.level.saveToNBT(null));
		tag.addTag(new Tag(Tag.Type.TAG_End, null, null));
		return tag;
	}
}
