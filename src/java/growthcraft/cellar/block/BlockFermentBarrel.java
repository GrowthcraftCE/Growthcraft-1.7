package growthcraft.cellar.block;

import java.util.Random;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.renderer.RenderFermentBarrel;
import growthcraft.cellar.tileentity.TileEntityFermentBarrel;
import growthcraft.core.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockFermentBarrel extends BlockContainer implements ICellarFluidHandler
{
	private final Random rand = new Random();
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockFermentBarrel()
	{
		super(Material.wood);
		this.isBlockContainer = true;
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.fermentBarrel");
		this.setCreativeTab(GrowthCraftCellar.tab);
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityFermentBarrel te = (TileEntityFermentBarrel)world.getTileEntity(x, y, z);

			if (te != null)
			{
				ItemStack itemstack = player.inventory.getCurrentItem();
				if (!Utils.fillTank(world, x, y, z, te, itemstack, player))
				{
					if (!drainTank(world, x, y, z, te, itemstack, player))
					{
						openGui(player, world, x, y, z);
					}
				}
			}

			return true;
		}
	}

	private boolean drainTank(World world, int x, int y, int z, IFluidHandler tank, ItemStack held, EntityPlayer player)
	{
		if (held != null)
		{
			FluidStack heldContents = FluidContainerRegistry.getFluidForFilledItem(held);
			FluidStack available = tank.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false);

			if (available != null)
			{
				ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, held);
				heldContents = FluidContainerRegistry.getFluidForFilledItem(filled);

				if (heldContents != null)
				{
					if (!player.inventory.addItemStackToInventory(filled))
					{
						world.spawnEntityInWorld(new EntityItem(world, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, filled));
					}
					else if (player instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
					}

					if (!player.capabilities.isCreativeMode)
					{
						if (--held.stackSize <= 0)
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
						}
					}

					tank.drain(ForgeDirection.UNKNOWN, heldContents.amount, true);
					setAchievements(player, available.getFluid());

					return true;
				}
			}
		}

		return false;
	}

	private void setAchievements(EntityPlayer player, Fluid fluid)
	{
		if (fluid != null)
		{
			if (CellarRegistry.instance().isFluidBooze(fluid))
			{
				int meta = CellarRegistry.instance().getBoozeIndex(fluid);
				if (meta > 0 && meta < 4)
				{
					player.triggerAchievement(GrowthCraftCellar.fermentBooze);
				}
			}
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

			world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		int meta = BlockPistonBase.determineOrientation(world, x, y, z, entity);
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);

		if (stack.hasDisplayName())
		{
			((TileEntityFermentBarrel)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		TileEntityFermentBarrel te = (TileEntityFermentBarrel)world.getTileEntity(x, y, z);

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
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(GrowthCraftCellar.fermentBarrel);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityFermentBarrel();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return Item.getItemFromBlock(GrowthCraftCellar.fermentBarrel);
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
		tex = new IIcon[4];

		tex[0] = reg.registerIcon("grccellar:fermentbarrel_0");
		tex[1] = reg.registerIcon("grccellar:fermentbarrel_1");
		tex[2] = reg.registerIcon("grccellar:fermentbarrel_2");
		tex[3] = reg.registerIcon("grccellar:fermentbarrel_3");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta == 0 || meta == 1)
		{
			return side == 0 || side == 1 ? this.tex[1] : this.tex[0];
		}
		else if (meta == 2 || meta == 3)
		{
			return side == 2 || side == 3 ? this.tex[1] : this.tex[0];
		}
		else if (meta == 4 || meta == 5)
		{
			return side == 4 || side == 5 ? this.tex[1] : this.tex[0];
		}
		return this.tex[0];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderFermentBarrel.id;
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
		TileEntityFermentBarrel te = (TileEntityFermentBarrel) world.getTileEntity(x, y, z);
		return te.getFermentProgressScaled(15);
	}
}
