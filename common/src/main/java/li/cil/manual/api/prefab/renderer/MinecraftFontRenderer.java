package li.cil.manual.api.prefab.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import li.cil.manual.api.render.FontRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;

/**
 * Implementation of the {@link FontRenderer} interface using the default Minecraft font renderer.
 */
public final class MinecraftFontRenderer implements FontRenderer {
    private final net.minecraft.client.gui.Font font;

    public MinecraftFontRenderer(final net.minecraft.client.gui.Font font) {
        this.font = font;
    }

    @Override
    public void draw(final PoseStack matrixStack, final CharSequence value, final int argb) {
        font.draw(matrixStack, value.toString(), 0, 0, argb);
    }

    @Override
    public void drawBatch(final PoseStack matrixStack, final MultiBufferSource buffer, final CharSequence value, final int argb) {
        font.drawInBatch(value.toString(), 0, 0, argb, false, matrixStack.last().pose(), buffer, false, 0, LightTexture.pack(0xF, 0xF));
    }

    @Override
    public int width(final CharSequence value) {
        return font.width(value.toString());
    }

    @Override
    public int width(final Component value) {
        return font.width(value);
    }

    @Override
    public int lineHeight() {
        return font.lineHeight;
    }
}
