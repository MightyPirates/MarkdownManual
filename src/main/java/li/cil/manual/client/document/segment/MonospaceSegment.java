package li.cil.manual.client.document.segment;

import li.cil.manual.api.render.FontRenderer;
import li.cil.manual.client.document.DocumentRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MonospaceSegment extends TextSegment {
    public MonospaceSegment(final DocumentRenderer document, final Segment parent, final String text) {
        super(document, parent, text);
    }

    // --------------------------------------------------------------------- //

    @Override
    public String toString() {
        return String.format("`%s`", super.toString());
    }

    // --------------------------------------------------------------------- //

    @Override
    protected boolean isIgnoringLeadingWhitespace() {
        return false;
    }

    @Override
    protected int getColor() {
        return style.getMonospaceTextColor();
    }

    @Override
    protected FontRenderer getFont() {
        return style.getMonospaceFont();
    }
}
