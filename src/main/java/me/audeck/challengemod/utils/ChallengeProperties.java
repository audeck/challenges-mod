package me.audeck.challengemod.utils;

import me.audeck.challengemod.cardinal.LevelComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.WorldProperties;

// @Environment(EnvType.CLIENT)
public class ChallengeProperties {
    public static boolean SPEEDRUN_MODE;
    public static boolean NO_MINING;
    public static boolean NO_CRAFTING;

    public static void load(MinecraftClient client) {
        CompoundTag tag = LevelComponents.CHALLENGE_PROPERTIES.get((WorldProperties) client.getServer().getSaveProperties()).getTag();

        if (tag.isEmpty()) {
            create(client);
        } else {
            SPEEDRUN_MODE = tag.contains("speedrun_mode") && tag.getBoolean("speedrun_mode");
            NO_MINING = tag.contains("no_mining") && tag.getBoolean("no_mining");  // Safe loading
            NO_CRAFTING = tag.contains("no_crafting") && tag.getBoolean("no_crafting");
        }
    }

    public static void create(MinecraftClient client) {
        CompoundTag tag = LevelComponents.CHALLENGE_PROPERTIES.get((WorldProperties) client.getServer().getSaveProperties()).getTag();

        tag.putBoolean("seen_credits", false);
        tag.putLong("time_played", 0L);
        tag.putLong("pause_time", 5000L);
        tag.putBoolean("speedrun_mode", SPEEDRUN_MODE);
        tag.putBoolean("no_mining", NO_MINING);
        tag.putBoolean("no_crafting", NO_CRAFTING);
    }

    public static void reset() {
        SPEEDRUN_MODE = false;
        NO_MINING = false;
        NO_CRAFTING = false;
    }
}
