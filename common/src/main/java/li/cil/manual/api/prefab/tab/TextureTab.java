package li.cil.manual.api.prefab.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Simple implementation of a tab icon renderer using a full texture as its graphic.
 */
public final class TextureTab extends AbstractTab {
    private final ResourceLocation location;

    public TextureTab(final String path, @Nullable final Component tooltip, final ResourceLocation location) {
        super(path, tooltip);
        this.location = location;
    }

    @Override
    public void renderIcon(final GuiGraphics graphics) {
        graphics.blit(location, 0, 0, 0, 0, 16, 16, 16, 16);
    }
}
