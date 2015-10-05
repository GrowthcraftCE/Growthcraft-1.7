package growthcraft.cellar.integration;

import growthcraft.cellar.block.ICellarFluidHandler;
import growthcraft.cellar.integration.waila.CellarDataProvider;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila
{
	public Waila()
	{
		FMLInterModComms.sendMessage("Waila", "register", getClass().getName() + ".register");
	}

	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider provider = new CellarDataProvider();
		reg.registerBodyProvider(provider, ICellarFluidHandler.class);
		reg.registerNBTProvider(provider, ICellarFluidHandler.class);
	}
}
