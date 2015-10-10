package growthcraft.core;

import java.io.File;

import growthcraft.core.ConfigBase;
import growthcraft.core.GrowthCraftCore;

import org.apache.logging.log4j.Level;
import cpw.mods.fml.common.FMLLog;

import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	// In case you don't have a wrench you can enable the amazing stick.
	public boolean useAmazingStick = false;

	@Override
	protected void loadConfig()
	{
		this.useAmazingStick = config.get(Configuration.CATEGORY_GENERAL, "Use Amazing Stick", useAmazingStick, "so, I heard you didn't have a wrench, Default : " + useAmazingStick).getBoolean();
	}
}
