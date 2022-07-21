package li.cil.manual.client.provider;

import com.machinezoo.noexception.optional.OptionalBoolean;
import li.cil.manual.api.ManualModel;
import li.cil.manual.api.prefab.provider.AbstractRendererProvider;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.client.document.Strings;
import li.cil.manual.client.document.segment.render.ItemStackContentRenderer;
import li.cil.manual.client.document.segment.render.MissingContentRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public final class TagRendererProvider extends AbstractRendererProvider {
    public TagRendererProvider() {
        super("tag");
    }

    // --------------------------------------------------------------------- //

    @Override
    public OptionalBoolean matches(final ManualModel manual) {
        return OptionalBoolean.of(true);
    }

    @Override
    protected Optional<ContentRenderer> doGetRenderer(final String data) {
        ITagManager<Item> itemTags = ForgeRegistries.ITEMS.tags();
        if (itemTags == null) {
            return Optional.of(new MissingContentRenderer(Strings.NO_SUCH_TAG));
        }

        TagKey<Item> tagKey = itemTags.createTagKey(new ResourceLocation(data));
        if (!itemTags.isKnownTagName(tagKey)) {
            return Optional.of(new MissingContentRenderer(Strings.NO_SUCH_TAG));
        }

        final ITag<Item> tag = itemTags.getTag(tagKey);
        if (tag.isEmpty()) {
            return Optional.of(new MissingContentRenderer(Strings.NO_SUCH_TAG));
        }

        return Optional.of(new ItemStackContentRenderer(tag
            .stream()
            .map(ItemStack::new)
            .toArray(ItemStack[]::new)));
    }
}
