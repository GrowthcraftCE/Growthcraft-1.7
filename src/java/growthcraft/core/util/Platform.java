package growthcraft.core.util;

import cpw.mods.fml.common.FMLCommonHandler;

public class Platform
{
	private Platform() {}

	public static boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
}
