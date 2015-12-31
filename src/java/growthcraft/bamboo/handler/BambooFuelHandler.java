package growthcraft.bamboo.handler;

import growthcraft.bamboo.GrowthCraftBamboo;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BambooFuelHandler implements IFuelHandler
{
	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if (fuel != null)
		{
			final Item item = fuel.getItem();
			if (GrowthCraftBamboo.bambooCoal.equals(item))
			{
				return 800;
			}
			else if (GrowthCraftBamboo.bambooShootFood.equals(item))
			{
				return 100;
			}
			else if (GrowthCraftBamboo.bambooShoot.getItem() == item)
			{
				return 100;
			}
			// Alatyami added per GitHub Issue #55
			else if (GrowthCraftBamboo.bambooSingleSlab.getItem() == item)
			{
				return 150;
			}
		}
		return 0;
	}
}
