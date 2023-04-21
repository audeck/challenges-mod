package me.audeck.challengemod.mixins;

import me.audeck.challengemod.utils.ChallengeProperties;
import me.audeck.challengemod.utils.SpeedrunTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
    @Shadow @Final MinecraftClient client;
    public SpeedrunTimer speedrun_timer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addSpeedrunTimer(MinecraftClient client, CallbackInfo info) {
        this.speedrun_timer = new SpeedrunTimer(client);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMountHealth(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.BEFORE))
    private void renderSpeedrunTimer(MatrixStack matrixStack, float f, CallbackInfo info) {
        MinecraftServer server = this.client.getServer();
        WorldProperties properties = (WorldProperties) server.getSaveProperties();

        //if (LevelComponents.CHALLENGE_PROPERTIES.get(properties).getTag().getBoolean("speedrun_mode")) {
        if (ChallengeProperties.SPEEDRUN_MODE) {
            if (this.client.player.isCreative() || server.isRemote() || server.getPlayerManager().areCheatsAllowed()) {
                //ChallengeProperties.SPEEDRUN_MODE = false;
                //LevelComponents.CHALLENGE_PROPERTIES.get(properties).getTag().putBoolean("speedrun_mode", false);
                this.speedrun_timer.render(matrixStack);
            } else {
                this.speedrun_timer.render(matrixStack);
            }
        }
    }
}
