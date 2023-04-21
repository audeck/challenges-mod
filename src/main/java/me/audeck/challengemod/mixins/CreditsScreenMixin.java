package me.audeck.challengemod.mixins;

import me.audeck.challengemod.cardinal.LevelComponents;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreditsScreen.class)
public class CreditsScreenMixin extends Screen {

    protected CreditsScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "init")
    private void setSeenCredits(CallbackInfo info) {
        MinecraftServer server = this.client.getServer();
        WorldProperties properties = server.getOverworld().getLevelProperties();
        LevelComponents.CHALLENGE_PROPERTIES.get(properties).getTag().putBoolean("seen_credits", true);
    }

    /*
    @Inject(at = @At("HEAD"), method = "close")
    private void sendResultMessage(CallbackInfo info) {
        MinecraftServer server = this.client.getServer();
        WorldProperties properties = server.getOverworld().getLevelProperties();

        if (LevelComponents.CHALLENGE_PROPERTIES.get(properties).getTag().getBoolean("speedrun_eligible")) {
            int index = 1;
            StringBuilder mod_list_string = new StringBuilder();

            for (Iterator<ModContainer> it = FabricLoader.getInstance().getAllMods().stream().sorted().iterator(); it.hasNext();) {
                ModContainer mod = it.next();
                mod_list_string.append(mod.getMetadata().getName());

                if (index < FabricLoader.getInstance().getAllMods().size()) {
                    mod_list_string.append(", ");
                }

                index++;
            }

            LevelComponents.CHALLENGE_PROPERTIES.get(properties).getTag().putInt("check_sum", mod_list_string.toString().hashCode());

            this.client.player.sendMessage(new LiteralText(" "), false);
            this.client.player.sendMessage(new LiteralText("Challenge completed successfully!").setStyle(Utils.SERVER_TEXT_STYLE_BOLD), false);
            this.client.player.sendMessage(new LiteralText(" "), false);
            this.client.player.sendMessage(new LiteralText(" - Final time: ").setStyle(Style.EMPTY.withFormatting(Formatting.AQUA)).append(new LiteralText(SpeedrunTimer.TIME_PLAYED_STRING).setStyle(Style.EMPTY.withFormatting(Formatting.YELLOW, Formatting.BOLD))), false);
            this.client.player.sendMessage(new LiteralText(" - Currently loaded mods: ").setStyle(Style.EMPTY.withFormatting(Formatting.AQUA)).append(new LiteralText(mod_list_string.toString()).setStyle(Style.EMPTY.withFormatting(Formatting.YELLOW))), false);
        }
    }
     */
}
