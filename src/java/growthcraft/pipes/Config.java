package growthcraft.pipes;

import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public boolean enabled = true;

	protected void loadConfig()
	{
		this.enabled = config.get(Configuration.CATEGORY_GENERAL, "enabled", enabled, "Should this module be enabled? Default : " + enabled).getBoolean();
	}
}
