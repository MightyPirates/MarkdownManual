package li.cil.manual.client.document;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import li.cil.manual.api.util.Constants;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public final class DocumentRenderTypes extends RenderType {
    private static final RenderType HIGHLIGHT = create(Constants.MOD_ID + "/highlight",
        DefaultVertexFormats.POSITION_COLOR,
        GL11.GL_QUADS, 4,
        false, false,
        State.builder()
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setWriteMaskState(COLOR_WRITE)
            .createCompositeState(false));

    public static RenderType highlight() {
        return HIGHLIGHT;
    }

    public static RenderType texture(final ResourceLocation location) {
        return create(Constants.MOD_ID + "/texture",
            DefaultVertexFormats.POSITION_TEX,
            GL11.GL_QUADS, 4,
            false, false,
            State.builder()
                .setTextureState(new TextureState(location, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false));
    }

    public static void draw(final RenderType renderType, final Consumer<IVertexBuilder> callback) {
        final BufferBuilder builder = Tessellator.getInstance().getBuilder();
        final IRenderTypeBuffer.Impl bufferSource = IRenderTypeBuffer.immediate(builder);
        final IVertexBuilder buffer = bufferSource.getBuffer(renderType);

        callback.accept(buffer);

        bufferSource.endBatch();
    }

    // --------------------------------------------------------------------- //

    private DocumentRenderTypes() {
        super("", DefaultVertexFormats.POSITION, 0, 256, false, false, () -> {}, () -> {});
        throw new UnsupportedOperationException("No meant to be instantiated.");
    }
}
