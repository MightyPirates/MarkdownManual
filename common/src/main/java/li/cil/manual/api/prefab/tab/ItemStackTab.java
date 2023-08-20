package li.cil.manual.api.prefab.tab;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

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
    public void renderIcon(final GuiGraphics graphics) {
        graphics.renderFakeItem(stack, 0, 0);
    }
}
