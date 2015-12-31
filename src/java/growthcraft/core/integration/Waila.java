package growthcraft.core.integration;

import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.common.block.IPaddy;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.waila.CropDataProvider;
import growthcraft.core.integration.waila.PaddyDataProvider;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

import net.minecraft.item.Item;

public class Waila extends WailaIntegrationBase
{
	public Waila()
	{
		super(GrowthCraftCore.MOD_ID);
	}

	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider cropProvider = new CropDataProvider();
		reg.registerBodyProvider(cropProvider, ICropDataProvider.class);
		reg.registerBodyProvider(cropProvider, Item.class);

		final IWailaDataProvider paddyProvider = new PaddyDataProvider();
		reg.registerBodyProvider(paddyProvider, IPaddy.class);
	}
}
