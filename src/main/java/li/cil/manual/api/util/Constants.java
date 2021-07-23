package li.cil.manual.api.util;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.Tab;
import li.cil.manual.api.provider.DocumentProvider;
import li.cil.manual.api.provider.PathProvider;
import li.cil.manual.api.provider.RendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class Constants {
    public static final String MOD_ID = "markdown_manual";

    // ----------------------------------------------------------------------- //

    public static final ResourceKey<Registry<PathProvider>> PATH_PROVIDERS = key("path_providers");
    public static final ResourceKey<Registry<DocumentProvider>> DOCUMENT_PROVIDERS = key("document_providers");
    public static final ResourceKey<Registry<RendererProvider>> RENDERER_PROVIDERS = key("renderer_providers");
    public static final ResourceKey<Registry<Tab>> TABS = key("tabs");
    public static final ResourceKey<Registry<ManualModel>> MANUALS = key("manuals");

    // ----------------------------------------------------------------------- //

    private static <T> ResourceKey<Registry<T>> key(final String name) {
        return ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, name));
    }

    // ----------------------------------------------------------------------- //

    private Constants() {
    }
}
