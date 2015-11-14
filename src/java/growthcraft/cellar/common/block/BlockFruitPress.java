package growthcraft.cellar.common.block;

import java.util.Random;

import growthcraft.cellar.client.render.RenderFruitPress;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.BlockFlags;
import growthcraft.core.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFruitPress extends BlockCellarContainer implements ICellarFluidHandler
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockFruitPress()
	{
		super(Material.wood);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.fruitPress");
		this.setCreativeTab(GrowthCraftCellar.tab);
	}

	private Block getPresserBlock()
	{
		return GrowthCraftCellar.fruitPresser.getBlock();
	}

	public boolean isRotatable(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	public void doRotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) ^ 1, BlockFlags.SEND_TO_CLIENT);
		world.setBlockMetadataWithNotify(x, y + 1, z, world.getBlockMetadata(x, y + 1, z) ^ 1, BlockFlags.SEND_TO_CLIENT);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
		world.setBlock(x, y + 1, z, getPresserBlock(), world.getBlockMetadata(x, y, z), 2);
	}

	private void setDefaultDirection(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			final Block block = world.getBlock(x, y, z - 1);
			final Block block1 = world.getBlock(x, y, z + 1);
			final Block block2 = world.getBlock(x - 1, y, z);
			final Block block3 = world.getBlock(x + 1, y, z);
			byte meta = 3;

			if (block.func_149730_j() && !block1.func_149730_j())
			{
				meta = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j())
			{
				meta = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j())
			{
				meta = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j())
			{
				meta = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_CLIENT);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		final int a = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (a == 0 || a == 2)
		{
			world.setBlockMetadataWithNotify(x, y, z, 0, BlockFlags.SEND_TO_CLIENT);
		}
		else if (a == 1 || a == 3)
		{
			world.setBlockMetadataWithNotify(x, y, z, 1, BlockFlags.SEND_TO_CLIENT);
		}

		world.setBlock(x, y + 1, z, getPresserBlock(), world.getBlockMetadata(x, y, z), BlockFlags.SEND_TO_CLIENT);

		if (stack.hasDisplayName())
		{
			final TileEntityFruitPress te = getTileEntity(world, x, y, z);
			te.setGuiDisplayName(stack.getDisplayName());
		}
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int m, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode && (m & 8) != 0 && presserIsAbove(world, x, y, z))
		{
			world.func_147480_a(x, y + 1, z, true);
			world.getTileEntity(x, y + 1, z).invalidate();
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			world.func_147480_a(x, y, z, true);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		final int meta = world.getBlockMetadata(x, y, z);

		if (meta == 0)
		{
			return side == ForgeDirection.EAST || side == ForgeDirection.WEST;
		}
		else if (meta == 1)
		{
			return side == ForgeDirection.NORTH || side == ForgeDirection.SOUTH;
		}

		return isNormalCube(world, x, y, z);
	}

	/************
	 * STUFF
	 ************/

	/**
	 * @param world - world block is in
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true if the BlockFruitPresser is above this block, false otherwise
	 */
	public boolean presserIsAbove(World world, int x, int y, int z)
	{
		return getPresserBlock() == world.getBlock(x, y + 1, z);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return presserIsAbove(world, x, y, z);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		if (y >= 255) return false;

		return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) &&
			super.canPlaceBlockAt(world, x, y, z) &&
			super.canPlaceBlockAt(world, x, y + 1, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftCellar.fruitPress.getItem();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityFruitPress();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random random, int par3)
	{
		return GrowthCraftCellar.fruitPress.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[6];

		icons[0] = reg.registerIcon("grccellar:fruit_press_wood_bottom");
		icons[1] = reg.registerIcon("grccellar:fruit_press_wood_top");
		icons[2] = reg.registerIcon("grccellar:fruit_press_wood_side");
		icons[3] = reg.registerIcon("grccellar:fruit_press_metal_bottom");
		icons[4] = reg.registerIcon("grccellar:fruit_press_metal_top");
		icons[5] = reg.registerIcon("grccellar:fruit_press_metal_side");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconByIndex(int index)
	{
		return icons[index];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 0)
		{
			return icons[0];
		}
		else if (side == 1)
		{
			return icons[1];
		}
		return icons[2];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderFruitPress.RENDER_ID;
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

	/************
	 * COMPARATOR
	 ************/
	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int par5)
	{
		final TileEntityFruitPress te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			return te.getFluidAmountScaled(15, 0);
		}
		return 0;
	}
}
