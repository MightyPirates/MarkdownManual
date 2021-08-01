package li.cil.manual.client.provider;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.provider.RendererProvider;
import li.cil.manual.api.render.ContentRenderer;
import li.cil.manual.client.document.Strings;
import li.cil.manual.client.document.segment.render.MissingContentRenderer;
import li.cil.manual.client.document.segment.render.TextureContentRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public final class TextureRendererProvider extends ForgeRegistryEntry<RendererProvider> implements RendererProvider {
    private static final String[] EXTENSIONS = {".png", ".gif", ".jpg", ".jpeg", ".bmp", ".tga"};

    @Override
    public int sortOrder() {
        return 10000;
    }

    @Override
    public boolean matches(final ManualModel manual) {
        return true;
    }

    @Override
    public Optional<ContentRenderer> getRenderer(final String path) {
        if (!hasSupportedExtension(path)) {
            return Optional.empty();
        }

        try {
            return Optional.of(new TextureContentRenderer(new ResourceLocation(path)));
        } catch (final Throwable t) {
            return Optional.of(new MissingContentRenderer(Strings.NO_SUCH_IMAGE));
        }
    }

    private static boolean hasSupportedExtension(final String path) {
        for (final String extension : EXTENSIONS) {
            if (path.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
