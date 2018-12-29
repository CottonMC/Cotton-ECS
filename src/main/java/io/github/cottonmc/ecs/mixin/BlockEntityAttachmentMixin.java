package io.github.cottonmc.ecs.mixin;


import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.api.SidedComponent;
import io.github.cottonmc.ecs.internal.ComponentContainer;
import io.github.cottonmc.ecs.mixin.internal.SidedAttachmentHolder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = {BlockEntity.class})
@Interface(iface = SidedAttachmentHolder.class, prefix = "attachment$")
public class BlockEntityAttachmentMixin {

    private ComponentContainer attachmentContainer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        attachmentContainer = new ComponentContainer();
    }


    public void attachment$addAttribute(String key, Component attribute, Class type) {
        attachmentContainer.addAttribute(key, attribute, type);
    }

    /**
     * we check if the attribute is sided, if it is we check for the side, otherwise return it.
     */
    public <T> Optional<T> attachment$getAttribute(String key, Class<T> type, Direction side) {

        Optional<T> attribute = attachmentContainer.getAttribute(key, type);
        if (attribute.isPresent()) {
            T sidedAttachment = attribute.get();
            if (sidedAttachment instanceof SidedComponent) {
                if (((SidedComponent) sidedAttachment).isSideValid(side)) {
                    return Optional.of(sidedAttachment);
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.of(sidedAttachment);
            }
        }

        return Optional.empty();
    }

    public boolean attachment$removeAttribute(String key) {
        return attachmentContainer.removeAttribute(key);
    }
}
