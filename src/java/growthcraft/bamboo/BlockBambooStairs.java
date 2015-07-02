package growthcraft.bamboo;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.BlockStairs;

public class BlockBambooStairs extends BlockStairs
{
	private boolean boundsFlag = false;
	private int boundsInt = 0;

	public BlockBambooStairs()
	{
		super(GrowthCraftBamboo.bambooBlock, 0);
		this.useNeighborBrightness = true;
		this.setCreativeTab(GrowthCraftCore.tab);
		this.setBlockName("grc.bambooStairs");
	}
}
