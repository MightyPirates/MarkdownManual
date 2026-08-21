/* SPDX-License-Identifier: MIT */

package li.cil.manual.client.neoforge;

import li.cil.manual.api.util.Constants;
import li.cil.manual.client.ClientSetup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class MarkdownManualNeoForge {
    public MarkdownManualNeoForge() {
        ClientSetup.initialize();
    }
}
