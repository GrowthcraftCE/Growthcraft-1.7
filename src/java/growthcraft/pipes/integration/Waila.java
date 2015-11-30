package growthcraft.pipes.integration;

import growthcraft.core.integration.WailaIntegrationBase;
import growthcraft.pipes.integration.waila.PipeDataProvider;
import growthcraft.pipes.block.BlockPipeBase;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila extends WailaIntegrationBase
{
	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider provider = new PipeDataProvider();

		reg.registerBodyProvider(provider, BlockPipeBase.class);
		reg.registerNBTProvider(provider, BlockPipeBase.class);
	}
}
