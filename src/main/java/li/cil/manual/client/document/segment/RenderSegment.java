package li.cil.manual.client.document.segment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.api.render.InteractiveContentRenderer;
import li.cil.manual.api.util.PathUtils;
import li.cil.manual.client.document.DocumentRenderTypes;
import li.cil.manual.client.document.DocumentRenderer;
import li.cil.manual.client.document.Strings;
import li.cil.manual.client.document.segment.render.MissingContentRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public final class RenderSegment extends AbstractSegment implements InteractiveSegment {
    private final Component title;
    private final ContentRenderer renderer;

    // --------------------------------------------------------------------- //

    public RenderSegment(final DocumentRenderer document, final Segment parent, final String title, final String url) {
        super(document, parent);
        this.title = new TextComponent(title);

        final String path;
        if (url.contains(":")) {
            // Namespaced URL, don't try to resolve as relative.
            path = url;
        } else if (document.getLocation() != null) {
            // We know where we are, try to resolve path.
            path = PathUtils.resolve(document.getLocation().toString(), url);
        } else {
            // No reference, use as-is.
            path = url;
        }

        final Optional<ContentRenderer> renderer = model.imageFor(path);
        this.renderer = renderer.orElseGet(() -> new MissingContentRenderer(Strings.getMissingContentText(url)));
    }

    // --------------------------------------------------------------------- //

    @Override
    public Optional<Component> getTooltip() {
        if (renderer instanceof InteractiveContentRenderer interactiveRenderer) {
            return Optional.of(interactiveRenderer.getTooltip(title));
        } else {
            return Optional.of(title);
        }
    }

    @Override
    public boolean mouseClicked() {
        return renderer instanceof InteractiveContentRenderer interactiveRenderer && interactiveRenderer.mouseClicked();
    }

    @Override
    public int getLineHeight(final int segmentX, final int documentWidth) {
        return imageHeight(segmentX, documentWidth);
    }

    @Override
    public Optional<InteractiveSegment> render(final PoseStack matrixStack, final int segmentX, final int lineHeight, final int documentWidth, final int mouseX, final int mouseY) {
        final int width = imageWidth(segmentX, documentWidth);
        final int height = imageHeight(segmentX, documentWidth);

        final boolean wrapBefore = segmentX >= documentWidth;
        final boolean centerAndWrapAfter = segmentX == 0 || wrapBefore;

        final int x = centerAndWrapAfter ? (documentWidth - width) / 2 : segmentX;
        final int y = wrapBefore ? lineHeight : 0;

        final float scale = scale(segmentX, documentWidth);

        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(scale, scale, scale);

        final boolean isHovered = mouseX >= x && mouseX <= x + width &&
                                  mouseY >= y && mouseY <= y + height;
        if (isHovered) {
            DocumentRenderTypes.draw(DocumentRenderTypes.highlight(), (buffer) -> {
                final Matrix4f matrix = matrixStack.last().pose();

                final float r = 0.2f, g = 0.4f, b = 0.6f, a = 0.25f;

                buffer.vertex(matrix, 0, renderer.getHeight(), 0).color(r, g, b, a).endVertex();
                buffer.vertex(matrix, renderer.getWidth(), renderer.getHeight(), 0).color(r, g, b, a).endVertex();
                buffer.vertex(matrix, renderer.getWidth(), 0, 0).color(r, g, b, a).endVertex();
                buffer.vertex(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
            });
        }

        renderer.render(matrixStack, mouseX, mouseY);

        matrixStack.popPose();

        return isHovered ? Optional.of(this) : Optional.empty();
    }

    @Override
    public NextSegmentInfo getNext(final int segmentX, final int lineHeight, final int documentWidth) {
        final int width = imageWidth(segmentX, documentWidth);
        final int height = imageHeight(segmentX, documentWidth);

        final boolean wrapBefore = segmentX >= documentWidth;
        final boolean centerAndWrapAfter = segmentX == 0 || wrapBefore;

        final int localX = centerAndWrapAfter ? (documentWidth - width) / 2 : segmentX;
        final int localY = wrapBefore ? lineHeight : 0;

        final int absoluteX = centerAndWrapAfter ? 0 : (localX + width);
        final int relativeY = localY + (centerAndWrapAfter ? height + 1 : 0);
        return new NextSegmentInfo(next, absoluteX, relativeY);
    }

    @Override
    public String toString() {
        return String.format("![%s](%s)", title, renderer);
    }

    // --------------------------------------------------------------------- //

    private int imageWidth(final int segmentX, final int documentWidth) {
        if (segmentX >= documentWidth) {
            return Math.min(documentWidth, renderer.getWidth());
        } else {
            return Math.min(documentWidth - segmentX, renderer.getWidth());
        }
    }

    private int imageHeight(final int segmentX, final int documentWidth) {
        return Mth.ceil(renderer.getHeight() * scale(segmentX, documentWidth));
    }

    private float scale(final int segmentX, final int documentWidth) {
        // Only scale down; if necessary, scale down to fill remainder of current line.
        return Math.min(1, imageWidth(segmentX, documentWidth) / (float) renderer.getWidth());
    }
}
