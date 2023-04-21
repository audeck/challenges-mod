package me.audeck.challengemod.mixins;

import me.audeck.challengemod.utils.ChallengeProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    // No mining mod
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void disableMining(BlockState block, CallbackInfoReturnable<Float> info_returnable) {
        if (ChallengeProperties.NO_MINING && !block.getMaterial().equals(Material.FIRE)) {
            info_returnable.setReturnValue(0F);
        }
    }
}
