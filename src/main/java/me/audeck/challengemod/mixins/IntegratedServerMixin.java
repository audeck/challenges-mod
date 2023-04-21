package me.audeck.challengemod.mixins;

import me.audeck.challengemod.utils.ChallengeProperties;
import me.audeck.challengemod.utils.SpeedrunTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Shadow @Final MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "setupServer")
    private void loadChallengeOptions(CallbackInfoReturnable<Boolean> info) {
        SpeedrunTimer.FIRST_CYCLE = true;
        ChallengeProperties.load(this.client);
    }

    @Inject(at = @At("TAIL"), method = "shutdown")
    private void resetChallengeOptions(CallbackInfo info) {
        ChallengeProperties.reset();
    }
}
