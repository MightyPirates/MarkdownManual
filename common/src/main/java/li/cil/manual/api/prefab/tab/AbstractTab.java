package li.cil.manual.api.prefab.tab;

import li.cil.manual.api.Tab;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractTab implements Tab {
    private final String path;
    @Nullable private final Component tooltip;

    public AbstractTab(final String path, @Nullable final Component tooltip) {
        this.path = path;
        this.tooltip = tooltip;
    }

    @Override
    public void getTooltip(final List<Component> tooltip) {
        if (this.tooltip != null) {
            tooltip.add(this.tooltip);
        }
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
