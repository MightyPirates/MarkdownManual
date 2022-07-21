package li.cil.manual.client.provider;

import com.machinezoo.noexception.optional.OptionalBoolean;
import li.cil.manual.api.ManualModel;
import li.cil.manual.api.prefab.provider.AbstractRendererProvider;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.client.document.Strings;
import li.cil.manual.client.document.segment.render.ItemStackContentRenderer;
import li.cil.manual.client.document.segment.render.MissingContentRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public final class ItemRendererProvider extends AbstractRendererProvider {
    public ItemRendererProvider() {
        super("item");
    }

    // --------------------------------------------------------------------- //

    @Override
    public OptionalBoolean matches(final ManualModel manual) {
        return OptionalBoolean.of(true);
    }

    @Override
    protected Optional<ContentRenderer> doGetRenderer(final String data) {
        final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(data));
        if (item != null && item != Items.AIR) {
            return Optional.of(new ItemStackContentRenderer(new ItemStack(item)));
        } else {
            return Optional.of(new MissingContentRenderer(Strings.NO_SUCH_ITEM));
        }
    }
}
