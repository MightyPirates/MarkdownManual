package li.cil.manual.api.content;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public final class Document {
    private final List<String> lines;
    @Nullable private final ResourceLocation location;

    public Document(final List<String> lines, @Nullable final ResourceLocation location) {
        this.location = location;
        this.lines = lines;
    }

    public Document(final List<String> lines) {
        this(lines, null);
    }

    public List<String> getLines() {
        return lines;
    }

    @Nullable
    public ResourceLocation getLocation() {
        return location;
    }
}
