package io.github.cottonmc.ecs.internal;


import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.api.ComponentRegistry;
import io.github.cottonmc.ecs.api.SidedComponentContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

public class SidedComponentContainerImpl implements SidedComponentContainer {
	protected List<Entry<? extends Component>> entries = new ArrayList<>();
	protected Set<String> keySet = new HashSet<>();
	
	//implements SidedComponentContainer {
		@Override
		public <T extends Component> boolean registerExtraComponent(Direction side, Class<T> componentClass, String key, T component) {
			Entry<T> existing = getEntry(componentClass, component, key);
			if (existing!=null) {
				existing.directions.add(side);
				return true;
			} else {
				if (contains(side, componentClass, key)) {
					return false;
				} else {
					Entry<T> entry = new Entry<>(componentClass, key, component, false);
					entry.directions.add(side);
					entries.add(entry);
					keySet.add(key);
					return true;
				}
			}
		}
	
		@Override
		public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component) {
			Entry<T> existing = getEntry(componentClass, component, key);
			if (existing!=null) {
				for(Direction d : Direction.values()) existing.directions.add(d);
				return true;
			} else {
				if (contains(componentClass, key)) {
					return false;
				} else {
					Entry<T> entry = new Entry<>(componentClass, key, component, false);
					for(Direction d : Direction.values()) entry.directions.add(d);
					entries.add(entry);
					keySet.add(key);
					return true;
				}
			}
		}
	
		@Override
		public <T extends Component> T getComponent(Direction side, Class<T> componentClass, String key) {
			return get(side, componentClass, key);
		}
	
		@Override
		public Set<String> getComponentKeys(Direction side, Class<? extends Component> componentClass) {
			return ImmutableSet.copyOf(keySet);
		}
	//}
	
	public <T extends Component> boolean register(Direction side, Class<T> componentClass, String key, T component) {
		if (contains(side, componentClass, key)) return false;
		entries.add(new Entry<T>(componentClass, key, component, true));
		keySet.add(key);
		return true;
	}
	
	public void remove(Direction direction, Class<? extends Component> componentClass, String key) {
		for(int i=0; i<entries.size(); i++) {
			Entry<?> entry = entries.get(i);
			if (entry.clazz==componentClass && entry.key.equals(key) && entry.directions.contains(direction)) {
				entry.directions.remove(direction);
				if (entry.directions.isEmpty()) {
					entries.remove(i);
				}
				return;
			}
		}
	}
	
	public ListTag serializeBuiltinComponents() {
		return serializeComponents(it->it.builtin);
	}
	
	public ListTag serializeExtraComponents() {
		return serializeComponents(it->!it.builtin);
	}
	
	protected ListTag serializeComponents(Predicate<Entry<? extends Component>> predicate) {
		ListTag result = new ListTag();
		
		for(Entry<? extends Component> entry : entries) {
			if (!predicate.test(entry)) continue;
			Identifier id = ComponentRegistry.getIdentifier(entry.clazz);
			if (id==null) continue;
			
			CompoundTag entryTag = new CompoundTag();
			entryTag.putString("Id", id.toString());
			entryTag.putString("Key", entry.key);
			ListTag directionTag = new ListTag();
			for(Direction d : entry.directions) directionTag.add(new StringTag(d.toString()));
			entryTag.put("Directions", directionTag);
			Tag valueTag = entry.component.toTag();
			entryTag.put("Value", valueTag);
			
			result.add(entryTag);
		}
		return result;
	}
	
	protected boolean contains(Direction side, Class<?> clazz, String key) {
		for(Entry<?> entry : entries) {
			if (entry.clazz==clazz && entry.key.equals(key) && entry.directions.contains(side)) return true;
		}
		return false;
	}
	
	/** returns true if this container contains the specified component class and key for *any* side. */
	protected boolean contains(Class<?> clazz, String key) {
		for(Entry<?> entry : entries) {
			if (entry.clazz==clazz && entry.key.equals(key)) return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	protected <T extends Component> T get(Direction side, Class<T> clazz, String key) {
		for(Entry<?> entry : entries) {
			if (entry.clazz==clazz && entry.key.equals(key) && entry.directions.contains(side)) {
				return (T) entry.component;
			}
		}
		return null;
	}
	
	/** Returns the Component if the container already has it registered for *any* side. */
	@SuppressWarnings("unchecked")
	@Nullable
	protected <T extends Component> Entry<T> getEntry(Class<T> clazz, T component, String key) {
		for(Entry<?> entry : entries) {
			if (entry.clazz==clazz && entry.component.equals(component) && entry.key.equals(key)) {
				return (Entry<T>) entry;
			}
		}
		return null;
	}
	
	protected static class Entry<T> {
		Class<T> clazz;
		T component;
		String key;
		EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
		boolean builtin;
		
		public Entry(Class<T> componentClass, String key, T component) {
			this.clazz = componentClass;
			this.key = key;
			this.component = component;
			this.builtin = true;
		}
		
		public Entry(Class<T> componentClass, String key, T component, boolean builtin) {
			this.clazz = componentClass;
			this.key = key;
			this.component = component;
			this.builtin = builtin;
		}
		
	}
}
