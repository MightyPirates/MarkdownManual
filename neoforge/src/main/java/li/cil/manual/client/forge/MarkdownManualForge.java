package li.cil.manual.client.forge;

import li.cil.manual.api.util.Constants;
import li.cil.manual.client.ClientSetup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;


@Mod(Constants.MOD_ID)
public final class MarkdownManualForge {
    public MarkdownManualForge() {
        //EventBuses.registerModEventBus(Constants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetup.initialize();
        }
    }
}
