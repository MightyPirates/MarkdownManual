package li.cil.manual.api.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

/**
 * Base interface for font renderers.
 */
public interface FontRenderer {
    /**
     * Render up to the specified amount of characters of the specified string.
     *
     * @param matrixStack the current matrix stack.
     * @param buffer      the buffer to render the string into.
     * @param value       the string to render.
     * @param argb        the color to render the string with.
     */
    void drawBatch(final PoseStack matrixStack, final MultiBufferSource buffer, final CharSequence value, final int argb);

    /**
     * Draws a string in immediate mode.
     *
     * @param matrixStack the current matrix stack.
     * @param value       the string to render.
     * @param argb        the color to render the string with.
     */
    default void draw(final PoseStack matrixStack, final CharSequence value, final int argb) {
        final BufferBuilder builder = Tesselator.getInstance().getBuilder();
        final MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(builder);
        drawBatch(matrixStack, buffer, value, argb);
        buffer.endBatch();
    }

    /**
     * Computes the rendered width of the provided character sequence.
     *
     * @param value the value to get the render width for.
     * @return the render width of the specified value.
     */
    int width(final CharSequence value);

    /**
     * Computes the rendered width of the provided text component.
     *
     * @param value the value to get the render width for.
     * @return the render width of the specified value.
     */
    int width(final Component value);

    /**
     * Get the height of the characters drawn with the font renderer, in pixels.
     *
     * @return the height of the drawn characters.
     */
    int lineHeight();
}
