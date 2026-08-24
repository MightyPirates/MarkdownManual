/* SPDX-License-Identifier: MIT */

package li.cil.manual.client.document.segment.render;

import com.mojang.blaze3d.platform.NativeImage;
import li.cil.manual.api.render.ContentRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class TextureContentRenderer implements ContentRenderer {
    private final Identifier location;
    private final ImageTexture texture;

    // --------------------------------------------------------------------- //

    public TextureContentRenderer(final Identifier location) {
        this.location = location;

        final TextureManager manager = Minecraft.getInstance().getTextureManager();
        final AbstractTexture image = manager.getTexture(location);
        if (image instanceof ImageTexture imageTexture) {
            this.texture = imageTexture;
        } else {
            this.texture = new ImageTexture(location);
            manager.registerAndLoad(location, texture);
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
        graphics.blit(RenderPipelines.GUI_TEXTURED, location,
            0, 0, 0, 0,
            texture.width, texture.height,
            texture.width, texture.height);
    }

    // --------------------------------------------------------------------- //

    private static class ImageTexture extends SimpleTexture {
        private int width;
        private int height;
        private boolean isValid;

        ImageTexture(final Identifier location) {
            super(location);
        }

        @Override
        public TextureContents loadContents(final ResourceManager manager) throws IOException {
            final TextureContents contents = super.loadContents(manager);
            final NativeImage nativeImage = contents.image();
            width = nativeImage.getWidth();
            height = nativeImage.getHeight();
            isValid = true;
            return contents;
        }
    }
}
