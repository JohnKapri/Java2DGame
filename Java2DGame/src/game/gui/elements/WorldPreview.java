package game.gui.elements;

import game.Game;
import game.Tag;
import game.Game.DebugLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldPreview {

	public String name;
	public String timePlayed;
	public String playerName;
	public File file;

	public WorldPreview(String path) {
		File f = new File(path);
		Tag worldTag = null;
		if (!f.exists()) {
			Game.debug(DebugLevel.WARNING, "File \"" + f.getAbsolutePath()
					+ "\" does not exist!");
			return;
		}
		try {
			worldTag = Tag.readFrom(new FileInputStream(f));
		} catch (IOException e) {
			Game.debug(
					Game.DebugLevel.ERROR,
					"An error occured while loading world from \""
							+ f.getAbsolutePath()
							+ "\"! The file may be corrupted or ist not a valid save file.");
			e.printStackTrace();
		}
		this.name = worldTag.findTagByName("NAME").getValue().toString();
		this.timePlayed = getDisplayTimeFromMillis((long) worldTag
				.findTagByName("TIME_PLAYED").getValue());
		this.playerName = worldTag.findTagByName("PLAYER")
				.findTagByName("NAME").getValue().toString();
	}

	private static String getDisplayTimeFromMillis(long milliseconds) {
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
		StringBuilder out = new StringBuilder();
		//if (seconds == 0 || minutes == 0 || hours == 0) {
			//out.append("not played yet");
		//} else {
			if (hours > 0) {
				out.append(hours).append("h");
			}
			if (minutes > 0) {
				out.append(minutes).append("m");
			}
			if (seconds > 0) {
				out.append(seconds).append("s");
			}
		//}
		return out.toString();
	}
}
