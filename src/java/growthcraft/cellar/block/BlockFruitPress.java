package growthcraft.cellar.block;

import java.util.Random;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.renderer.RenderFruitPress;
import growthcraft.cellar.tileentity.TileEntityFruitPress;
import growthcraft.core.Utils;
import growthcraft.core.utils.BlockFlags;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFruitPress extends BlockCellarContainer implements ICellarFluidHandler
{
	private final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockFruitPress()
	{
		super(Material.wood);
		this.isBlockContainer = true;
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.fruitPress");
		this.setCreativeTab(GrowthCraftCellar.tab);
	}

	private Block getPresserBlock()
	{
		return GrowthCraftCellar.fruitPresser.getBlock();
	}

	public boolean isRotatable()
	{
		return true;
	}

	public void doRotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) ^ 1, BlockFlags.SEND_TO_CLIENT);
		world.setBlockMetadataWithNotify(x, y + 1, z, world.getBlockMetadata(x, y + 1, z) ^ 1, BlockFlags.SEND_TO_CLIENT);
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (useWrenchItem(player, world, x, y, z))
		{
			return true;
		}

		if (world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityFruitPress te = (TileEntityFruitPress)world.getTileEntity(x, y, z);

			if (te != null)
			{
				ItemStack itemstack = player.inventory.getCurrentItem();
				if (!Utils.drainTank(world, x, y, z, te, itemstack, player, false, 64, 0.35F))
				{
					openGui(player, world, x, y, z);
				}
			}
			return true;
		}
	}

	private void openGui(EntityPlayer player, World world, int x, int y, int z)
	{
		player.openGui(GrowthCraftCellar.instance, 0, world, x, y, z);
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
			Block block = world.getBlock(x, y, z - 1);
			Block block1 = world.getBlock(x, y, z + 1);
			Block block2 = world.getBlock(x - 1, y, z);
			Block block3 = world.getBlock(x + 1, y, z);
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
		int a = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

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
			((TileEntityFruitPress)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
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

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		TileEntityFruitPress te = (TileEntityFruitPress)world.getTileEntity(x, y, z);

		if (te != null)
		{
			for (int index = 0; index < te.getSizeInventory(); ++index)
			{
				ItemStack stack = te.getStackInSlot(index);

				if (stack != null)
				{
					float f = this.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
					float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

					while (stack.stackSize > 0)
					{
						int k1 = this.rand.nextInt(21) + 10;

						if (k1 > stack.stackSize)
						{
							k1 = stack.stackSize;
						}

						stack.stackSize -= k1;
						EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(stack.getItem(), k1, stack.getItemDamage()));

						if (stack.hasTagCompound())
						{
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
						entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
						world.spawnEntityInWorld(entityitem);
					}
				}
			}

			world.func_147453_f(x, y, z, block);
		}

		super.breakBlock(world, x, y, z, block, par6);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 0)
		{
			return (side == side.EAST || side == side.WEST);
		}
		else if (meta == 1)
		{
			return (side == side.NORTH || side == side.SOUTH);
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
		return y >= 255 ? false : World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
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
	public int quantityDropped(Random rand)
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
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("planks_oak");
		tex[1] = reg.registerIcon("anvil_base");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[0];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderFruitPress.id;
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
		TileEntityFruitPress te = (TileEntityFruitPress) world.getTileEntity(x, y, z);
		return te.getFluidAmountScaled(15);
	}
}
