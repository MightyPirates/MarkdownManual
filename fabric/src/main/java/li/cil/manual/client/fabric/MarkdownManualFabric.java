package li.cil.manual.client.fabric;

import li.cil.manual.client.ClientSetup;
import net.fabricmc.api.ClientModInitializer;

public final class MarkdownManualFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientSetup.initialize();
    }
}
