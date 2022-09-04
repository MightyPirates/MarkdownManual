package li.cil.manual.client.document.segment;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import li.cil.manual.api.render.FontRenderer;
import li.cil.manual.client.document.DocumentRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSegment extends AbstractSegment {
    private static final char[] BREAKS = {' ', '.', ',', ':', ';', '!', '?', '_', '=', '-', '+', '*', '/', '\\'};
    private static final CharSequence[] LISTS = {"- ", "* "};

    // ----------------------------------------------------------------------- //

    private final String text;
    private final List<TextBlock> blockCache = new ArrayList<>();
    private CacheKey blockCacheKey;
    private NextSegmentInfo nextCache;
    private CacheKey nextCacheKey;

    // ----------------------------------------------------------------------- //

    public TextSegment(final DocumentRenderer document, @Nullable final Segment parent, final String text) {
        super(document, parent);
        this.text = text;
    }

    // ----------------------------------------------------------------------- //

    @Override
    public int getLineHeight(final int indent, final int documentWidth) {
        return getLineHeight();
    }

    @Override
    public NextSegmentInfo getNext(final int segmentX, final int lineHeight, final int documentWidth) {
        final CacheKey cacheKey = new CacheKey(segmentX, lineHeight, documentWidth);
        if (!Objects.equals(cacheKey, nextCacheKey)) {
            nextCache = new NextSegmentInfo(next);
            forEachBlock(segmentX, lineHeight, documentWidth, block -> {
                nextCache.absoluteX = block.x + getStringWidth(block.chars);
                nextCache.relativeY = block.y;
            });

            // If the next segment belongs to a different hierarchy we force it to a new line.
            // This is mainly for stuff like lists.
            if (next != null && next.getLineRoot() != getLineRoot()) {
                nextCache.absoluteX = 0;
                if (nextCache.relativeY == 0) {
                    nextCache.relativeY = Math.max(lineHeight, getLineHeight());
                } else {
                    nextCache.relativeY += getLineHeight();
                }
            }

            nextCacheKey = cacheKey;
        }

        return nextCache;
    }

    @Override
    public Optional<InteractiveSegment> render(final PoseStack matrixStack, final int segmentX, final int lineHeight, final int documentWidth, final int mouseX, final int mouseY) {
        final String format = getFormat();
        final float scale = getFontScale() * getScale();
        final int color = getColor();

        final Optional<InteractiveSegment> interactive = getInteractiveParent();
        final ObjectReference<Optional<InteractiveSegment>> hovered = new ObjectReference<>(Optional.empty());

        final BufferBuilder builder = Tesselator.getInstance().getBuilder();
        final MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(builder);

        forEachBlock(segmentX, lineHeight, documentWidth, block -> {
            final int blockWidth = getStringWidth(block.chars);
            final int blockHeight = getLineHeight();
            if (hovered.value.isEmpty() &&
                mouseX >= block.x && mouseX <= block.x + blockWidth &&
                mouseY >= block.y && mouseY <= block.y + blockHeight) {
                hovered.value = interactive;
            }

            matrixStack.pushPose();
            matrixStack.translate(block.x, block.y, 0);
            matrixStack.scale(scale, scale, scale);

            getFont().drawBatch(matrixStack, bufferSource, format + block.chars, color);

            matrixStack.popPose();
        });

        bufferSource.endBatch();

        return hovered.value;
    }

    @Override
    public Iterable<Segment> refine(final Pattern pattern, final SegmentRefiner factory) {
        final List<Segment> result = new ArrayList<>();

        // Keep track of last matches end, to generate plain text segments.
        int textStart = 0;
        final Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            // Create segment for leading plain text.
            if (matcher.start() > textStart) {
                result.add(new TextSegment(document, this, text.substring(textStart, matcher.start())));
            }
            textStart = matcher.end();

            // Create segment for formatted text.
            result.add(factory.refine(document, this, matcher));
        }

        // Create segment for remaining plain text.
        if (textStart == 0) {
            result.add(this);
        } else if (textStart < text.length()) {
            result.add(new TextSegment(document, this, text.substring(textStart)));
        }
        return result;
    }

    @Override
    public String toString() {
        return text;
    }

    // ----------------------------------------------------------------------- //

    protected boolean isIgnoringLeadingWhitespace() {
        return true;
    }

    protected FontRenderer getFont() {
        return style.getRegularFont();
    }

    protected int getColor() {
        return tryGetFromParent(style.getRegularTextColor(), TextSegment::getColor);
    }

    protected float getScale() {
        return tryGetFromParent(1f, TextSegment::getScale);
    }

    protected String getFormat() {
        return tryGetFromParent("", TextSegment::getFormat);
    }

    protected int getLineHeight() {
        return (int) ((getFont().lineHeight() + 1) * getFontScale() * getScale());
    }

    // ----------------------------------------------------------------------- //

    private float getFontScale() {
        return style.getLineHeight() / (float) getFont().lineHeight();
    }

    private int getStringWidth(final CharSequence string) {
        return (int) (getFont().width(getFormat() + string) * getFontScale() * getScale());
    }

    private void forEachBlock(final int segmentX, final int lineHeight, final int documentWidth, final Consumer<TextBlock> blockConsumer) {
        final CacheKey cacheKey = new CacheKey(segmentX, lineHeight, documentWidth);
        if (!Objects.equals(cacheKey, blockCacheKey)) {
            blockCache.clear();

            String chars = text;
            if (isIgnoringLeadingWhitespace() && segmentX == 0) {
                chars = chars.substring(indexOfFirstNonWhitespace(chars));
            }

            final int wrappedIndent = computeWrappedIndent();
            int currentX = segmentX;
            int currentY = 0;

            int charCount = computeCharsFittingOnLine(chars, documentWidth - currentX, documentWidth - wrappedIndent);
            while (chars.length() > 0) {
                final String blockChars = chars.substring(0, charCount);
                blockCache.add(new TextBlock(
                    currentX,
                    currentY,
                    blockChars
                ));

                currentX = wrappedIndent;
                if (currentY == 0) {
                    currentY = Math.max(lineHeight, getLineHeight());
                } else {
                    currentY += getLineHeight();
                }

                chars = chars.substring(charCount);
                chars = chars.substring(indexOfFirstNonWhitespace(chars));
                charCount = computeCharsFittingOnLine(chars, documentWidth - currentX, documentWidth - wrappedIndent);
            }

            blockCacheKey = cacheKey;
        }

        for (final TextBlock block : blockCache) {
            blockConsumer.accept(block);
        }
    }

    private static int indexOfFirstNonWhitespace(final String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return i;
            }
        }
        return s.length();
    }

    private int computeCharsFittingOnLine(final String string, final int remainingLineWidth, final int documentWidth) {
        final int fullWidth = getStringWidth(string);

        int count = 0;
        int lastBreak = -1;
        while (count < string.length()) {
            final int nextLargerWidth = getStringWidth(string.substring(0, count + 1));
            final boolean exceedsLineLength = nextLargerWidth >= remainingLineWidth;
            if (exceedsLineLength) {
                final boolean mayUseFullLine = remainingLineWidth == documentWidth;
                final boolean canFitInLine = fullWidth <= documentWidth;
                final boolean matchesFullLine = fullWidth == documentWidth;
                if (lastBreak >= 0) {
                    return lastBreak + 1; // Can do a soft split.
                }
                if (mayUseFullLine && matchesFullLine) {
                    return string.length(); // Special case for exact match.
                }
                if (canFitInLine && !mayUseFullLine) {
                    return 0; // Wrap line, use next line.
                }
                return count; // Gotta split hard.
            }
            if (ArrayUtils.contains(BREAKS, string.charAt(count))) {
                lastBreak = count;
            }
            count += 1;
        }
        return count;
    }

    private <T> T tryGetFromParent(final T defaultValue, final Function<TextSegment, T> getter) {
        final Optional<Segment> parent = getParent();
        if (parent.isPresent() && parent.get() instanceof TextSegment textSegment) {
            return getter.apply(textSegment);
        } else {
            return defaultValue;
        }
    }

    private Optional<InteractiveSegment> getInteractiveParent() {
        Optional<Segment> optional = Optional.of(this);
        while (optional.isPresent()) {
            final Segment segment = optional.get();
            if (segment instanceof InteractiveSegment interactiveSegment) {
                return Optional.of(interactiveSegment);
            }
            optional = segment.getParent();
        }
        return Optional.empty();
    }

    private TextSegment getRootTextSegment() {
        TextSegment rootSegment = this;
        Optional<Segment> parent = getParent();
        while (parent.isPresent() && parent.get() instanceof TextSegment textSegment) {
            rootSegment = textSegment;
            parent = textSegment.getParent();
        }
        return rootSegment;
    }

    private int computeWrappedIndent() {
        final TextSegment textSegment = getRootTextSegment();
        final CharSequence rootPrefix = textSegment.text.subSequence(0, Math.min(2, textSegment.text.length()));
        return (ArrayUtils.contains(LISTS, rootPrefix)) ? getFont().width(rootPrefix) : 0;
    }

    // ----------------------------------------------------------------------- //

    private static final class ObjectReference<T> {
        public T value;

        public ObjectReference(final T value) {
            this.value = value;
        }
    }

    private record CacheKey(int segmentX, int lineHeight, int documentWidth) {
    }

    private record TextBlock(int x, int y, String chars) {
    }
}
