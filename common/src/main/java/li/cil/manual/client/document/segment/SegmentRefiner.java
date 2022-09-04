package li.cil.manual.client.document.segment;

import li.cil.manual.client.document.DocumentRenderer;

import java.util.regex.Matcher;

@FunctionalInterface
public interface SegmentRefiner {
    Segment refine(final DocumentRenderer document, final Segment segment, final Matcher matcher);
}
