package game.level;

import game.Tag;

public interface NBTCapable {

	/**
	 * Loads the instance from the given NBT.
	 * @param tag The Tag that contains the data. This is usually the main Tag for the instance. (e.g. Tag "PLAYER" is given to the Player, not "WORLD")
	 */
	abstract public void loadFromNBT(Tag tag);
	
	/**
	 * Saves the instance to the given NBT.
	 * @param tag The Tag that will contain the data. This is usually the main Tag for the instance. (e.g. Tag "PLAYER" is given to the Player, not "WORLD")
	 */
	abstract public Tag saveToNBT(Tag tag);
}
