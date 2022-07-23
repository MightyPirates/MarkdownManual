package li.cil.manual.client.provider;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import li.cil.manual.api.ManualModel;
import li.cil.manual.api.prefab.provider.AbstractRendererProvider;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.api.util.MatchResult;
import li.cil.manual.client.document.Strings;
import li.cil.manual.client.document.segment.render.ItemStackContentRenderer;
import li.cil.manual.client.document.segment.render.MissingContentRenderer;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public final class BlockRendererProvider extends AbstractRendererProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, BlockState> BLOCK_STATE_CACHE = new HashMap<>();

    // --------------------------------------------------------------------- //

    public BlockRendererProvider() {
        super("block");
    }

    // --------------------------------------------------------------------- //

    @Override
    public MatchResult matches(final ManualModel manual) {
        return MatchResult.MATCH;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Optional<ContentRenderer> doGetRenderer(final String data) {
        final BlockState state = Objects.requireNonNull(BLOCK_STATE_CACHE.computeIfAbsent(data, (string) -> {
            try {
                return BlockStateParser.parseForBlock(Registry.BLOCK, new StringReader(string), false).blockState();
            } catch (final CommandSyntaxException e) {
                LOGGER.error("Failed parsing block state.", e);
                return Blocks.AIR.defaultBlockState();
            }
        }));

        if (state.getBlock() != Blocks.AIR) {
            return Optional.of(new ItemStackContentRenderer(new ItemStack(state.getBlock())));
        } else {
            return Optional.of(new MissingContentRenderer(Strings.NO_SUCH_BLOCK));
        }
    }
}
