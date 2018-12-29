package io.github.cottonmc.ecs.mixin.internal;

import io.github.cottonmc.ecs.api.Component;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public interface SidedAttachmentHolder {
    void addAttribute(String key, Component attribute, Class type, Direction side);

    <T> Optional<T> getAttribute(String key, Class<T> type, Direction side);

    boolean removeAttribute(String key, Direction side);

}
