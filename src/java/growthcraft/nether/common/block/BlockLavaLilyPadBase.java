package growthcraft.nether.common.block;

import java.util.List;

import growthcraft.core.util.RenderType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockLavaLilyPadBase extends BlockBush
{
	public BlockLavaLilyPadBase()
	{
		super();
		setHardness(0.0F);
		setStepSound(Block.soundTypeGrass);
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

	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity entity)
	{
		if (entity == null || !(entity instanceof EntityBoat))
		{
			super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return AxisAlignedBB.getBoundingBox((double)x + getBlockBoundsMinX(), (double)y + getBlockBoundsMinY(), (double)z + getBlockBoundsMinZ(),
			(double)x + getBlockBoundsMaxX(), (double)y + getBlockBoundsMaxY(), (double)z + getBlockBoundsMaxZ());
	}

	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return y >= 0 && y < 256 ? world.getBlock(x, y - 1, z).getMaterial() == Material.lava && world.getBlockMetadata(x, y - 1, z) == 0 : false;
	}
}
