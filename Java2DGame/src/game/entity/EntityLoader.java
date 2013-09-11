package game.entity;

import game.Tag;
import game.level.Level;

import java.lang.reflect.InvocationTargetException;

public class EntityLoader {

	public static Entity loadEntity(Level level, Tag tag) {
		String classname = (String) tag.findTagByName("CLASSNAME").getValue();
		Entity ent = null;
		try {
			ent = (Entity) Class.forName(classname)
					.getConstructor(Level.class, Tag.class).newInstance(level, tag);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return ent;
	}

	public static Tag saveObject(Entity o) {
		Tag tag = new Tag(Tag.Type.TAG_Compound, "ENTITY_" + o.getId(), new Tag[] {new Tag(Tag.Type.TAG_Int, "dump", 0)});
		tag.addTag(new Tag(Tag.Type.TAG_String, "CLASSNAME", o.getClass().getName()));
//		try {
//			if (doesArrayContain(o.getClass().getMethods(),
//					NBTCapable.class.getMethod("saveToNBT", Tag.class))) {
				o.saveToNBT(tag);
//			}
//		} catch (SecurityException | NoSuchMethodException e) {
//			e.printStackTrace();
//		}
		tag.addTag(new Tag(Tag.Type.TAG_End, null, null));
		
		return tag;
	}

	private static boolean doesArrayContain(Object[] array, Object obj) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(obj)) {
				return true;
			}
		}
		return false;
	}
}
