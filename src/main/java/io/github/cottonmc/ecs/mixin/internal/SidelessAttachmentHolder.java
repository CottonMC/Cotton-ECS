package io.github.cottonmc.ecs.mixin.internal;

import io.github.cottonmc.ecs.api.Component;

import java.util.Optional;

public interface SidelessAttachmentHolder {
    void addAttribute(String key, Component attribute, Class type);

    <T> Optional<T> getAttribute(String key, Class<T> type);

    boolean removeAttribute(String key);

}
