package growthcraft.core.integration;

import codechicken.nei.api.API;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

import net.minecraft.item.ItemStack;

public class NEI
{
	static final String MOD_ID = "NotEnoughItems";

	private NEI() {}

	/**
	 * @return true if NEI is available, false otherwise
	 */
	public static boolean neiIsAvailable()
	{
		return Loader.isModLoaded(MOD_ID);
	}

	@Optional.Method(modid=MOD_ID)
	private static void hideItem_API(ItemStack itemStack)
	{
		API.hideItem(itemStack);
	}

	public static void hideItem(ItemStack itemStack)
	{
		if (neiIsAvailable()) hideItem_API(itemStack);
	}
}
