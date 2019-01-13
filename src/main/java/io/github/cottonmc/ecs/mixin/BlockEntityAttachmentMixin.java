package io.github.cottonmc.ecs.mixin;

import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.api.SidedComponentContainer;
import io.github.cottonmc.ecs.internal.SidedComponentContainerImpl;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BlockEntity.class})
@Interface(iface = SidedComponentContainer.class, prefix = "attachment$")
public class BlockEntityAttachmentMixin {

	private SidedComponentContainer container;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(CallbackInfo ci) {
		container = new SidedComponentContainerImpl();
	}
	
	public <T extends Component> boolean registerExtraComponent(Direction side, Class<T> componentClass, String key, T component) {
		return container.registerExtraComponent(side, componentClass, key, component);
	}
	
	public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component) {
		return container.registerExtraComponent(componentClass, key, component);
	}
	
	@Nullable
	public <T extends Component> T getComponent(Direction side, Class<T> componentClass, String key) {
		return container.getComponent(side, componentClass, key);
	}
	
	@Nonnull
	public Set<String> getComponentKeys(Direction side, Class<? extends Component> componentClass) {
		return container.getComponentKeys(side, componentClass);
	}
	
	//TODO: Deserialize?
}
