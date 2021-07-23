package li.cil.manual.api.provider;

import li.cil.manual.api.prefab.Manual;
import li.cil.manual.api.util.MarkdownManualRegistryEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

/**
 * Allows providing paths for item stacks and blocks in the world.
 * <p>
 * For example, this is used for opening the manual when held in hand and
 * sneak-activating a block in the world.
 * <p>
 * Note that you can use the special variable {@link Manual#LANGUAGE_KEY} in your
 * paths for language agnostic paths. These will be resolved to the currently
 * set language, falling back to {@link Manual#FALLBACK_LANGUAGE}, during actual
 * content lookup.
 */
@OnlyIn(Dist.CLIENT)
public interface PathProvider extends MarkdownManualRegistryEntry<PathProvider> {
    /**
     * Get the path to the documentation page for the provided item stack.
     * <p>
     * Return {@code null} if there is no known page for this item, allowing
     * other providers to be queried.
     *
     * @param stack the stack to get the documentation path to.
     * @return the path to the page, {@code null} if none is known.
     */
    Optional<String> pathFor(final ItemStack stack);

    /**
     * Get the path to the documentation page for the provided block.
     * <p>
     * Return {@code null} if there is no known page for this item, allowing
     * other providers to be queried.
     *
     * @param world the world containing the block.
     * @param pos   the position coordinate of the block.
     * @param face  the face of the block.
     * @return the path to the page, {@code null} if none is known.
     */
    Optional<String> pathFor(final Level world, final BlockPos pos, final Direction face);
}
