package me.audeck.challengemod.mixins;

import me.audeck.challengemod.utils.ChallengeProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Shadow @Final PlayerEntity player;

    // No crafting mod
    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private static void manageRecipes(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo info) {
        if (ChallengeProperties.NO_CRAFTING && !player.abilities.creativeMode) {
            info.cancel();
        }
    }
}
