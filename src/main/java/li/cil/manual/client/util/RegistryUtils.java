package li.cil.manual.client.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class RegistryUtils {
    private enum Phase {
        PRE_INIT,
        INIT,
        POST_INIT,
    }

    private static final List<DeferredRegister<?>> ENTRIES = new ArrayList<>();
    private static Phase phase = Phase.PRE_INIT;
    private static String modId;

    public static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(ResourceKey<Registry<T>> key) {
        if (phase != Phase.INIT) throw new IllegalStateException();

        final DeferredRegister<T> entry = DeferredRegister.create(key.registry(), modId);
        ENTRIES.add(entry);
        return entry;
    }

    public static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(final IForgeRegistry<T> registry) {
        if (phase != Phase.INIT) throw new IllegalStateException();

        final DeferredRegister<T> entry = DeferredRegister.create(registry, modId);
        ENTRIES.add(entry);
        return entry;
    }

    public static void begin(final String modId) {
        RegistryUtils.modId = modId;
        if (phase != Phase.PRE_INIT) throw new IllegalStateException();
        phase = Phase.INIT;
    }

    public static void finish() {
        if (phase != Phase.INIT) throw new IllegalStateException();
        phase = Phase.POST_INIT;

        for (final DeferredRegister<?> register : ENTRIES) {
            register.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        ENTRIES.clear();
    }

    private RegistryUtils() {
    }
}
