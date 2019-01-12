package io.github.cottonmc.ecs.api;

import net.minecraft.nbt.Tag;

public interface Component {
	/**
	 * Read this Component in from the specified Tag. Malformed tags result in no changes to the Component.
	 */
	void fromTag(Tag tag);
	
	/**
	 * Write this Component out to a Tag.
	 * @return An nbt Tag of any type
	 */
	Tag toTag();
}