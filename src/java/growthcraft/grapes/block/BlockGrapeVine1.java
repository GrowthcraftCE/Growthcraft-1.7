package growthcraft.grapes.block;

import java.util.List;

import growthcraft.core.utils.BlockCheck;
import growthcraft.core.utils.BlockFlags;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.renderer.RenderGrapeVine1;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGrapeVine1 extends BlockGrapeVineBase
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;
	public boolean graphicFlag;

	public BlockGrapeVine1()
	{
		super();
		this.setGrowthRateMultiplier(GrowthCraftGrapes.getConfig().grapeVineTrunkGrowthRate);
		this.setTickRandomly(true);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.grapeVine1");
		this.setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	protected boolean canUpdateGrowth(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) == 0 || world.isAirBlock(x, y + 1, z);
	}

	@Override
	protected void doGrowth(World world, int x, int y, int z, int meta)
	{
		final Block above = world.getBlock(x, y + 1, z);
		/* Is there a rope block above this? */
		if (BlockCheck.isRope(above))
		{
			incrementGrowth(world, x, y, z, meta);
			world.setBlock(x, y + 1, z, GrowthCraftGrapes.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
		}
		else if (world.isAirBlock(x, y + 1, z))
		{
			incrementGrowth(world, x, y, z, meta);
			world.setBlock(x, y + 1, z, this, 0, BlockFlags.UPDATE_CLIENT);
		}
		else if (GrowthCraftGrapes.grapeLeaves.getBlock() == above)
		{
			incrementGrowth(world, x, y, z, meta);
		}
	}

	@Override
	protected float getGrowthRate(World world, int x, int y, int z)
	{
		int j = y;
		if (world.getBlock(x, j - 1, z) == this && world.getBlock(x, j - 2, z) == Blocks.farmland)
		{
			j = y - 1;
		}
		return super.getGrowthRate(world, x, j, z);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return BlockCheck.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this) ||
			this == world.getBlock(x, y - 1, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapeSeeds.getItem();
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[3];

		tex[0] = reg.registerIcon("grcgrapes:trunk");
		tex[1] = reg.registerIcon("grcgrapes:leaves");
		tex[2] = reg.registerIcon("grcgrapes:leaves_opaque");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[0];
	}

	public IIcon getLeafTexture()
	{
		this.graphicFlag = ((BlockLeaves)Blocks.leaves).isOpaqueCube();
		return !this.graphicFlag ? this.tex[1] : this.tex[2];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderGrapeVine1.id;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final float f = 0.0625F;

		if (meta == 0)
		{
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 0.5F, 10*f);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
			this.setBlockBounds(4*f, 0.5F, 4*f, 12*f, 1.0F, 12*f);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}
		else if (meta == 1)
		{
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}

		this.setBlockBoundsBasedOnState(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final float f = 0.0625F;

		if (meta == 0)
		{
			this.setBlockBounds(4*f, 0.0F, 4*f, 12*f, 1.0F, 12*f);
		}
		else
		{
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
		}
	}
}
