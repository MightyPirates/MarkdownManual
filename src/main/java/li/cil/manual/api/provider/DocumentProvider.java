package li.cil.manual.api.provider;

import li.cil.manual.api.content.Document;
import li.cil.manual.api.prefab.provider.NamespaceDocumentProvider;
import li.cil.manual.api.util.MarkdownManualRegistryEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

/**
 * This interface allows implementation of document providers for the manual.
 * <p>
 * Document providers can be used to provide (optionally dynamic) page content for
 * arbitrary paths.
 *
 * @see NamespaceDocumentProvider
 */
@OnlyIn(Dist.CLIENT)
public interface DocumentProvider extends MarkdownManualRegistryEntry<DocumentProvider> {
    /**
     * Called to get the content of a path pointed to by the specified path.
     * <p>
     * This should provide an iterable over the lines of a Markdown document
     * (with the formatting provided by the in-game manual, which is a small
     * subset of "normal" Markdown).
     * <p>
     * If this provider cannot provide the requested path, it should return
     * {@code null} to indicate so, allowing other providers to be queried.
     *
     * @param path     the path to the manual page we're looking for.
     * @param language the language of the content to look up.
     * @return the content of the document at that path, or {@code null}.
     */
    Optional<Document> getDocument(final String path, final String language);
}
