package me.audeck.challengemod.cardinal.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface CompoundTagComponent {
    CompoundTag getTag();
    void setTag(CompoundTag tag);
}
