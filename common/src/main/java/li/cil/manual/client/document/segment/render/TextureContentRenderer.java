package li.cil.manual.client.document.segment.render;

import com.mojang.blaze3d.platform.NativeImage;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.client.document.DocumentRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(final GuiGraphics graphics, final int mouseX, final int mouseY) {
        DocumentRenderTypes.draw(graphics, DocumentRenderTypes.texture(location), (buffer) -> {
            final var matrix = graphics.pose().last().pose();
            buffer.addVertex(matrix, 0, texture.height, 0).setUv(0, 1);
            buffer.addVertex(matrix, texture.width, texture.height, 0).setUv(1, 1);
            buffer.addVertex(matrix, texture.width, 0, 0).setUv(1, 0);
            buffer.addVertex(matrix, 0, 0, 0).setUv(0, 0);
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
