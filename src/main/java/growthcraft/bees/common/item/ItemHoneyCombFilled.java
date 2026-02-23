package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.item.GrcItemBase;

public class ItemHoneyCombFilled extends GrcItemBase
{
	public ItemHoneyCombFilled()
	{
		super();
		setTextureName("grcbees:honeycomb_1");
		setUnlocalizedName("grc.honeyComb.full");
		setCreativeTab(GrowthCraftBees.tab);
		setContainerItem(GrowthCraftBees.items.honeyCombEmpty.getItem());
	}
}
