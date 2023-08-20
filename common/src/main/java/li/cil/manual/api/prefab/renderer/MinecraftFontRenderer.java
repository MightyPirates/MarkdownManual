package li.cil.manual.api.prefab.renderer;

import li.cil.manual.api.render.FontRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

/**
 * Implementation of the {@link FontRenderer} interface using the default Minecraft font renderer.
 */
public final class MinecraftFontRenderer implements FontRenderer {
    private final net.minecraft.client.gui.Font font;

    public MinecraftFontRenderer(final net.minecraft.client.gui.Font font) {
        this.font = font;
    }

    @Override
    public void draw(final GuiGraphics graphics, final CharSequence value, final int argb) {
        graphics.drawString(font, value.toString(), 0, 0, argb);
    }

    @Override
    public void drawInBatch(final CharSequence value, final int argb, final Matrix4f matrix, final MultiBufferSource buffer) {
        font.drawInBatch(value.toString(), 0, 0, argb, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, LightTexture.pack(0xF, 0xF));
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
