package li.cil.manual.api.prefab;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.Tab;
import li.cil.manual.api.content.Document;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.api.util.Constants;
import li.cil.manual.api.util.MarkdownManualRegistryEntry;
import li.cil.manual.api.util.PathUtils;
import li.cil.manual.client.document.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Simple implementation of the {@link ManualModel} interface which should cover most use-cases.
 */
@OnlyIn(Dist.CLIENT)
public class Manual extends ForgeRegistryEntry<ManualModel> implements ManualModel {
    /**
     * The magic first characters indicating a redirect in a document, with the target path following.
     */
    private static final String REDIRECT_PRAGMA = "#redirect ";

    // ----------------------------------------------------------------------- //

    /**
     * The current navigation history for this manual.
     */
    protected final List<History> history = new ArrayList<>();

    /**
     * The current index in the navigation history. We keep pages we came back from
     * at the back of the list to allow navigating back forwards, retaining the stored
     * scroll offset of these pages.
     */
    protected int historyIndex;

    // ----------------------------------------------------------------------- //

    public Manual() {
        reset();
    }

    // ----------------------------------------------------------------------- //

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> pathFor(final ItemStack stack) {
        return find(Constants.PATH_PROVIDER_REGISTRY, provider -> provider.pathFor(stack));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> pathFor(final Level world, final BlockPos pos, final Direction face) {
        return find(Constants.PATH_PROVIDER_REGISTRY, provider -> provider.pathFor(world, pos, face));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Document> documentFor(final String path) {
        final String language = Minecraft.getInstance().getLanguageManager().getSelected().getCode();
        final Optional<Document> document = documentFor(path.replace(LANGUAGE_KEY, language), language, new LinkedHashSet<>());
        return document.isPresent() ? document : documentFor(path.replace(LANGUAGE_KEY, FALLBACK_LANGUAGE), FALLBACK_LANGUAGE, new LinkedHashSet<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ContentRenderer> imageFor(final String path) {
        return find(Constants.RENDERER_PROVIDER_REGISTRY, provider -> provider.getRenderer(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Tab> getTabs() {
        return StreamSupport.stream(RegistryManager.ACTIVE.getRegistry(Constants.TAB_REGISTRY).spliterator(), false).
            filter(tab -> tab.matches(this)).
            collect(Collectors.toList());
    }

    // ----------------------------------------------------------------------- //

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        history.clear();
        history.add(createHistoryEntry(StringUtils.stripStart(getStartPage(), "/")));
        historyIndex = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void push(final String path) {
        if (path.startsWith("/")) throw new IllegalArgumentException("Path must not start with a slash.");
        if (Objects.equals(history.get(historyIndex).path, path)) {
            return;
        }

        // Try to re-use "future" entry.
        if (history.size() > historyIndex + 1 && Objects.equals(history.get(historyIndex + 1).path, path)) {
            historyIndex++;
            return;
        }

        // Remove "future" entries we kept to navigate back forwards.
        while (history.size() > historyIndex + 1) {
            history.remove(history.size() - 1);
        }

        history.add(createHistoryEntry(path));
        historyIndex++;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean pop() {
        if (historyIndex <= 0) {
            return false;
        }

        historyIndex--;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String peek() {
        return history.get(historyIndex).path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolve(final String path) {
        return PathUtils.resolve(peek(), path);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getUserData(final Class<T> type) {
        return Optional.ofNullable((T) history.get(historyIndex).userData.get(type));
    }

    /**
     * {@inheritDoc}
     */
    public <T> void setUserData(final T value) {
        history.get(historyIndex).userData.put(value.getClass(), value);
    }

    // ----------------------------------------------------------------------- //

    protected String getStartPage() {
        return LANGUAGE_KEY + "/index.md";
    }

    protected History createHistoryEntry(final String path) {
        return new History(path);
    }

    protected <TProvider extends MarkdownManualRegistryEntry<TProvider>, TResult> Optional<TResult> find(final ResourceKey<Registry<TProvider>> key, final Function<TProvider, Optional<TResult>> lookup) {
        return RegistryManager.ACTIVE.getRegistry(key).getValues().stream().
            filter(provider -> provider.matches(this)).
            sorted().map(lookup).filter(Optional::isPresent).findFirst().flatMap(x -> x);
    }

    /**
     * Loads the document from the specified path, in the specified language.
     * <p>
     * This method may perform additional processing on the loaded documents. The default implementation
     * will resolve redirects and trim leading and trailing blank lines.
     *
     * @param path     the path of the document to load.
     * @param language the language of the document to load.
     * @param seen     a set of already seen documents in the loading process, used to break cycles in redirects.
     * @return the loaded document.
     */
    protected Optional<Document> documentFor(final String path, final String language, final Set<String> seen) {
        if (!seen.add(path)) {
            final List<String> message = new ArrayList<>();
            message.add(Strings.getRedirectionLoopText());
            message.addAll(seen);
            message.add(path);
            return Optional.of(new Document(message));
        }

        return find(Constants.DOCUMENT_PROVIDER_REGISTRY, provider -> provider.getDocument(path, language)).flatMap(document -> {
            // Read first line only to check for redirect.
            final List<String> lines = document.getLines();
            if (!lines.isEmpty() && lines.get(0).toLowerCase().startsWith(REDIRECT_PRAGMA)) {
                final String redirectPath = lines.get(0).substring(REDIRECT_PRAGMA.length()).trim();
                return documentFor(PathUtils.resolve(path, redirectPath), language, seen);
            }

            // Trim leading and trailing blank lines. Mostly for the trailing ones, to avoid scrolling being weird.
            while (!lines.isEmpty() && StringUtils.isWhitespace(lines.get(0))) {
                lines.remove(0);
            }
            while (!lines.isEmpty() && StringUtils.isWhitespace(lines.get(lines.size() - 1))) {
                lines.remove(lines.size() - 1);
            }
            return Optional.of(document);
        });
    }

    // --------------------------------------------------------------------- //

    protected static class History {
        public final String path;
        public final Map<Class<?>, Object> userData = new HashMap<>();

        public History(final String path) {
            this.path = path;
        }
    }
}
