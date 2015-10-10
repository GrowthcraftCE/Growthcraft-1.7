package growthcraft.core.integration;

import growthcraft.core.block.ICropDataProvider;
import growthcraft.core.integration.waila.CropDataProvider;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

import net.minecraft.item.Item;

public class Waila extends WailaIntegrationBase
{
	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider provider = new CropDataProvider();
		reg.registerBodyProvider(provider, ICropDataProvider.class);
		reg.registerBodyProvider(provider, Item.class);
	}
}
