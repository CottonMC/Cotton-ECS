package io.github.cottonmc.ecs.mixin;

import io.github.cottonmc.ecs.api.BlockComponentContainer;
import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.internal.BlockComponentContainerImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {Block.class})
@Interface(iface = BlockComponentContainer.class, prefix = "attachment$")
public class BlockAttachmentMixin {

	private BlockComponentContainer container;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(CallbackInfo ci) {
		container = new BlockComponentContainerImpl();
	}
	
	public <T extends Component> boolean attachment$registerExtraComponent(BlockState state, World world, BlockPos pos, Direction side, Class<T> componentClass, String key, T component) {
		return container.registerExtraComponent(state, world, pos, side, componentClass, key, component);
	}
	
	public <T extends Component> boolean attachment$registerExtraComponent(Class<T> componentClass, String key, T component) {
		return container.registerExtraComponent(componentClass, key, component);
	}
	
	@Nullable
	public <T extends Component> T attachment$getComponent(BlockState state, World world, BlockPos pos, Direction side, Class<T> componentClass, String key) {
		return container.getComponent(state, world, pos, side, componentClass, key);
	}
	
	@Nonnull
	public Set<String> attachment$getComponentKeys(BlockState state, World world, BlockPos pos, Direction side, Class<? extends Component> componentClass) {
		return container.getComponentKeys(state, world, pos, side, componentClass);
	}
	
	//TODO: Deserialize?
}
