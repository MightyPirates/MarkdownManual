package li.cil.manual.api.prefab.tab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
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
    public void renderIcon(final PoseStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, location);
        Screen.blit(matrixStack, 0, 0, 16, 16, 0, 0, 1, 1, 1, 1);
    }
}
