package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.BlockStairs;

public class BlockBambooStairs extends BlockStairs
{
	public BlockBambooStairs()
	{
		super(GrowthCraftBamboo.blocks.bambooBlock.getBlock(), 0);
		this.useNeighborBrightness = true;
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setBlockName("grc.bambooStairs");
	}
}
