package li.cil.manual.api.prefab.provider;

import li.cil.manual.api.provider.RendererProvider;
import li.cil.manual.api.render.ContentRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

/**
 * Utility base class for {@link RendererProvider}s, taking care of the prefix check.
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractRendererProvider implements RendererProvider {
    private final String prefix;

    public AbstractRendererProvider(final String prefix) {
        this.prefix = prefix + ":";
    }

    @Override
    public Optional<ContentRenderer> getRenderer(final String path) {
        if (path.startsWith(prefix)) {
            return doGetRenderer(path.substring(prefix.length()));
        } else {
            return Optional.empty();
        }
    }

    protected abstract Optional<ContentRenderer> doGetRenderer(final String data);
}
