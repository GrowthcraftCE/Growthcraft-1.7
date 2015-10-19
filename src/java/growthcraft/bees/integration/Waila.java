package growthcraft.bees.integration;

import growthcraft.core.integration.WailaIntegrationBase;
import growthcraft.bees.common.block.BlockBeeBox;
import growthcraft.bees.integration.waila.BeesDataProvider;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila extends WailaIntegrationBase
{
	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider provider = new BeesDataProvider();
		reg.registerBodyProvider(provider, BlockBeeBox.class);
		reg.registerNBTProvider(provider, BlockBeeBox.class);
	}
}
