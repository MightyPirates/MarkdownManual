/* SPDX-License-Identifier: MIT */

package li.cil.manual.api.prefab.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

/**
 * Simple implementation of a tab icon renderer using a full texture as its graphic.
 */
public final class TextureTab extends AbstractTab {
    private final Identifier location;

    public TextureTab(final String path, @Nullable final Component tooltip, final Identifier location) {
        super(path, tooltip);
        this.location = location;
    }

    @Override
    public void renderIcon(final GuiGraphics graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, location, 0, 0, 0, 0, 16, 16, 16, 16);
    }
}
