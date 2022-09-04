package li.cil.manual.client.document;

import com.mojang.blaze3d.vertex.*;
import li.cil.manual.api.util.Constants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public final class DocumentRenderTypes extends RenderType {
    private static final RenderType HIGHLIGHT = create(Constants.MOD_ID + "/highlight",
        DefaultVertexFormat.POSITION_COLOR,
        VertexFormat.Mode.QUADS, 4,
        false, false,
        CompositeState.builder()
            .setShaderState(POSITION_COLOR_SHADER)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setWriteMaskState(COLOR_WRITE)
            .createCompositeState(false));

    public static RenderType highlight() {
        return HIGHLIGHT;
    }

    public static RenderType texture(final ResourceLocation location) {
        return create(Constants.MOD_ID + "/texture",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS, 4,
            false, false,
            CompositeState.builder()
                .setShaderState(POSITION_TEX_SHADER)
                .setTextureState(new TextureStateShard(location, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false));
    }

    public static void draw(final RenderType renderType, final Consumer<VertexConsumer> callback) {
        final BufferBuilder builder = Tesselator.getInstance().getBuilder();
        final MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(builder);
        final VertexConsumer buffer = bufferSource.getBuffer(renderType);

        callback.accept(buffer);

        bufferSource.endBatch();
    }

    // --------------------------------------------------------------------- //

    private DocumentRenderTypes() {
        super("", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, () -> {}, () -> {});
        throw new UnsupportedOperationException("No meant to be instantiated.");
    }
}
