package growthcraft.cellar.block;

import java.util.List;
import java.util.Random;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.renderer.RenderBrewKettle;
import growthcraft.cellar.tileentity.TileEntityBrewKettle;
import growthcraft.core.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrewKettle extends BlockCellarContainer implements ICellarFluidHandler
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	private final boolean dropItemsInBrewKettle = GrowthCraftCellar.getConfig().dropItemsInBrewKettle;

	public BlockBrewKettle()
	{
		super(Material.iron);
		this.isBlockContainer = true;
		this.setHardness(2.0F);
		this.setBlockName("grc.brewKettle");
		this.setCreativeTab(GrowthCraftCellar.tab);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (dropItemsInBrewKettle)
		{
			if (!world.isRemote)
			{
				final TileEntityBrewKettle te = (TileEntityBrewKettle)world.getTileEntity(x, y, z);
				if (te != null)
				{
					if (entity instanceof EntityItem)
					{
						te.tryMergeItemIntoMainSlot(((EntityItem)entity).getEntityItem());
					}
				}
			}
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (world.isRemote) return true;
		if (tryWrenchItem(player, world, x, y, z)) return true;

		final TileEntityBrewKettle te = (TileEntityBrewKettle)world.getTileEntity(x, y, z);
		final ItemStack is = player.inventory.getCurrentItem();
		if (te != null)
		{
			if (!Utils.fillTank(world, x, y, z, te, is, player))
			{
				if (!Utils.drainTank(world, x, y, z, te, is, player, false, 64, 0.35F))
				{
					openGui(player, world, x, y, z);
				}
				else
				{
					//=====
					world.markBlockForUpdate(x, y, z);
				}
			}
			else
			{
				//=====
				world.markBlockForUpdate(x, y, z);
			}
			return true;
		}

		return false;
	}

	private void openGui(EntityPlayer player, World world, int x, int y, int z)
	{
		player.openGui(GrowthCraftCellar.instance, 0, world, x, y, z);
	}

	private void spawnExp(int amount, float exp, EntityPlayer player)
	{
		int j;

		if (exp == 0.0F)
		{
			amount = 0;
		}
		else if (exp < 1.0F)
		{
			j = MathHelper.floor_float((float)amount * exp);

			if (j < MathHelper.ceiling_float_int((float)amount * exp) && (float)Math.random() < (float)amount * exp - (float)j)
			{
				++j;
			}

			amount = j;
		}

		while (amount > 0)
		{
			j = EntityXPOrb.getXPSplit(amount);
			amount -= j;
			player.worldObj.spawnEntityInWorld(new EntityXPOrb(player.worldObj, player.posX, player.posY + 0.5D, player.posZ + 0.5D, j));
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			((TileEntityBrewKettle)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftCellar.brewKettle.getItem();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityBrewKettle();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random random, int par3)
	{
		return GrowthCraftCellar.brewKettle.getItem();
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

		icons[0] = reg.registerIcon("grccellar:brewkettle_0");
		icons[1] = reg.registerIcon("grccellar:brewkettle_1");
		icons[2] = reg.registerIcon("grccellar:brewkettle_2");
		icons[3] = reg.registerIcon("grccellar:brewkettle_3");
	}

	public IIcon getIconByIndex(int index)
	{
		return icons[index];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			return icons[3];
		}
		else if (side == 0)
		{
			return icons[0];
		}
		return icons[2];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderBrewKettle.id;
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
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		final float f = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBoundsForItemRender();
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
		final TileEntityBrewKettle te = (TileEntityBrewKettle) world.getTileEntity(x, y, z);
		return te.getFluidAmountScaled(15, 1);
	}
}
