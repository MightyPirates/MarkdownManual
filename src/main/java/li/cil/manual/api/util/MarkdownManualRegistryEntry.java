package li.cil.manual.api.util;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.provider.DocumentProvider;
import li.cil.manual.api.provider.PathProvider;
import li.cil.manual.api.provider.RendererProvider;

/**
 * This is used when associating various registry objects, such as {@link DocumentProvider}s,
 * {@link PathProvider}s and {@link RendererProvider}s with manuals that may need some particular
 * order in which they are queried, e.g. to overwrite some other existing provider.
 *
 * @param <T> the generic type of the entry.
 */
public interface MarkdownManualRegistryEntry extends Comparable<MarkdownManualRegistryEntry> {
    /**
     * Checks if this instance applies to the specified manual and should be used
     * in the internal logic of the manual, such as looking up paths and content.
     *
     * @param manual the manual to check.
     * @return {@link MatchResult#MATCH} if this instance applies to the manual,
     * {@link MatchResult#MISMATCH} if not, {@link MatchResult#PASS} if the default
     * heuristic should be used.
     */
    default MatchResult matches(final ManualModel manual) {
        return MatchResult.PASS;
    }

    /**
     * The sort order of this instance.
     * <p>
     * Registry entries are sorted using this order before being queried. For example,
     * this may be used by {@link DocumentProvider}s to replace a more generic
     * default provider.
     *
     * @return the sort order of this instance.
     */
    default int sortOrder() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(final MarkdownManualRegistryEntry other) {
        return Integer.compare(sortOrder(), other.sortOrder());
    }
}
