package li.cil.manual.client;

import li.cil.manual.api.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class MarkdownManual {
    public MarkdownManual() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::initialize);
    }
}
