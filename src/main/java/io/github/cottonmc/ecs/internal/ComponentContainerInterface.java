package io.github.cottonmc.ecs.internal;

import io.github.cottonmc.ecs.api.Component;

import java.util.Optional;

/**
 * The attachment container template
 * */
public interface ComponentContainerInterface {

    void addAttribute(String key, Component attribute, Class type);

    <T> Optional<T> getAttribute(String key, Class<T> type);

    boolean removeAttribute(String key);

}
