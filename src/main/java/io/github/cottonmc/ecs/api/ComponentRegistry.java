package io.github.cottonmc.ecs.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;

public class ComponentRegistry {
	private static final Logger LOGGER = LogManager.getLogger(ComponentRegistry.class);
	private static Map<Identifier, Registration<?>> registry = new HashMap<>();
	private static Map<Class<? extends Component>, Identifier> byClass = new HashMap<>();
	
	@Nullable
	public static Component deserialize(Identifier id, Tag tag) {
		Registration<? extends Component> reg = registry.get(id);
		Component result = reg.supplier.get();
		result.fromTag(tag);
		return result;
	}
	
	@Nullable
	public static Class<? extends Component> getComponentClass(Identifier id) {
		Registration<? extends Component> reg = registry.get(id);
		if (reg==null) return null;
		return reg.clazz;
	}
	
	public static <T extends Component> void register(Identifier id, Class<T> clazz, Supplier<T> supplier) {
		if (registry.containsKey(id)) {
			LOGGER.warn("Duplicate registration for component '%s' ignored!", id);
		} else {
			registry.put(id, new Registration<T>(clazz, supplier));
			byClass.put(clazz, id);
		}
	}
	
	private static class Registration<T extends Component> {
		Class<T> clazz;
		Supplier<T> supplier;
		
		public Registration(Class<T> clazz, Supplier<T> supplier) {
			this.clazz = clazz;
			this.supplier = supplier;
		}
	}

	public static Identifier getIdentifier(Class<? extends Component> clazz) {
		return byClass.get(clazz);
	}
}
