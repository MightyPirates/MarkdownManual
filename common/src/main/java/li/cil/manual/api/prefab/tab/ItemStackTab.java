package li.cil.manual.api.prefab.tab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;

import javax.annotation.Nullable;

/**
 * Simple implementation of a tab icon renderer using an item stack as its graphic.
 */
public final class ItemStackTab extends AbstractTab {
    private final ItemStack stack;

    public ItemStackTab(final String path, @Nullable final Component tooltip, final ItemStack stack) {
        super(path, tooltip);
        this.stack = stack;
    }

    @Override
    public void renderIcon(final PoseStack matrixStack) {
        // This is *nasty*, but sadly there's no renderItemAndEffectIntoGUI() variant that
        // takes a MatrixStack. Yet.

        final var position = new Vector4f();
        position.mul(matrixStack.last().pose());

        final PoseStack renderSystemPoseStack = RenderSystem.getModelViewStack();
        renderSystemPoseStack.pushPose();
        renderSystemPoseStack.translate(position.x(), position.y(), 0);

        Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, 0, 0);

        renderSystemPoseStack.popPose();
        RenderSystem.applyModelViewMatrix();

        // Unfuck GL state.
        RenderSystem.enableBlend();
    }
}
