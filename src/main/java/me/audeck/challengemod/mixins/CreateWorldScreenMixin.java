package me.audeck.challengemod.mixins;

import me.audeck.challengemod.screens.ChallengeSelectScreen;
import me.audeck.challengemod.utils.ChallengeProperties;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin extends Screen {
    @Shadow ButtonWidget gameRulesButton;
    private ButtonWidget challenge_options_button;

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "method_29695", at = @At("HEAD"))
    private void resetChallengeOptions(CallbackInfo info) {
        ChallengeProperties.reset();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;setMoreOptionsOpen()V"), method = "init")
    private void editButtons(CallbackInfo info) {
        this.gameRulesButton.x = this.width / 2 + 5;
        this.gameRulesButton.y = 151;
        this.challenge_options_button = (ButtonWidget) this.addButton(new ButtonWidget(this.width / 2 - 155, 185, 150, 20, new LiteralText("audeck's Challenges"), (buttonWidget) -> {
            this.client.openScreen(new ChallengeSelectScreen(this, 0));
        }));
    }

    @Inject(at = @At("TAIL"), method = "setMoreOptionsOpen(Z)V")
    private void editButtonVisibility(boolean moreOptionsOpen, CallbackInfo info) {
        this.gameRulesButton.visible = moreOptionsOpen;
        this.challenge_options_button.visible = !moreOptionsOpen;
    }
}
