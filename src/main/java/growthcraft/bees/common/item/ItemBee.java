package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.item.GrcItemBase;

public class ItemBee extends GrcItemBase
{
	public ItemBee()
	{
		super();
		setUnlocalizedName("grc.bee");
		setCreativeTab(GrowthCraftBees.tab);
		setTextureName("grcbees:bee");
	}
}
