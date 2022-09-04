package li.cil.manual.client.document.segment.render;

import li.cil.manual.api.render.InteractiveContentRenderer;
import li.cil.manual.api.util.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class MissingContentRenderer extends TextureContentRenderer implements InteractiveContentRenderer {
    private static final ResourceLocation LOCATION_GUI_MANUAL_MISSING = new ResourceLocation(Constants.MOD_ID, "textures/gui/missing.png");

    private final Component tooltip;

    // --------------------------------------------------------------------- //

    public MissingContentRenderer(final Component tooltip) {
        super(LOCATION_GUI_MANUAL_MISSING);
        this.tooltip = tooltip;
    }

    // --------------------------------------------------------------------- //

    @Override
    public Component getTooltip(final Component tooltip) {
        return this.tooltip;
    }
}
