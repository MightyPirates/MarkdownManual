package li.cil.manual.client.provider;

import li.cil.manual.api.content.Document;
import li.cil.manual.api.provider.ContentProvider;
import li.cil.manual.api.provider.DocumentProvider;
import li.cil.manual.api.util.Constants;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DeprecatedContentProvider extends ForgeRegistryEntry<DocumentProvider> implements DocumentProvider {
    @Override
    public int sortOrder() {
        return 10000;
    }

    @Override
    public Optional<Document> getDocument(final String path, final String language) {
        final ForgeRegistry<ContentProvider> registry = RegistryManager.ACTIVE.getRegistry(Constants.CONTENT_PROVIDERS);
        final List<ContentProvider> providers = registry.getValues().stream().sorted().collect(Collectors.toList());
        for (final ContentProvider provider : providers) {
            final Optional<Stream<String>> content = provider.getContent(path, language);
            if (content.isPresent()) {
                return Optional.of(new Document(content.get().collect(Collectors.toList())));
            }
        }
        return Optional.empty();
    }
}
