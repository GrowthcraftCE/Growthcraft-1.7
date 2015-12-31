package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;

import net.minecraft.item.Item;

public class ItemHoneyCombFilled extends Item
{
	public ItemHoneyCombFilled()
	{
		super();
		setTextureName("grcbees:honeycomb_1");
		setUnlocalizedName("grc.honeyComb.full");
		setCreativeTab(GrowthCraftBees.tab);
		setContainerItem(GrowthCraftBees.honeyCombEmpty.getItem());
	}
}
