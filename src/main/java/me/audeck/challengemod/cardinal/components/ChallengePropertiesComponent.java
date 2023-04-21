package me.audeck.challengemod.cardinal.components;

import me.audeck.challengemod.cardinal.LevelComponents;
import me.audeck.challengemod.cardinal.interfaces.CompoundTagComponent;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.sync.LevelSyncedComponent;
import net.minecraft.nbt.CompoundTag;

public class ChallengePropertiesComponent implements CompoundTagComponent, LevelSyncedComponent {
    private CompoundTag tag = new CompoundTag();

    @Override
    public CompoundTag getTag() {
        return this.tag;
    }

    @Override
    public void setTag(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.tag = tag.getCompound("challenge_options");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.put("challenge_options", this.tag);
        return tag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return LevelComponents.CHALLENGE_PROPERTIES;
    }
}
