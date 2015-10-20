package growthcraft.pipes.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.api.core.GrcColour;
import growthcraft.cellar.common.block.ICellarFluidHandler;
import growthcraft.core.common.block.IWrenchable;
import growthcraft.core.common.block.IDroppableBlock;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.ItemUtils;
import growthcraft.pipes.client.renderer.RenderPipe;
import growthcraft.pipes.common.tileentity.TileEntityPipeBase;
import growthcraft.pipes.util.PipeType;
import growthcraft.pipes.util.PipeConsts;

import buildcraft.api.blocks.IColorRemovable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPipeBase extends Block implements IPipeBlock, ITileEntityProvider, ICellarFluidHandler, IDroppableBlock, IWrenchable, IColorRemovable
{
	private PipeType pipeType;

	public BlockPipeBase(PipeType type)
	{
		super(Material.glass);
		setHardness(0.1F);
		setStepSound(soundTypeGlass);
		setBlockName("grc.pipeBase");
		setCreativeTab(GrowthCraftCore.tab);
		this.pipeType = type;
	}

	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(block, 1, GrcColour.Transparent.ordinal()));
	}

	/**
	 * Drops the block as an item and replaces it with air
	 *
	 * @param world - world to drop in
	 * @param x - x Coord
	 * @param y - y Coord
	 * @param z - z Coord
	 */
	public void fellBlockAsItem(World world, int x, int y, int z)
	{
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		world.setBlockToAir(x, y, z);
	}

	public PipeType getPipeType()
	{
		return pipeType;
	}

	public TileEntityPipeBase getTileEntity(IBlockAccess world, int x, int y, int z)
	{
		return (TileEntityPipeBase)world.getTileEntity(x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor)
	{
		final TileEntityPipeBase pipeBase = getTileEntity(world, x, y, z);
		if (pipeBase != null) pipeBase.onNeighbourChanged();
	}

	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityPipeBase();
	}

	@Override
	public int getRenderType()
	{
		return RenderPipe.RENDER_ID;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	public boolean setBlockColour(World world, int x, int y, int z, ForgeDirection side, GrcColour colour)
	{
		final TileEntityPipeBase te = getTileEntity(world, x, y, z);

		if (te != null)
		{
			te.setColour(colour);
			return true;
		}
		return false;
	}

	/* IColorRemovable */
	@Override
	public boolean removeColorFromBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		return setBlockColour(world, x, y, z, side, GrcColour.Transparent);
	}

	@Override
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour)
	{
		return setBlockColour(world, x, y, z, side, GrcColour.VALID_COLORS.get(colour));
	}

	public boolean wrenchBlock(World world, int x, int y, int z, EntityPlayer player, ItemStack wrench)
	{
		if (player != null)
		{
			final ItemStack is = player.inventory.getCurrentItem();
			if (ItemUtils.canWrench(is, player, x, y, z))
			{
				/*
				 * This branch is for removing the pipe using a wrench, while the
				 * player is sneaking.
				 */
				if (player.isSneaking())
				{
					if (!world.isRemote)
					{
						fellBlockAsItem(world, x, y, z);
						ItemUtils.wrenchUsed(is, player, x, y, z);
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		final TileEntityPipeBase te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			return te.getColour().ordinal();
		}
		return GrcColour.Transparent.ordinal();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
	{
		setBlockColour(world, x, y, z, ForgeDirection.UNKNOWN, GrcColour.toColour(itemstack.getItemDamage()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity entity)
	{
		switch (pipeType)
		{
			case BASE:
				setBlockBounds(PipeConsts.BASE_CORE_MIN_X, PipeConsts.BASE_CORE_MIN_Y, PipeConsts.BASE_CORE_MIN_Z,
					PipeConsts.BASE_CORE_MAX_X, PipeConsts.BASE_CORE_MAX_Y, PipeConsts.BASE_CORE_MAX_Z);
				break;
			case VACUUM:
				setBlockBounds(PipeConsts.VACUUM_CORE_MIN_X, PipeConsts.VACUUM_CORE_MIN_Y, PipeConsts.VACUUM_CORE_MIN_Z,
					PipeConsts.VACUUM_CORE_MAX_X, PipeConsts.VACUUM_CORE_MAX_Y, PipeConsts.VACUUM_CORE_MAX_Z);
				break;
			default:
				setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		}

		super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		final TileEntityPipeBase te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			for (int i = 0; i < 6; ++i)
			{
				if (te.pipeSections[i].usageState != TileEntityPipeBase.UsageState.UNUSABLE)
				{
					if (pipeType == PipeType.BASE)
					{
						setBlockBounds(PipeConsts.INNER_SIDES[i][0], PipeConsts.INNER_SIDES[i][1], PipeConsts.INNER_SIDES[i][2],
							PipeConsts.INNER_SIDES[i][3], PipeConsts.INNER_SIDES[i][4], PipeConsts.INNER_SIDES[i][5]);
						super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
					}
					setBlockBounds(PipeConsts.BUS_SIDES[i][0], PipeConsts.BUS_SIDES[i][1], PipeConsts.BUS_SIDES[i][2],
						PipeConsts.BUS_SIDES[i][3], PipeConsts.BUS_SIDES[i][4], PipeConsts.BUS_SIDES[i][5]);
					super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
				}
			}
		}
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
}
