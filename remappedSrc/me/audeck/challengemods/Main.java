package me.audeck.challengemod;

import me.audeck.challengemod.utils.ModOptions;
import me.audeck.challengemod.utils.Utils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Material;
import net.minecraft.text.LiteralText;

public class Main implements ModInitializer {

	// public ModSettings mod_settings = new ModSettings();

	@Override
	public void onInitialize() {
		// No mining mod
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			if (ModOptions.no_mining && !state.getMaterial().equals(Material.FIRE) && !player.abilities.creativeMode) {
				player.sendMessage(new LiteralText("* Nope!").setStyle(Utils.server_text_style), false);
				return false;
			} else {
				return true;
			}
		});
	}
}
