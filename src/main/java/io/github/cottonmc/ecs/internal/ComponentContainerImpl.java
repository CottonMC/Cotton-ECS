package io.github.cottonmc.ecs.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.api.ComponentContainer;
import io.github.cottonmc.ecs.api.ComponentRegistry;
import io.github.cottonmc.ecs.api.BlockComponentContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * 
 */
public class ComponentContainerImpl implements ComponentContainer {
	protected List<Entry<? extends Component>> entries = new ArrayList<>();
	protected Set<String> keySet = new HashSet<>();
	
	//implements ComponentContainer {
		
		@Override
		public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component) {
			if (contains(componentClass, key)) return false;
			entries.add(new Entry<T>(componentClass, key, component, false));
			keySet.add(key);
			return true;
		}
		
		@Override
		public <T extends Component> T getComponent(Class<T> componentClass, String key) {
			return get(componentClass, key);
		}
		
		@Override
		public Set<String> getComponentKeys(Class<? extends Component> componentClass) {
			return ImmutableSet.copyOf(keySet);
		}
	//}
	
	public <T extends Component> boolean register(Class<T> componentClass, String key, T component) {
		if (contains(componentClass, key)) return false;
		entries.add(new Entry<T>(componentClass, key, component, true));
		keySet.add(key);
		return true;
	}
	
	public void remove(Class<? extends Component> componentClass, String key) {
		for(int i=0; i<entries.size(); i++) {
			Entry<?> entry = entries.get(i);
			if (entry.clazz==componentClass && entry.key.equals(key)) {
				entries.remove(i);
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
			if (entry.builtin) continue;
			Identifier id = ComponentRegistry.getIdentifier(entry.clazz);
			if (id==null) continue;
			
			CompoundTag entryTag = new CompoundTag();
			entryTag.putString("Id", id.toString());
			entryTag.putString("Key", entry.key);
			Tag valueTag = entry.component.toTag();
			entryTag.put("Value", valueTag);
			
			result.add(entryTag);
		}
		return result;
	}
	
	protected boolean contains(Class<?> clazz, String key) {
		for(Entry<?> entry : entries) {
			if (entry.clazz==clazz && entry.key.equals(key)) return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	protected <T extends Component> T get(Class<T> clazz, String key) {
		for(Entry<?> entry : entries) {
			if (entry.clazz==clazz && entry.key.equals(key)) {
				return (T) entry.component;
			}
		}
		return null;
	}
	
	protected static class Entry<T> {
		Class<T> clazz;
		T component;
		String key;
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
