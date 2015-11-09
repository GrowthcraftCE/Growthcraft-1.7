package growthcraft.nether.common.item;

import growthcraft.nether.GrowthCraftNether;

import net.minecraft.item.Item;

public class ItemNetherGhastPowder extends Item
{
	public ItemNetherGhastPowder()
	{
		super();
		setUnlocalizedName("grcnether.netherGhastPowder");
		setTextureName("grcnether:ghastpowder");
		setCreativeTab(GrowthCraftNether.tab);
	}
}
