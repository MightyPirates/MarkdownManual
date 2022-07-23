package li.cil.manual.api.util;

import li.cil.manual.api.ManualModel;

/**
 * Result type for {@link MarkdownManualRegistryEntry#matches(ManualModel)} to determine if an entry is valid for a
 * {@link ManualModel}.
 */
public enum MatchResult {
    PASS,
    MATCH,
    MISMATCH
}
