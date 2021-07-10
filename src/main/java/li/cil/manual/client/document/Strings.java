package li.cil.manual.client.document;

import li.cil.manual.api.util.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class Strings {
    public static final ITextComponent NO_SUCH_IMAGE = new TranslationTextComponent(Constants.MOD_ID + ".warning.missing.image");
    public static final ITextComponent NO_SUCH_ITEM = new TranslationTextComponent(Constants.MOD_ID + ".warning.missing.item");
    public static final ITextComponent NO_SUCH_BLOCK = new TranslationTextComponent(Constants.MOD_ID + ".warning.missing.block");
    public static final ITextComponent NO_SUCH_TAG = new TranslationTextComponent(Constants.MOD_ID + ".warning.missing.tag");

    public static String getMissingContentText(final String url) {
        return I18n.get(Constants.MOD_ID + ".warning.missing.content_render", url);
    }

    public static String getRedirectionLoopText() {
        return I18n.get(Constants.MOD_ID + ".warning.redirection_loop");
    }

    // --------------------------------------------------------------------- //

    private Strings() {
    }
}
