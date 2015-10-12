package growthcraft.core;

import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	// In case you don't have a wrench you can enable the amazing stick.
	public boolean useAmazingStick;

	@Override
	protected void loadConfig()
	{
		this.useAmazingStick = config.get(Configuration.CATEGORY_GENERAL, "Use Amazing Stick", useAmazingStick, "so, I heard you didn't have a wrench, Default : " + useAmazingStick).getBoolean();
	}
}
