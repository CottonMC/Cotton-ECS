package io.github.cottonmc.ecs.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public interface ItemComponentContainer {
	/**
	 * Regsters a non-standard component to this container.
	 * @param stack the ItemStack to register to.
	 * @param componentClass the interface that describes the Component's behavior.
	 * @param key the key to register the Component under.
	 * @param component the Component to register.
	 * @return true if this component was successfully registered.
	 */
	public <T extends Component> boolean registerExtraComponent(ItemStack stack, Class<T> componentClass, String key, T component);

	/**
	 * Regsters a non-standard component to this container. If there was previously a component registered
	 * for this interface and key for *any* side, nothing is registered and false is returned.
	 * @param componentClass the interface that describes the Component's behavior.
	 * @param key the key to register the Component under.
	 * @param component the Component to register.
	 * @return true if this component was successfully registered.
	 */
	public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component);

	/**
	 * Gets a component from this container, if it exists.
	 * @param <T> the Type of the interface that describes the target Component's behavior
	 * @param stack the ItemStack to be inspected.
	 * @param componentClass the interface that describes the target Component's behavior.
	 * @param key a key describing which component of this type to retrieve.
	 * @return The specified Component, if available, or null if the Component isn't provided under this key.
	 */
	@Nullable
	public <T extends Component> T getComponent(ItemStack stack, Class<T> componentClass, String key);

	/**
	 * Gets the complete set of valid keys for the specified component class.
	 * @param stack the ItemStack to be inspected.
	 * @param componentClass the interface that describes the target Component's behavior.
	 * @return A set of keys which can be used to retrieve components of this type, or an empty set if the Component isn't provided.
	 */
	@Nonnull
	public Set<String> getComponentKeys(ItemStack stack, Class<? extends Component> componentClass);
}
