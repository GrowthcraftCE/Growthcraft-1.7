package growthcraft.core.integration.witchery;

import cpw.mods.fml.common.Loader;

public class WitcheryPlatform {

    public static final String MOD_ID = "Witchery";

    private WitcheryPlatform() {
    }

    public static boolean isLoaded() {
        return Loader.isModLoaded(MOD_ID);
    }
}
