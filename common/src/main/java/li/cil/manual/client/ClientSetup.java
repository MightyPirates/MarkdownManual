package li.cil.manual.client;

import dev.architectury.registry.registries.DeferredRegister;
import li.cil.manual.api.ManualModel;
import li.cil.manual.api.ManualScreenStyle;
import li.cil.manual.api.ManualStyle;
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

public final class ClientSetup {
    public static void initialize() {
        RegistryUtils.begin(Constants.MOD_ID);

        RegistryUtils.builder(Constants.TAB_REGISTRY).build();
        RegistryUtils.builder(Constants.PATH_PROVIDER_REGISTRY).build();
        RegistryUtils.builder(Constants.DOCUMENT_PROVIDER_REGISTRY).build();
        RegistryUtils.builder(Constants.RENDERER_PROVIDER_REGISTRY).build();
        RegistryUtils.builder(Constants.MANUAL_REGISTRY).build();

        final DeferredRegister<RendererProvider> rendererProviders = RegistryUtils.get(Constants.RENDERER_PROVIDER_REGISTRY);

        rendererProviders.register("texture", TextureRendererProvider::new);
        rendererProviders.register("item", ItemRendererProvider::new);
        rendererProviders.register("block", BlockRendererProvider::new);
        rendererProviders.register("tag", TagRendererProvider::new);

        RegistryUtils.finish();

        ShowManualScreenEvent.SHOW_MANUAL_SCREEN_EVENT.register(ClientSetup::handleShowManualScreen);
    }

    // --------------------------------------------------------------------- //

    private static void handleShowManualScreen(final ShowManualScreenEvent event) {
        final ManualModel model = event.getManualModel();
        final ManualStyle manualStyle = event.getManualStyle().orElse(ManualStyle.DEFAULT);
        final ManualScreenStyle screenStyle = event.getScreenStyle().orElse(ManualScreenStyle.DEFAULT);

        final ManualScreen screen = new ManualScreen(model, manualStyle, screenStyle);
        Minecraft.getInstance().setScreen(screen);
    }
}
