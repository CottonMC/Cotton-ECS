package io.github.cottonmc.ecs.api;

import net.minecraft.nbt.CompoundTag;

public interface Component {
    void read(CompoundTag tag);

    CompoundTag write(CompoundTag tag);
}
