package li.cil.manual.client.document.segment.render;

import li.cil.manual.api.render.InteractiveContentRenderer;
import li.cil.manual.api.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MissingContentRenderer extends TextureContentRenderer implements InteractiveContentRenderer {
    private static final ResourceLocation LOCATION_GUI_MANUAL_MISSING = new ResourceLocation(Constants.MOD_ID, "textures/gui/missing.png");

    private final ITextComponent tooltip;

    // --------------------------------------------------------------------- //

    public MissingContentRenderer(final ITextComponent tooltip) {
        super(LOCATION_GUI_MANUAL_MISSING);
        this.tooltip = tooltip;
    }

    // --------------------------------------------------------------------- //

    @Override
    public ITextComponent getTooltip(final ITextComponent tooltip) {
        return this.tooltip;
    }
}
