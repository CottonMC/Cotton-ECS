package io.github.cottonmc.ecs.api;

import net.minecraft.util.math.Direction;

public interface SidedComponent {

    boolean isSideValid(Direction side);
}
