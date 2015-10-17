package growthcraft.nether.common.block;

import java.util.Random;

import growthcraft.core.utils.BlockFlags;
import growthcraft.core.utils.RenderType;
import growthcraft.nether.GrowthCraftNether;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockNetherCinderrot extends BlockBush
{
	public BlockNetherCinderrot()
	{
		super();
		setTickRandomly(true);
		setBlockName("grcnether.netherCinderrot");
		setCreativeTab(GrowthCraftNether.tab);
		setBlockTextureName("grcnether:cinderrot");
	}

	protected boolean func_149854_a(Block block)
	{
		return Blocks.netherrack == block;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, BlockFlags.UPDATE_CLIENT);
		}
		else if (random.nextInt(25) == 0)
		{
			int xo = random.nextInt(2);
			int zo = random.nextInt(2);

			if (random.nextInt(2) == 0) xo = -xo;
			if (random.nextInt(2) == 0) zo = -zo;

			if (xo != 0 || zo != 0)
			{
				if (world.isAirBlock(x + xo, y, z + zo))
				{
					world.setBlock(x + xo, y, z + zo, this, 0, BlockFlags.UPDATE_CLIENT);
				}
			}
		}
	}

	@Override
	public int getRenderType()
	{
		return RenderType.CROPS;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
