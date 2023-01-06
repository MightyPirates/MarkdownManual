package li.cil.manual.api.platform;

/**
 * Fabric specific interface, used to define an entrypoint that is invoked when all
 * manual related registries have been created, and entries may be registered with
 * them. This is Fabric's approach to allowing ordered initialization. For Forge this
 * is not required, since it allows declaring mod initialization ordering.
 */
public interface FabricManualInitializer {
    /**
     * Registers objects with manual related registries.
     */
    void registerManualObjects();
}
