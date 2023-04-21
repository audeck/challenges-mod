package me.audeck.challengemod.mixins;

import me.audeck.challengemod.screens.ModOptionsScreen;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    @Shadow @Final private Screen parent;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void customOptionsScreen(CallbackInfo info) {
        for (Element element : this.children) {
            if (((AbstractButtonWidget) element).getMessage().getString().contains("Realms Notifications") || ((AbstractButtonWidget) element).getMessage().getString().contains("FOV") || ((AbstractButtonWidget) element).getMessage().getString().contains("Difficulty")) {
                ((AbstractButtonWidget) element).y += 6;
            }
        }

        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 24 - 6, 150, 20, new LiteralText("audeck's Challenge Mods"), (buttonWidget) -> {
            this.client.openScreen(new ModOptionsScreen(this, 0));
        }));
    }
}
