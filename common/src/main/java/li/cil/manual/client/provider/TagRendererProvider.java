package li.cil.manual.client.provider;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.prefab.provider.AbstractRendererProvider;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.api.util.MatchResult;
import li.cil.manual.client.document.Strings;
import li.cil.manual.client.document.segment.render.ItemStackContentRenderer;
import li.cil.manual.client.document.segment.render.MissingContentRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class TagRendererProvider extends AbstractRendererProvider {
    public TagRendererProvider() {
        super("tag");
    }

    // --------------------------------------------------------------------- //

    @Override
    public MatchResult matches(final ManualModel manual) {
        return MatchResult.MATCH;
    }

    @Override
    protected Optional<ContentRenderer> doGetRenderer(final String data) {
        final ResourceLocation location = new ResourceLocation(data);
        return BuiltInRegistries.ITEM.getTagNames()
            .filter(key -> key.location().equals(location))
            .findFirst()
            .flatMap(BuiltInRegistries.ITEM::getTag)
            .map(tag -> (ContentRenderer) new ItemStackContentRenderer(tag
                .stream()
                .map(ItemStack::new)
                .toArray(ItemStack[]::new)
            ))
            .or(() -> Optional.of(new MissingContentRenderer(Strings.NO_SUCH_TAG)));
    }
}
