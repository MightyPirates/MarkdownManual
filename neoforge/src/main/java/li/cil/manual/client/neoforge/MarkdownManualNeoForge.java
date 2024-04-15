package li.cil.manual.client.neoforge;

import li.cil.manual.api.util.Constants;
import li.cil.manual.client.ClientSetup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public final class MarkdownManualNeoForge {
    public MarkdownManualNeoForge() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetup.initialize();
        }
    }
}
