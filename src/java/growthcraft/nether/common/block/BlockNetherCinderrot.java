package growthcraft.nether.common.block;

import java.util.Random;

import growthcraft.core.utils.BlockCheck;
import growthcraft.core.utils.BlockFlags;
import growthcraft.nether.GrowthCraftNether;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockNetherCinderrot extends BlockBush implements IPlantable
{
	private final float cinderrotSpreadRate = GrowthCraftNether.getConfig().cinderrotSpreadRate;

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
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		final Block soil = world.getBlock(x, y - 1, z);
		return BlockCheck.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	public void growSelf(World world, int x, int y, int z)
	{
		if (world.isAirBlock(x, y, z) && canBlockStay(world, x, y, z))
		{
			world.setBlock(x, y, z, this, 0, BlockFlags.UPDATE_CLIENT);
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, BlockFlags.UPDATE_CLIENT);
		}
		else if (random.nextFloat() <= cinderrotSpreadRate)
		{
			int xo = random.nextInt(2);
			int zo = random.nextInt(2);

			if (random.nextInt(2) == 0) xo = -xo;
			if (random.nextInt(2) == 0) zo = -zo;

			if (xo != 0 || zo != 0)
			{
				growSelf(world, x + xo, y, z + zo);
			}
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote) return;
		entity.setFire(15);
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
