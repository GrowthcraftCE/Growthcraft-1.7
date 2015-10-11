package growthcraft.rice.integration;

import growthcraft.core.integration.WailaIntegrationBase;
import growthcraft.rice.block.IPaddy;
import growthcraft.rice.integration.waila.PaddyDataProvider;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila extends WailaIntegrationBase
{
	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider provider = new PaddyDataProvider();
		reg.registerBodyProvider(provider, IPaddy.class);
	}
}
