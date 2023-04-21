package me.audeck.challengemod.mixins;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "initWidgets")
    private void editGameMenu(CallbackInfo info) {
        for (Element element : this.children) {
            if (((AbstractButtonWidget) element).getMessage().getString().contains("Give Feedback") || ((AbstractButtonWidget) element).getMessage().getString().contains("Report Bugs")) {
                ((AbstractButtonWidget) element).visible = false;
            }
        }

        this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 72 + -16, 204, 20, new LiteralText("audeck's Challenges"), (buttonWidgetx) -> {
            this.client.openScreen(new ConfirmChatLinkScreen((bl) -> {
                if (bl) {
                    Util.getOperatingSystem().open("https://audeck.github.io");
                }

                this.client.openScreen(this);
            }, "https://audeck.github.io", true));
        }));
    }
}
