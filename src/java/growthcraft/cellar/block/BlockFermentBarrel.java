package growthcraft.cellar.block;

import java.util.Random;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.renderer.RenderFermentBarrel;
import growthcraft.cellar.tileentity.TileEntityFermentBarrel;
import growthcraft.core.Utils;
import growthcraft.core.utils.BlockFlags;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockFermentBarrel extends BlockCellarContainer implements ICellarFluidHandler
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockFermentBarrel()
	{
		super(Material.wood);
		this.isBlockContainer = true;
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.fermentBarrel");
		this.setCreativeTab(GrowthCraftCellar.tab);
	}

	public boolean isRotatable(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (world.isRemote) return true;
		if (tryWrenchItem(player, world, x, y, z)) return true;

		final TileEntityFermentBarrel te = (TileEntityFermentBarrel)world.getTileEntity(x, y, z);

		if (te != null)
		{
			final ItemStack itemstack = player.inventory.getCurrentItem();
			if (!Utils.fillTank(world, x, y, z, te, itemstack, player))
			{
				if (!drainTank(world, x, y, z, te, itemstack, player))
				{
					openGui(player, world, x, y, z);
				}
			}
			return true;
		}
		return false;
	}

	private boolean drainTank(World world, int x, int y, int z, IFluidHandler tank, ItemStack held, EntityPlayer player)
	{
		if (held != null)
		{
			final FluidStack available = tank.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false);
			FluidStack heldContents = FluidContainerRegistry.getFluidForFilledItem(held);

			if (available != null)
			{
				final ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, held);
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
			if (CellarRegistry.instance().booze().isFluidBooze(fluid))
			{
				final int meta = CellarRegistry.instance().booze().getBoozeIndex(fluid);
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
		final int meta = BlockPistonBase.determineOrientation(world, x, y, z, entity);
		world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_CLIENT);

		if (stack.hasDisplayName())
		{
			((TileEntityFermentBarrel)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftCellar.fermentBarrel.getItem();
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
		return GrowthCraftCellar.fermentBarrel.getItem();
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
		this.icons = new IIcon[4];

		icons[0] = reg.registerIcon("grccellar:fermentbarrel_0");
		icons[1] = reg.registerIcon("grccellar:fermentbarrel_1");
		icons[2] = reg.registerIcon("grccellar:fermentbarrel_2");
		icons[3] = reg.registerIcon("grccellar:fermentbarrel_3");
	}

	public IIcon getIconByIndex(int index)
	{
		return icons[index];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta == 0 || meta == 1)
		{
			return side == 0 || side == 1 ? icons[1] : icons[0];
		}
		else if (meta == 2 || meta == 3)
		{
			return side == 2 || side == 3 ? icons[1] : icons[0];
		}
		else if (meta == 4 || meta == 5)
		{
			return side == 4 || side == 5 ? icons[1] : icons[0];
		}
		return icons[0];
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
		final TileEntityFermentBarrel te = (TileEntityFermentBarrel) world.getTileEntity(x, y, z);
		return te.getFermentProgressScaled(15);
	}
}
