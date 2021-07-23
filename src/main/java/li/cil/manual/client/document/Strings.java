package li.cil.manual.client.document;

import li.cil.manual.api.util.Constants;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public final class Strings {
    public static final Component NO_SUCH_IMAGE = new TranslatableComponent(Constants.MOD_ID + ".warning.missing.image");
    public static final Component NO_SUCH_ITEM = new TranslatableComponent(Constants.MOD_ID + ".warning.missing.item");
    public static final Component NO_SUCH_BLOCK = new TranslatableComponent(Constants.MOD_ID + ".warning.missing.block");
    public static final Component NO_SUCH_TAG = new TranslatableComponent(Constants.MOD_ID + ".warning.missing.tag");

    public static Component getMissingContentText(final String url) {
        return new TranslatableComponent(Constants.MOD_ID + ".warning.missing.content_renderer", url);
    }

    public static String getRedirectionLoopText() {
        return I18n.get(Constants.MOD_ID + ".warning.redirection_loop");
    }

    // --------------------------------------------------------------------- //

    private Strings() {
    }
}
