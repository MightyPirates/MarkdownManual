package li.cil.manual.client.document.segment.render;

import li.cil.manual.api.render.ContentRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

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
    public void render(final GuiGraphics graphics, final int mouseX, final int mouseY) {
        final int index = (int) (System.currentTimeMillis() % (CYCLE_SPEED * stacks.length)) / CYCLE_SPEED;
        final ItemStack stack = stacks[index];

        final float scaleX = getWidth() / 16f;
        final float scaleY = getHeight() / 16f;

        final var pose = graphics.pose();

        pose.pushPose();
        pose.scale(scaleX, scaleY, 1);

        graphics.renderFakeItem(stack, 0, 0);

        pose.popPose();
    }
}
