package li.cil.manual.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod("markdown_manual")
public final class MarkdownManual {
    public MarkdownManual() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::initialize);
    }
}
