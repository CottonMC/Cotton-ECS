package io.github.cottonmc.ecs.api;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface ComponentContainer {
	/**
	 * Regsters a non-standard component to this container
	 * @param componentClass the interface that describes the Component's behavior.
	 * @param key the key to register the Component under.
	 * @param component the Component to register.
	 * @return true if this component was successfully registered.
	 */
	public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component);
	
	/**
	 * Gets a component from this container, if it exists.
	 * @param <T> the Type of the interface that describes the target Component's behavior
	 * @param componentClass the interface that describes the target Component's behavior.
	 * @param key a key describing which component of this type to retrieve.
	 * @return The specified Component, if available, or null if the Component isn't provided under this key.
	 */
	@Nullable
	public <T extends Component> T getComponent(Class<T> componentClass, String key);
	
	/**
	 * Gets the complete set of valid keys for the specified component class.
	 * @param componentClass the interface that describes the target Component's behavior.
	 * @return A set of keys which can be used to retrieve components of this type, or an empty set if the Component isn't provided.
	 */
	@Nonnull
	public Set<String> getComponentKeys(Class<? extends Component> componentClass);
}
