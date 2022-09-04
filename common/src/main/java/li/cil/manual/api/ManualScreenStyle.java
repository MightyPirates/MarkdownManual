package li.cil.manual.api;

import li.cil.manual.api.util.Constants;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

/**
 * Style definition that may be used when using the built-in manual
 * screen and requiring the option for additional customizations.
 */
public interface ManualScreenStyle {
    /**
     * Default implementation of a screen style.
     */
    ManualScreenStyle DEFAULT = new ManualScreenStyle() {
    };

    default ResourceLocation getWindowBackground() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/manual.png");
    }

    default ResourceLocation getScrollButtonTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/scroll_button.png");
    }

    default ResourceLocation getTabButtonTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/tab_button.png");
    }

    default Rect2i getWindowRect() {
        return new Rect2i(0, 0, 256, 256);
    }

    default Rect2i getDocumentRect() {
        return new Rect2i(24, 16, 208, 216);
    }

    default Rect2i getScrollBarRect() {
        return new Rect2i(250, 16, 20, 216);
    }

    default Rect2i getScrollButtonRect() {
        return new Rect2i(0, 0, 20, 12);
    }

    default Rect2i getTabAreaRect() {
        return new Rect2i(-52, 25, 64, 216);
    }

    default Rect2i getTabRect() {
        return new Rect2i(0, 0, 64, 24);
    }

    default int getTabOverlap() {
        return 4;
    }

    default int getTabHoverShift() {
        return 20;
    }
}
