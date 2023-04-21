package me.audeck.challengemod.screens;

import me.audeck.challengemod.utils.ModOptions;
import me.audeck.challengemod.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModOptionsScreen extends Screen {

    protected final Screen parent;
    private int page;

    public ModOptionsScreen(Screen parent, int page) {
        super(new LiteralText("audeck's Challenge Mods (Page " + (page + 1) + "/2)"));
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
                if (((ButtonWidget) element).getMessage().getString().contains("No Mining:")) {
                    tooltip = MinecraftClient.getInstance().textRenderer.wrapLines(
                        new LiteralText("Makes you unable to mine blocks and other materials (i.e. shrubs, dead bushes, etc.) apart from fire.")
                    , 200);
                    tooltip.add(new LiteralText(" "));
                    tooltip.addAll(MinecraftClient.getInstance().textRenderer.wrapLines(
                        new LiteralText("Suggestion: enable both this and 'No Crafting' at once to play... well, definitely not ").setStyle(Utils.server_text_style).append(
                        new LiteralText("Minecraft").setStyle(Utils.server_text_style.withItalic(true))).append(
                        new LiteralText(".").setStyle(Utils.server_text_style))
                    , 200));
                }
            }
        }

        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        if (tooltip != null) {
            this.renderTooltip(matrices, tooltip, mouseX, mouseY);
        }
    }

    private void initWidgets() {
        switch (page) {
            case 0:
                if (!ModOptions.no_mining) {
                    this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 - 6, 150, 20, new LiteralText("No Mining: OFF"), (buttonWidget) -> {
                        ModOptions.no_mining = true;
                        this.client.openScreen(new ModOptionsScreen(this.parent, this.page));
                    }));
                } else {
                    this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 - 6, 150, 20, new LiteralText("No Mining: ON"), (buttonWidget) -> {
                        ModOptions.no_mining = false;
                        this.client.openScreen(new ModOptionsScreen(this.parent, this.page));
                    }));
                }
                // ModOptions.no_mining_option.createButton(this.client.options, this.width / 2 - 155, this.height / 6 - 6, 150);
                this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 - 6, 150, 20, new LiteralText("Coming soon..."), (buttonWidget) -> {
                    System.out.println("Coming soon...");
                }));
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

        this.addButton(new ButtonWidget(this.width / 2 - 100 + 24, this.height / 6 + 168, 200 - 48, 20, ScreenTexts.DONE, (buttonWidget) -> {
            this.client.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 20, 20, new LiteralText("<<"), (buttonWidget) -> {
            this.page -= 1;
            if (this.page < 0) {this.page = 1;}
            this.client.openScreen(new ModOptionsScreen(this.parent, this.page));
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 100 - 20, this.height / 6 + 168, 20, 20, new LiteralText(">>"), (buttonWidget) -> {
            this.page += 1;
            if (this.page > 1) {this.page = 0;}
            this.client.openScreen(new ModOptionsScreen(this.parent, this.page));
        }));
    }

    public void onClose() {
        this.client.openScreen(this.parent);
    }
}
