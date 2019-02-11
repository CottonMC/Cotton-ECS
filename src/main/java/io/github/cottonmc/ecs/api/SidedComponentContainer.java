package io.github.cottonmc.ecs.api;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.math.Direction;

public interface SidedComponentContainer {
	/**
	 * Regsters a non-standard component to this container.
	 * @param <T> the Type of the interface that describes the provided Component's behavior.
	 * @param side the Direction which the Component is being registered for, from the perspective of the container.
	 * @param componentClass the interface that describes the provided Component's behavior.
	 * @param key the key to register the Component under.
	 * @param component the Component to register.
	 * @return true if this component was successfully registered.
	 */
	public <T extends Component> boolean registerExtraComponent(Direction side, Class<T> componentClass, String key, T component);
	
	/**
	 * Registers a non-standard component to this container for all sides. If there was previously a component registered
	 * for this interface and key for *any* side, nothing is registered and false is returned.
	 * @param <T> the Type of the interface that describes the provided Component's behavior.
	 * @param componentClass the interface that describes the provided Component's behavior.
	 * @param key the key to register the Component under
	 * @param component the Component to register
	 * @return true if this component was successfully registered for all sides. false if it was not registered to any side.
	 */
	public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component);
	
	/**
	 * Gets a component from this container, if it exists.
	 * @param <T> the Type of the interface that describes the target Component's behavior.
	 * @param side the Direction which Components are being inspected from, from the perspective of the container.
	 * @param componentClass the interface that describes the target Component's behavior.
	 * @param key a key describing which component of this type to retrieve.
	 * @return The specified Component, if available, or null if the Component isn't provided under this key.
	 */
	@Nullable
	public <T extends Component> T getComponent(Direction side, Class<T> componentClass, String key);
	
	/**
	 * Gets the complete set of valid keys for the specified component class.
	 * @param componentClass the interface that describes the target Component's behavior.
	 * @param side the side to inspect for Components of the specified type.
	 * @return A set of keys which can be used to retrieve components of this type, or an empty set if the Component isn't provided.
	 */
	@Nonnull
	public Set<String> getComponentKeys(Direction side, Class<? extends Component> componentClass);
}
