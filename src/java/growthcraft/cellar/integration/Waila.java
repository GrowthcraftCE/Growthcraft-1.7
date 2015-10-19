package growthcraft.cellar.integration;

import growthcraft.cellar.common.block.BlockFruitPresser;
import growthcraft.cellar.common.block.ICellarFluidHandler;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.integration.waila.CellarDataProvider;
import growthcraft.core.integration.WailaIntegrationBase;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila extends WailaIntegrationBase
{
	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider provider = new CellarDataProvider();
		reg.registerBodyProvider(provider, BlockFruitPresser.class);
		reg.registerBodyProvider(provider, ICellarFluidHandler.class);
		reg.registerNBTProvider(provider, ICellarFluidHandler.class);

		final String option = "grc.cellar.waila.option.";
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "DisplayFluidContent", option + "DisplayFluidContent", true);
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "FruitPressExtras", option + "FruitPressExtras", true);
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "BrewKettleExtras", option + "BrewKettleExtras", true);
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "FermentBarrelExtras", option + "FermentBarrelExtras", true);
	}
}
