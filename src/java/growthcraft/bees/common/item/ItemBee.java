package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;

import net.minecraft.item.Item;

public class ItemBee extends Item
{
	public ItemBee()
	{
		super();
		setUnlocalizedName("grc.bee");
		setCreativeTab(GrowthCraftBees.tab);
		setTextureName("grcbees:bee");
	}
}
