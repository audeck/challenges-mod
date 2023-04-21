package me.audeck.challengemod.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

import java.util.Iterator;
import java.util.List;

public abstract class CustomTooltipRenderer extends Screen {

    protected CustomTooltipRenderer(Text title) {
        super(title);
    }

    @Override
    public void renderTooltip(MatrixStack matrices, List<? extends StringRenderable> lines, int x, int y) {
        if (!lines.isEmpty()) {
            int i = 0;
            Iterator iterator = lines.iterator();

            while (iterator.hasNext()) {
                StringRenderable stringRenderable = (StringRenderable) iterator.next();
                int j = this.textRenderer.getWidth(stringRenderable);
                if (j > i) {
                    i = j;
                }
            }

            int k = x + 12;
            int l = y - 12;
            int n = 8;
            if (lines.size() > 1) {
                n += 2 + (lines.size() - 1) * 10;
            }

            /*
            if (this.client.options.guiScale > 2) {
                if (x > this.width / 2) {
                    k -= 20 + i;
                }
            } else {
                if (k + i > this.width - 12) {
                    k -= 20 + i;
                }
            }
            */

            k = x > this.width/2 ? this.width/2 - i - 3 : this.width/2 + 3;
            l = this.height/6 - 6 + Math.floorDiv((y - (this.height/6 - 6)), 24)*24 + 3;

            if (l + n + 6 > this.height) {
                l -= l + n + 6 - this.height;
            }

            matrices.push();
            int o = -267386864;
            int p = 1347420415;
            int q = 1344798847;
            int r = 1;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = matrices.peek().getModel();
            fillGradient(matrix4f, bufferBuilder, k - 3, l - 4, k + i + 3, l - 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, k - 3, l + n + 3, k + i + 3, l + n + 4, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, k - 3, l - 3, k + i + 3, l + n + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, k - 4, l - 3, k - 3, l + n + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, k + i + 3, l - 3, k + i + 4, l + n + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, k - 3, l - 3 + 1, k - 3 + 1, l + n + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, k + i + 2, l - 3 + 1, k + i + 3, l + n + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, k - 3, l - 3, k + i + 3, l - 3 + 1, 400, 1347420415, 1347420415);
            fillGradient(matrix4f, bufferBuilder, k - 3, l + n + 2, k + i + 3, l + n + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0D, 0.0D, 400.0D);

            for (int s = 0; s < lines.size(); ++s) {
                StringRenderable stringRenderable2 = (StringRenderable) lines.get(s);
                if (stringRenderable2 != null) {
                    this.textRenderer.draw((StringRenderable) stringRenderable2, (float) k, (float) l, -1, true, matrix4f, immediate, false, 0, 15728880);
                }

                if (s == 0) {
                    l += 2;
                }

                l += 10;
            }

            immediate.draw();
            matrices.pop();
        }
    }
}
