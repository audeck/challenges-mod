package me.audeck.challengemod.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.sound.SoundInstanceListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubtitlesHud.class)
public abstract class SubtitlesHudMixin extends DrawableHelper implements SoundInstanceListener {
    @Shadow @Final MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;translatef(FFF)V", shift = At.Shift.AFTER))
    private void translateSubtitles(CallbackInfo info) {
        switch ((int)this.client.getWindow().getScaleFactor()) {
            case 1:
                RenderSystem.translatef(0F, 1F, 0F);
                break;
            case 2:
                RenderSystem.translatef(0F, -3.5F, 0F);
                break;
            case 3:
                RenderSystem.translatef(0F, 1F, 0F);
                break;
            case 4:
                if (this.client.options.fullscreen) {
                    RenderSystem.translatef(0F, 2F, 0F);
                } else {
                    RenderSystem.translatef(0F, 1F, 0F);
                }
                break;
        }
    }
}
