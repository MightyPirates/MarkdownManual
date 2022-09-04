package li.cil.manual.client.document.segment;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.ManualStyle;
import li.cil.manual.client.document.DocumentRenderer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

abstract class AbstractSegment implements Segment {
    protected final DocumentRenderer document;
    protected final ManualModel model;
    protected final ManualStyle style;
    @Nullable private final Segment parent;
    protected Segment next;

    // --------------------------------------------------------------------- //

    protected AbstractSegment(final DocumentRenderer document, @Nullable final Segment parent) {
        this.document = document;
        this.model = document.getModel();
        this.style = document.getStyle();
        this.parent = parent;
    }

    // --------------------------------------------------------------------- //

    @Override
    public Segment getLineRoot() {
        return parent != null ? parent.getLineRoot() : this;
    }

    @Override
    public Optional<Segment> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Iterable<Segment> refine(final Pattern pattern, final SegmentRefiner refiner) {
        return Collections.singletonList(this);
    }

    @Override
    public void setNext(@Nullable final Segment segment) {
        next = segment;
    }
}
