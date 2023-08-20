package li.cil.manual.api.prefab.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.chars.Char2IntMap;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import li.cil.manual.api.render.FontRenderer;
import li.cil.manual.api.util.Constants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.Optional;

/**
 * Base implementation for texture based monospace font rendering.
 */
public abstract class BitmapFontRenderer implements FontRenderer {
    private final Char2IntMap CHAR_MAP;

    private final int COLUMNS = getResolution() / (charWidth() + getGapU());
    private final float U_SIZE = charWidth() / (float) getResolution();
    private final float V_SIZE = lineHeight() / (float) getResolution();
    private final float U_STEP = (charWidth() + getGapU()) / (float) getResolution();
    private final float V_STEP = (lineHeight() + getGapV()) / (float) getResolution();

    private RenderType renderLayer;

    protected BitmapFontRenderer() {
        CHAR_MAP = new Char2IntOpenHashMap();
        final CharSequence chars = getCharacters();
        for (int index = 0; index < chars.length(); index++) {
            CHAR_MAP.put(chars.charAt(index), index);
        }
    }

    // --------------------------------------------------------------------- //

    /**
     * {@inheritDoc}
     */
    public void drawInBatch(final CharSequence value, final int argb, final Matrix4f matrix, final MultiBufferSource bufferFactory) {
        final VertexConsumer buffer = getDefaultBuffer(bufferFactory);

        float tx = 0f;
        for (int i = 0; i < value.length(); i++) {
            final char ch = value.charAt(i);
            drawChar(matrix, buffer, argb, tx, ch);
            tx += width(" ") + getGapU();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int width(final CharSequence value) {
        return value.length() * charWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int width(final Component value) {
        final MutableInteger count = new MutableInteger();
        value.visit(s -> {
            count.value += s.length() * charWidth();
            return Optional.empty();
        });
        return count.value;
    }

    // --------------------------------------------------------------------- //

    /**
     * The list of characters available in the backing texture, in left-to right, then top-to bottom order.
     * <p>
     * For example, for the character sequence {@code abcd}, the texture would look like this:
     * <pre>
     * ab
     * cd
     * </pre>
     *
     * @return the list of characters.
     */
    protected abstract CharSequence getCharacters();

    /**
     * The location of the texture to use for rendering characters.
     *
     * @return the location of the font texture.
     */
    protected abstract ResourceLocation getTextureLocation();

    /**
     * The actual resolution of the texture.
     * <p>
     * Note that the texture is expected to be squared.
     *
     * @return the resolution of the texture.
     */
    protected abstract int getResolution();

    /**
     * The horizontal pixel gap between characters in the font texture.
     *
     * @return the horizontal character gap.
     */
    protected abstract int getGapU();

    /**
     * The vertical pixel gap between characters in the font texture.
     *
     * @return the vertical character gap.
     */
    protected abstract int getGapV();

    /**
     * The width of a single character, in pixels.
     *
     * @return the width of a character.
     */
    protected abstract int charWidth();

    // --------------------------------------------------------------------- //

    private VertexConsumer getDefaultBuffer(final MultiBufferSource bufferFactory) {
        if (renderLayer == null) {
            renderLayer = FontRenderTypes.create(getTextureLocation());
        }

        return bufferFactory.getBuffer(renderLayer);
    }

    private void drawChar(final Matrix4f matrix, final VertexConsumer buffer, final int argb, final float x, final char ch) {
        if (Character.isWhitespace(ch) || Character.isISOControl(ch)) {
            return;
        }

        final int index = getCharIndex(ch);

        final int a = (argb >>> 24) & 0xFF;
        final int r = (argb >>> 16) & 0xFF;
        final int g = (argb >>> 8) & 0xFF;
        final int b = argb & 0xFF;

        final int column = index % COLUMNS;
        final int row = index / COLUMNS;
        final float u = column * U_STEP;
        final float v = row * V_STEP;

        buffer.vertex(matrix, x, lineHeight(), 0)
            .color(r, g, b, a)
            .uv(u, v + V_SIZE)
            .endVertex();
        buffer.vertex(matrix, x + charWidth(), lineHeight(), 0)
            .color(r, g, b, a)
            .uv(u + U_SIZE, v + V_SIZE)
            .endVertex();
        buffer.vertex(matrix, x + charWidth(), 0, 0)
            .color(r, g, b, a)
            .uv(u + U_SIZE, v)
            .endVertex();
        buffer.vertex(matrix, x, 0, 0)
            .color(r, g, b, a)
            .uv(u, v)
            .endVertex();
    }

    private int getCharIndex(final char ch) {
        if (!CHAR_MAP.containsKey(ch)) {
            return CHAR_MAP.get('?');
        }
        return CHAR_MAP.get(ch);
    }

    // --------------------------------------------------------------------- //

    private static final class MutableInteger {
        public int value;
    }

    private static final class FontRenderTypes extends RenderType {
        public static RenderType create(final ResourceLocation texture) {
            return create(Constants.MOD_ID + "/bitmap_font",
                DefaultVertexFormat.POSITION_COLOR_TEX,
                VertexFormat.Mode.QUADS, 256,
                false, false,
                CompositeState.builder()
                    .setShaderState(POSITION_COLOR_TEX_SHADER)
                    .setTextureState(new TextureStateShard(texture, false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));
        }

        // --------------------------------------------------------------------- //

        private FontRenderTypes() {
            super("", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, () -> {
            }, () -> {
            });
            throw new UnsupportedOperationException("No meant to be instantiated.");
        }
    }
}
