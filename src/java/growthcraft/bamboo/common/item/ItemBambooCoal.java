package growthcraft.bamboo.common.item;

import growthcraft.core.GrowthCraftCore;

import net.minecraft.item.Item;

public class ItemBambooCoal extends Item
{
	public ItemBambooCoal()
	{
		super();
		setUnlocalizedName("grc.bambooCoal");
		setTextureName("grcbamboo:coal");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}
}
