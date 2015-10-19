package growthcraft.core;

import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	// In case you don't have a wrench you can enable the amazing stick.
	public boolean useAmazingStick;
	public boolean enableThaumcraftIntegration = true;

	@Override
	protected void loadConfig()
	{
		this.useAmazingStick = config.get(Configuration.CATEGORY_GENERAL, "Use Amazing Stick", useAmazingStick, "so, I heard you didn't have a wrench, Default : " + useAmazingStick).getBoolean();
		this.enableThaumcraftIntegration = config.get(Configuration.CATEGORY_GENERAL, "Enable Thaumcraft Integration", enableThaumcraftIntegration, "Should we integrate with Thaumcraft (if available); Default: " + enableThaumcraftIntegration).getBoolean();
	}
}
