package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.BlockStairs;

public class BlockBambooStairs extends BlockStairs
{
	public BlockBambooStairs()
	{
		super(GrowthCraftBamboo.bambooBlock.getBlock(), 0);
		this.useNeighborBrightness = true;
		this.setCreativeTab(GrowthCraftCore.tab);
		this.setBlockName("grc.bambooStairs");
	}
}
