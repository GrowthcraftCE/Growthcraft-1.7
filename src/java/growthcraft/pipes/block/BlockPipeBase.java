package growthcraft.pipes.block;

import growthcraft.api.core.GrcColour;
import growthcraft.cellar.block.ICellarFluidHandler;
import growthcraft.core.block.IWrenchable;
import growthcraft.core.block.IDroppableBlock;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.utils.ItemUtils;
import growthcraft.pipes.client.render.RenderPipe;
import growthcraft.pipes.tileentity.TileEntityPipeBase;
import growthcraft.pipes.utils.PipeType;

import buildcraft.api.blocks.IColorRemovable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.player.EntityPlayer;
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
			world.notifyBlockChange(x, y, z, this);
			return true;
		}
		return false;
	}

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
					fellBlockAsItem(world, x, y, z);
					ItemUtils.wrenchUsed(is, player, x, y, z);
					return true;
				}
			}
		}
		return false;
	}
}
