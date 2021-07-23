package li.cil.manual.api.prefab.item;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.ManualScreenStyle;
import li.cil.manual.api.ManualStyle;
import li.cil.manual.api.util.ShowManualScreenEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

/**
 * Base class that may be used for manual items, used as a representation of some manual and
 * to open said manual.
 */
public abstract class AbstractManualItem extends Item {
    protected AbstractManualItem(final Properties properties) {
        super(properties);
    }

    // --------------------------------------------------------------------- //
    // Item

    @Override
    public InteractionResult useOn(final UseOnContext context) {
        final Level world = context.getLevel();
        if (world.isClientSide()) {
            openManualFor(context, world);
        }
        return InteractionResult.sidedSuccess(world.isClientSide());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
        if (world.isClientSide()) {
            openManual(player);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }

    // --------------------------------------------------------------------- //

    /**
     * The manual instance represented by this item.
     *
     * @return the manual.
     */
    @OnlyIn(Dist.CLIENT)
    protected abstract ManualModel getManualModel();

    /**
     * The style used when displaying the manual.
     *
     * @return the style.
     */
    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected ManualStyle getManualStyle() {
        return null;
    }

    /**
     * The style used for the default manual screen.
     *
     * @return the screen style.
     */
    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected ManualScreenStyle getScreenStyle() {
        return null;
    }

    /**
     * Opens the {@link net.minecraft.client.gui.screens.Screen} used to display the manual represented by this item.
     * <p>
     * By default this will open the built-in manual screen, which can be customized using the style returned by
     * {@link #getManualStyle()} and {@link #getScreenStyle()}. To use a custom screen implementation, override this
     * method.
     */
    @OnlyIn(Dist.CLIENT)
    protected void showManualScreen() {
        MinecraftForge.EVENT_BUS.post(new ShowManualScreenEvent(getManualModel(), getManualStyle(), getScreenStyle()));
    }

    // --------------------------------------------------------------------- //

    @OnlyIn(Dist.CLIENT)
    private void openManualFor(final UseOnContext context, final Level world) {
        final ManualModel model = getManualModel();
        model.reset();
        model.pathFor(world, context.getClickedPos(), context.getClickedFace()).ifPresent(model::push);
        showManualScreen();
    }

    @OnlyIn(Dist.CLIENT)
    private void openManual(final Player player) {
        final ManualModel manual = getManualModel();
        if (player.isShiftKeyDown()) {
            manual.reset();
        }
        showManualScreen();
    }
}
