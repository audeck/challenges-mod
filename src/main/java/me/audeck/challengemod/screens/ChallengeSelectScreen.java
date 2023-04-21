package me.audeck.challengemod.screens;

import me.audeck.challengemod.utils.ChallengeProperties;
import me.audeck.challengemod.utils.CustomTooltipRenderer;
import me.audeck.challengemod.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChallengeSelectScreen extends CustomTooltipRenderer {
    protected final Screen parent;
    private int page;


    public ChallengeSelectScreen(Screen parent, int page) {
        super(new LiteralText("audeck's Challenges (Page " + (page + 1) + "/2)"));
        this.parent = parent;
        this.page = page;
    }

    protected void init() {
        this.initWidgets();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        @Nullable List<StringRenderable> tooltip = null;

        for (Element element : this.children) {
            if (element.isMouseOver(mouseX, mouseY) && element instanceof ButtonWidget) {
                // Speedrun mode tooltip
                if (((ButtonWidget) element).getMessage().getString().contains("Speedrun Mode:") && hasShiftDown()) {
                    tooltip = MinecraftClient.getInstance().textRenderer.wrapLines(
                            new LiteralText("Enables a seamless speedrun timer, as well as some quality-of-life changes for 1.16.1+.")
                            , 200);
                    tooltip.add(new LiteralText(" "));
                    tooltip.addAll(MinecraftClient.getInstance().textRenderer.wrapLines(
                            new LiteralText("Note: Doesn't give you Dream luck, but what can you do.").setStyle(Utils.SERVER_TEXT_STYLE_BOLD)
                            , 200));
                }
                // No mining tooltip
                if (((ButtonWidget) element).getMessage().getString().contains("No Mining:") && hasShiftDown()) {
                    tooltip = MinecraftClient.getInstance().textRenderer.wrapLines(
                        new LiteralText("Makes you unable to mine blocks and other materials (i.e. grass, saplings, etc.) with the exception of fire.")
                            , 200);
                    tooltip.add(new LiteralText(" "));
                    tooltip.addAll(MinecraftClient.getInstance().textRenderer.wrapLines(
                        new LiteralText("Fun fact: this can render some worlds unplayable.").setStyle(Utils.SERVER_TEXT_STYLE_BOLD)
                            , 200));
                }
                // No crafting tooltip
                if (((ButtonWidget) element).getMessage().getString().contains("No Crafting:") && hasShiftDown()) {
                    tooltip = MinecraftClient.getInstance().textRenderer.wrapLines(
                            new LiteralText("'Disables' every single crafting recipe in the game.")
                            , 200);
                    tooltip.add(new LiteralText(" "));
                    tooltip.addAll(MinecraftClient.getInstance().textRenderer.wrapLines(
                            new LiteralText("Suggestion: enable both this and 'No Mining' at once to play... well, definitely not ").setStyle(Utils.SERVER_TEXT_STYLE_BOLD).append(
                                    new LiteralText("Minecraft").setStyle(Utils.SERVER_TEXT_STYLE_BOLD.withItalic(true))).append(
                                    new LiteralText(".").setStyle(Utils.SERVER_TEXT_STYLE_BOLD))
                            , 200));
                }
            }
        }

        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, (this.height / 6 - 6)/2 - 9, 16777215);  // #FFFFFF
        this.drawCenteredString(matrices, this.textRenderer, "Hold shift for more details!", this.width / 2, (this.height / 6 - 6)/2 + 1, 6579300);  // #646464
        super.render(matrices, mouseX, mouseY, delta);
        if (tooltip != null) {
            this.renderTooltip(matrices, tooltip, mouseX, mouseY);
        }
    }

    private void initWidgets() {
        switch (page) {
            case 0:
                // Speedrun mode button
                if (!ChallengeProperties.SPEEDRUN_MODE) {
                    this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 - 6, 150, 20, new LiteralText("Speedrun Mode: OFF"), (buttonWidget) -> {
                        ChallengeProperties.SPEEDRUN_MODE = true;
                        this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
                    }));
                } else {
                    this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 - 6, 150, 20, new LiteralText("Speedrun Mode: ON"), (buttonWidget) -> {
                        ChallengeProperties.SPEEDRUN_MODE = false;
                        this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
                    }));
                }
                // No mining button
                if (!ChallengeProperties.NO_MINING) {
                    this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 - 6, 150, 20, new LiteralText("No Mining: OFF"), (buttonWidget) -> {
                        ChallengeProperties.NO_MINING = true;
                        this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
                    }));
                } else {
                    this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 - 6, 150, 20, new LiteralText("No Mining: ON"), (buttonWidget) -> {
                        ChallengeProperties.NO_MINING = false;
                        this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
                    }));
                }
                /*
                // No crafting button
                if (!ChallengeProperties.NO_CRAFTING) {
                    this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 - 6, 150, 20, new LiteralText("No Crafting: OFF"), (buttonWidget) -> {
                        ChallengeProperties.NO_CRAFTING = true;
                        this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
                    }));
                } else {
                    this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 - 6, 150, 20, new LiteralText("No Crafting: ON"), (buttonWidget) -> {
                        ChallengeProperties.NO_CRAFTING = false;
                        this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
                    }));
                }
                 */

                this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 - 6 + 24, 150, 20, new LiteralText("Coming soon..."), (buttonWidget) -> {
                    System.out.println("Coming soon...");
                }));
                this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 - 6 + 24, 150, 20, new LiteralText("Coming soon..."), (buttonWidget) -> {
                    System.out.println("Coming soon...");
                }));
                break;
            case 1:
                break;
        }

        this.addButton(new ButtonWidget(this.width / 2 - 100 + 24, this.height - 28, 200 - 48, 20, new LiteralText("Done"), (buttonWidget) -> {
            this.client.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 28, 20, 20, new LiteralText("<<"), (buttonWidget) -> {
            this.page -= 1;
            if (this.page < 0) {this.page = 1;}
            this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 100 - 20, this.height - 28, 20, 20, new LiteralText(">>"), (buttonWidget) -> {
            this.page += 1;
            if (this.page > 1) {this.page = 0;}
            this.client.openScreen(new ChallengeSelectScreen(this.parent, this.page));
        }));
    }

    public void onClose() {
        this.client.openScreen(this.parent);
    }
}
