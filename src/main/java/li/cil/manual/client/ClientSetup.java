package li.cil.manual.client;

import li.cil.manual.api.ManualModel;
import li.cil.manual.api.ManualScreenStyle;
import li.cil.manual.api.ManualStyle;
import li.cil.manual.api.Tab;
import li.cil.manual.api.provider.DocumentProvider;
import li.cil.manual.api.provider.PathProvider;
import li.cil.manual.api.provider.RendererProvider;
import li.cil.manual.api.util.Constants;
import li.cil.manual.api.util.ShowManualScreenEvent;
import li.cil.manual.client.gui.ManualScreen;
import li.cil.manual.client.provider.BlockRendererProvider;
import li.cil.manual.client.provider.ItemRendererProvider;
import li.cil.manual.client.provider.TagRendererProvider;
import li.cil.manual.client.provider.TextureRendererProvider;
import li.cil.manual.client.util.RegistryUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

@OnlyIn(Dist.CLIENT)
public final class ClientSetup {
    public static void initialize() {
        RegistryUtils.begin(Constants.MOD_ID);

        final DeferredRegister<Tab> tabs = RegistryUtils.get(Constants.TAB_REGISTRY);
        final DeferredRegister<PathProvider> pathProviders = RegistryUtils.get(Constants.PATH_PROVIDER_REGISTRY);
        final DeferredRegister<DocumentProvider> documentProviders = RegistryUtils.get(Constants.DOCUMENT_PROVIDER_REGISTRY);
        final DeferredRegister<RendererProvider> rendererProviders = RegistryUtils.get(Constants.RENDERER_PROVIDER_REGISTRY);
        final DeferredRegister<ManualModel> manuals = RegistryUtils.get(Constants.MANUAL_REGISTRY);

        makeClientOnlyRegistry(pathProviders);
        makeClientOnlyRegistry(documentProviders);
        makeClientOnlyRegistry(rendererProviders);
        makeClientOnlyRegistry(tabs);
        makeClientOnlyRegistry(manuals);

        rendererProviders.register("texture", TextureRendererProvider::new);
        rendererProviders.register("item", ItemRendererProvider::new);
        rendererProviders.register("block", BlockRendererProvider::new);
        rendererProviders.register("tag", TagRendererProvider::new);

        RegistryUtils.finish();

        MinecraftForge.EVENT_BUS.addListener(ClientSetup::handleShowManualScreen);
    }

    // --------------------------------------------------------------------- //

    private static void handleShowManualScreen(final ShowManualScreenEvent event) {
        final ManualModel model = event.getManualModel();
        final ManualStyle manualStyle = event.getManualStyle().orElse(ManualStyle.DEFAULT);
        final ManualScreenStyle screenStyle = event.getScreenStyle().orElse(ManualScreenStyle.DEFAULT);

        final ManualScreen screen = new ManualScreen(model, manualStyle, screenStyle);
        Minecraft.getInstance().setScreen(screen);
    }

    private static <T> void makeClientOnlyRegistry(final DeferredRegister<T> deferredRegister) {
        deferredRegister.makeRegistry(() -> new RegistryBuilder<T>().disableSync().disableSaving());
    }
}
