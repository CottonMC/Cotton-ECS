package io.github.cottonmc.ecs.api;

import io.github.cottonmc.ecs.internal.ComponentContainer;
import io.github.cottonmc.ecs.internal.ComponentException;

import java.util.*;
import java.util.function.Function;

public class Registry<T> {

    private Map<Class<T>, List<EntryContainer>> attributes;

    public Registry() {
        attributes = new HashMap<>();
    }

    /**
     * Adds an attribute to an entry, upon it's creation.
     *
     * @param type    the concrete class of the entry that we attach to.
     * @param creator a function tha can build the new parameter that we attach
     */
    public void delegateToEntry(Class<T> type, String name, Function<T, Component> creator) {
        EntryContainer entryContainer = new EntryContainer(name, creator, type);
        if (attributes.containsKey(type)) {
            attributes.get(type).add(entryContainer);
        } else {
            LinkedList<EntryContainer> entryContainers = new LinkedList<>();
            entryContainers.add(entryContainer);
            attributes.put(type, entryContainers);
        }
    }

    public T attachToEntry(T entry) {
        Class<?> entryClass = entry.getClass();
        if (!(entry instanceof ComponentContainer)) {
            throw new ComponentException(entryClass.getCanonicalName() + " is not an attribute container");
        }

        if (attributes.containsKey(entryClass)) {
            for (EntryContainer entryContainer : attributes.get(entryClass)) {
                Component attribute = entryContainer.creator.apply(entry);
                ((ComponentContainer) entry).addAttribute(entryContainer.name, attribute, entryContainer.type);
            }
        }

        return entry;
    }

    private class EntryContainer {
        private final String name;
        private final Function<T, Component> creator;
        private final Class type;

        public EntryContainer(String name, Function<T, Component> creator, Class type) {
            this.name = name;
            this.creator = creator;
            this.type = type;
        }

    }
}
