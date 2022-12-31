package li.cil.manual.client.document.segment.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.client.document.DocumentRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class TextureContentRenderer implements ContentRenderer {
    private final ResourceLocation location;
    private final ImageTexture texture;

    // --------------------------------------------------------------------- //

    public TextureContentRenderer(final ResourceLocation location) {
        this.location = location;

        final TextureManager manager = Minecraft.getInstance().getTextureManager();
        final AbstractTexture image = manager.getTexture(location);
        if (image instanceof ImageTexture imageTexture) {
            this.texture = imageTexture;
        } else {
            this.texture = new ImageTexture(location);
            manager.register(location, texture);
            if (!texture.isValid) {
                throw new IllegalArgumentException();
            }
        }
    }

    // --------------------------------------------------------------------- //

    @Override
    public int getWidth() {
        return texture.width;
    }

    @Override
    public int getHeight() {
        return texture.height;
    }

    @Override
    public void render(final PoseStack matrixStack, final int mouseX, final int mouseY) {
        DocumentRenderTypes.draw(DocumentRenderTypes.texture(location), (buffer) -> {
            final var matrix = matrixStack.last().pose();
            buffer.vertex(matrix, 0, texture.height, 0).uv(0, 1).endVertex();
            buffer.vertex(matrix, texture.width, texture.height, 0).uv(1, 1).endVertex();
            buffer.vertex(matrix, texture.width, 0, 0).uv(1, 0).endVertex();
            buffer.vertex(matrix, 0, 0, 0).uv(0, 0).endVertex();
        });
    }

    // --------------------------------------------------------------------- //

    private static class ImageTexture extends SimpleTexture {
        private int width = 0;
        private int height = 0;
        private boolean isValid;

        ImageTexture(final ResourceLocation location) {
            super(location);
        }

        @Override
        public void load(final ResourceManager manager) throws IOException {
            super.load(manager);
            final TextureImage textureData = getTextureImage(manager);
            try {
                final NativeImage nativeImage = textureData.getImage();
                width = nativeImage.getWidth();
                height = nativeImage.getHeight();
                isValid = true;
            } finally {
                textureData.close();
            }
        }
    }
}
