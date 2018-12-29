package io.github.cottonmc.ecs.mixin;

import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.internal.ComponentContainer;
import io.github.cottonmc.ecs.mixin.internal.SidelessAttachmentHolder;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * The attachment implementation for the entity class.
 * */
@Mixin(Entity.class)
@Interface(iface= SidelessAttachmentHolder.class,prefix="attachment$")
public class EntityAttachmentMixin {
    private ComponentContainer attachmentContainer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        attachmentContainer = new ComponentContainer();
    }


    public void attachment$addAttribute(String key, Component attribute, Class type) {
        attachmentContainer.addAttribute(key, attribute, type);
    }

    public <T> Optional<T> attachment$getAttribute(String key, Class<T> type) {
        return attachmentContainer.getAttribute(key, type);
    }

    public boolean attachment$removeAttribute(String key) {
        return attachmentContainer.removeAttribute(key);
    }
}
