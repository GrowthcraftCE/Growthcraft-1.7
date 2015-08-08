package growthcraft.bamboo.item;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBambooSlab extends ItemSlab
{
	public ItemBambooSlab(Block block)
	{
		super(block, GrowthCraftBamboo.bambooSingleSlab, GrowthCraftBamboo.bambooDoubleSlab, block == GrowthCraftBamboo.bambooDoubleSlab);
		this.setUnlocalizedName("grc.bambooSlab");
	}
}
