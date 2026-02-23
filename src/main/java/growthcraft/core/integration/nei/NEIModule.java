package growthcraft.core.integration.nei;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEIModuleBase;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NEIModule extends NEIModuleBase
{
	public NEIModule()
	{
		super(GrowthCraftCore.MOD_ID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid=NEIPlatform.MOD_ID)
	public void integrateClient()
	{
		API.registerRecipeHandler(new RecipeHandlerShapelessMulti());
		API.registerUsageHandler(new RecipeHandlerShapelessMulti());
		
		API.registerRecipeHandler(new RecipeHandlerShapedMulti());
		API.registerUsageHandler(new RecipeHandlerShapedMulti());
	}
}
