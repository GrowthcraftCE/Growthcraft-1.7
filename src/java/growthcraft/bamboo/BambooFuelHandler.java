package growthcraft.bamboo;

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
			Item item = fuel.getItem();
			if (item == GrowthCraftBamboo.bambooCoal)
			{
				return 800;
			}
			else if (item == GrowthCraftBamboo.bambooShootFood)
			{
				return 100;
			}
			else if (item == Item.getItemFromBlock(GrowthCraftBamboo.bambooShoot))
			{
				return 100;
			}
		}
		return 0;
	}
}
