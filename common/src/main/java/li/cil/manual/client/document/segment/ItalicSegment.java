package li.cil.manual.client.document.segment;

import li.cil.manual.client.document.DocumentRenderer;
import net.minecraft.ChatFormatting;

public final class ItalicSegment extends TextSegment {
    public ItalicSegment(final DocumentRenderer document, final Segment parent, final String text) {
        super(document, parent, text);
    }

    // --------------------------------------------------------------------- //

    @Override
    public String toString() {
        return String.format("*%s*", super.toString());
    }

    // --------------------------------------------------------------------- //

    @Override
    protected String getFormat() {
        return super.getFormat() + ChatFormatting.ITALIC;
    }
}
