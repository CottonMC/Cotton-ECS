package io.github.cottonmc.ecs.api;

import javax.annotation.Nonnull;

public interface Observable {
	public void listen(@Nonnull Runnable r);
}
