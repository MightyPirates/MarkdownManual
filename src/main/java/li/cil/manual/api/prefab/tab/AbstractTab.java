package li.cil.manual.api.prefab.tab;

import li.cil.manual.api.Tab;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractTab extends ForgeRegistryEntry<Tab> implements Tab {
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
