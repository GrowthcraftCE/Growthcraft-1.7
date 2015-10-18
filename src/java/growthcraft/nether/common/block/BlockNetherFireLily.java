package growthcraft.nether.common.block;

import growthcraft.core.utils.RenderType;
import growthcraft.nether.GrowthCraftNether;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.init.Blocks;

public class BlockNetherFireLily extends BlockBush
{
	public BlockNetherFireLily()
	{
		super();
		setBlockName("grcnether.netherFireLily");
		setCreativeTab(GrowthCraftNether.tab);
		setBlockTextureName("grcnether:firelily");
		final float var1 = 0.5F;
		final float var2 = 0.015625F;
		setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
	}

	@Override
	public int getRenderType()
	{
		return RenderType.LILYPAD;
	}

	protected boolean func_149854_a(Block block)
	{
		return Blocks.lava == block;
	}
}
