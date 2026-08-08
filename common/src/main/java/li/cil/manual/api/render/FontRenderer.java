package li.cil.manual.api.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

/**
 * Base interface for font renderers.
 */
public interface FontRenderer {
    /**
     * Render a string into the specified buffer, for use in world rendering.
     * <p>
     * GUI rendering no longer goes through a {@link MultiBufferSource}; use
     * {@link #draw(GuiGraphics, CharSequence, int)} there instead.
     *
     * @param value  the string to render.
     * @param argb   the color to render the string with.
     * @param matrix the current transform matrix.
     * @param buffer the buffer to render the string into.
     */
    void drawInBatch(final CharSequence value, final int argb, final Matrix4f matrix, final MultiBufferSource buffer);

    /**
     * Draws a string at the origin of the current transform of the specified graphics context.
     *
     * @param graphics the current graphics context.
     * @param value    the string to render.
     * @param argb     the color to render the string with.
     */
    void draw(final GuiGraphics graphics, final CharSequence value, final int argb);

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
