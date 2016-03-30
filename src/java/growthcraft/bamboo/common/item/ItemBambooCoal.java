package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.item.Item;

public class ItemBambooCoal extends Item
{
	public ItemBambooCoal()
	{
		super();
		setUnlocalizedName("grc.bambooCoal");
		setTextureName("grcbamboo:coal");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}
}
