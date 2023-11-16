package li.cil.manual.client.fabric;

import li.cil.manual.api.platform.FabricManualInitializer;
import li.cil.manual.client.ClientSetup;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class MarkdownManualFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientSetup.initialize();

        FabricLoader.getInstance()
            .getEntrypoints("markdown_manual:registration", FabricManualInitializer.class)
            .forEach(FabricManualInitializer::registerManualObjects);
    }
}
