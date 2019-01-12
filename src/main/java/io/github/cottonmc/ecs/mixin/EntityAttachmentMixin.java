package io.github.cottonmc.ecs.mixin;

import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.api.ComponentContainer;
import io.github.cottonmc.ecs.internal.ComponentContainerImpl;
import net.minecraft.entity.Entity;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The attachment implementation for the entity class.
 * */
@Mixin(Entity.class)
@Interface(iface= ComponentContainer.class,prefix="attachment$")
public class EntityAttachmentMixin {
	private ComponentContainer container;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(CallbackInfo ci) {
		container = new ComponentContainerImpl();
	}

	public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component) {
		return container.registerExtraComponent(componentClass, key, component);
	}
	
	@Nullable
	public <T extends Component> T getComponent(Class<T> componentClass, String key) {
		return container.getComponent(componentClass, key);
	}
	
	@Nonnull
	public Set<String> getComponentKeys(Class<? extends Component> componentClass) {
		return container.getComponentKeys(componentClass);
	}
	
	//TODO: Deserialize?
}
