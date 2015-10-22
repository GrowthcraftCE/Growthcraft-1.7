package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;

import net.minecraft.item.Item;

public class ItemHoneyCombEmpty extends Item
{
	public ItemHoneyCombEmpty()
	{
		super();
		setTextureName("grcbees:honeycomb_0");
		setUnlocalizedName("grc.honeyComb.empty");
		setCreativeTab(GrowthCraftBees.tab);
	}
}
