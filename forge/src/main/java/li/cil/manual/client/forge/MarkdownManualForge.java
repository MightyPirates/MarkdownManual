package li.cil.manual.client.forge;

import dev.architectury.platform.forge.EventBuses;
import li.cil.manual.api.util.Constants;
import li.cil.manual.client.ClientSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public final class MarkdownManualForge {
    public MarkdownManualForge() {
        EventBuses.registerModEventBus(Constants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::initialize);
    }
}
