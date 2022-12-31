package li.cil.manual.client.document.segment.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import li.cil.manual.api.render.ContentRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;

public final class ItemStackContentRenderer implements ContentRenderer {
    /**
     * How long to show individual stacks, in milliseconds, before switching to the next.
     */
    private static final int CYCLE_SPEED = 1000;

    // --------------------------------------------------------------------- //

    private final ItemStack[] stacks;

    // --------------------------------------------------------------------- //

    public ItemStackContentRenderer(final ItemStack... stacks) {
        this.stacks = stacks;
    }

    // --------------------------------------------------------------------- //

    @Override
    public int getWidth() {
        return 32;
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public void render(final PoseStack matrixStack, final int mouseX, final int mouseY) {
        final Minecraft mc = Minecraft.getInstance();
        final int index = (int) (System.currentTimeMillis() % (CYCLE_SPEED * stacks.length)) / CYCLE_SPEED;
        final ItemStack stack = stacks[index];

        final float scaleX = getWidth() / 16f;
        final float scaleY = getHeight() / 16f;

        // This is *nasty*, but sadly there's no renderItemAndEffectIntoGUI() variant that
        // takes a MatrixStack. Yet.

        final var position = new Vector4f();
        position.mul(matrixStack.last().pose());

        final PoseStack renderSystemPoseStack = RenderSystem.getModelViewStack();
        renderSystemPoseStack.pushPose();
        renderSystemPoseStack.translate(position.x(), position.y(), 0);
        renderSystemPoseStack.scale(scaleX, scaleY, 1);

        mc.getItemRenderer().renderGuiItem(stack, 0, 0);

        renderSystemPoseStack.popPose();
        RenderSystem.applyModelViewMatrix();

        // Unfuck GL state.
        RenderSystem.enableBlend();
    }
}
