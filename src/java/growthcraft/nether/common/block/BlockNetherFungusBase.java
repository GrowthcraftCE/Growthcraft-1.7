package growthcraft.nether.common.block;

import java.util.Random;

import growthcraft.core.util.BlockCheck;
import growthcraft.core.util.BlockFlags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockNetherFungusBase extends BlockBush implements IPlantable, IGrowable
{
	public BlockNetherFungusBase()
	{
		super();
		setTickRandomly(true);
	}

	protected boolean func_149854_a(Block block)
	{
		return Blocks.netherrack == block || Blocks.soul_sand == block;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return BlockCheck.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	protected void growFungus(World world, int x, int y, int z)
	{
		if (world.isAirBlock(x, y, z) && canBlockStay(world, x, y, z))
		{
			world.setBlock(x, y, z, this, 0, BlockFlags.UPDATE_CLIENT);
		}
	}

	public boolean canFungusSpread(World world, int x, int y, int z)
	{
		for (BlockCheck.BlockDirection dir : BlockCheck.DIR8)
		{
			if (world.isAirBlock(x + dir.offsetX, y, z + dir.offsetZ) && canBlockStay(world, x + dir.offsetX, y, z + dir.offsetZ))
			{
				return true;
			}
		}
		return false;
	}

	/* Can this accept bonemeal? */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isClient)
	{
		return canFungusSpread(world, x, y, z);
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z)
	{
		final BlockCheck.BlockDirection dir = BlockCheck.randomDirection8(random);
		growFungus(world, x + dir.offsetX, y, z + dir.offsetZ);
	}

	protected abstract float getSpreadRate(World world, int x, int y, int z);

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, BlockFlags.UPDATE_CLIENT);
		}
		else if (random.nextFloat() <= getSpreadRate(world, x, y, z))
		{
			final BlockCheck.BlockDirection dir = BlockCheck.randomDirection8(random);
			growFungus(world, x + dir.offsetX, y, z + dir.offsetZ);
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Nether;
	}

	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return this;
	}

	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}
}
