package game.level;

import game.Tag;

public interface NBTCapable {

	/**
	 * Loads the instance from the given NBT.
	 * 
	 * @param tag
	 *            The Tag that contains the data. This is usually the parent Tag
	 *            for the instance. (e.g. Tag "REGIONS" is given to the Region,
	 *            not "REGION_$id")
	 */
	abstract public void loadFromNBT(Tag tag);

	/**
	 * Saves the instance to the given NBT.
	 * 
	 * @param tag
	 *            The Tag that will contain the data. This is usually the parent
	 *            Tag for the instance. (e.g. Tag "REGIONS" is given to the
	 *            Region, not "REGION_$id")
	 */
	abstract public Tag saveToNBT(Tag tag);
}
