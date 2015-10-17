package growthcraft.nether.common.block;

import growthcraft.nether.GrowthCraftNether;
import growthcraft.core.block.Materials;

import net.minecraft.block.Block;

public class BlockNetherMalicePlanks extends Block
{
	public BlockNetherMalicePlanks()
	{
		super(Materials.fireproofWood);
		setHardness(2.0F);
		setBlockName("grcnether.netherMalicePlanks");
		setBlockTextureName("grcnether:planks_malice");
		setHarvestLevel("axe", 0);
		setCreativeTab(GrowthCraftNether.tab);
	}
}
