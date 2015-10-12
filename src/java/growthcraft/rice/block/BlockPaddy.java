package growthcraft.rice.block;

import java.util.List;
import java.util.Random;

import growthcraft.core.Utils;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.renderer.RenderPaddy;
import growthcraft.rice.ClientProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockPaddy extends Block implements IPaddy
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	private final int paddyFieldMax = GrowthCraftRice.getConfig().paddyFieldMax;

	public BlockPaddy()
	{
		super(Material.ground);
		this.setHardness(0.5F);
		this.setTickRandomly(true);
		this.setStepSound(soundTypeGravel);
		this.setBlockName("grc.paddyField");
		this.setCreativeTab(null);
	}

	public boolean isFilledWithWater(IBlockAccess world, int x, int y, int z, int meta)
	{
		return meta >= paddyFieldMax;
	}


	private boolean isBelowWater(World world, int x, int y, int z)
	{
		return world.getBlock(x, y + 1, z).getMaterial() == Material.water;
	}

	/************
	 * TICK
	 ************/

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (isBelowWater(world, x, y, z) && world.canLightningStrikeAt(x, y + 1, z))
		{
			world.setBlockMetadataWithNotify(x, y, z, paddyFieldMax, 2);
		}
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
			final ItemStack itemstack = player.inventory.getCurrentItem();
			if (itemstack != null)
			{
				if (FluidContainerRegistry.isFilledContainer(itemstack))
				{
					final FluidStack addF = FluidContainerRegistry.getFluidForFilledItem(itemstack);
					if (addF != null)
					{
						int radius = addF.amount * 2 / FluidContainerRegistry.BUCKET_VOLUME;
						if (radius % 2 == 0)
						{
							radius -= 1;
						}

						if (addF.getFluid() == FluidRegistry.WATER && radius > 0)
						{
							for (int lx = x - radius; lx <= x + radius; ++lx)
							{
								for (int lz = z - radius; lz <= z + radius; ++lz)
								{
									if (world.getBlock(lx, y, lz) == this)
									{
										world.setBlockMetadataWithNotify(lx, y, lz, paddyFieldMax, 3);
									}
								}
							}

							if(!player.capabilities.isCreativeMode)
							{
								player.inventory.setInventorySlotContents(player.inventory.currentItem, Utils.consumeItem(itemstack));
							}

							return true;
						}
					}
				}
			}

			return false;
		}
	}

	@Override
	public void onFallenUpon(World world, int x, int y, int z, Entity entity, float par6)
	{
		if (!world.isRemote && world.rand.nextFloat() < par6 - 0.5F)
		{
			if (!(entity instanceof EntityPlayer) && !world.getGameRules().getGameRuleBooleanValue("mobGriefing"))
			{
				return;
			}

			final Block plant = world.getBlock(x, y + 1, z);
			if (plant instanceof IPaddyCrop)
			{
				plant.dropBlockAsItem(world, x, y + 1, z, world.getBlockMetadata(x, y + 1, z), 0);
				world.setBlockToAir(x, y + 1, z);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		super.onNeighborBlockChange(world, x, y, z, par5);
		final Material material = world.getBlock(x, y + 1, z).getMaterial();
		if (Material.water == material)
		{
			world.setBlockMetadataWithNotify(x, y, z, paddyFieldMax, 3);
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(Blocks.dirt);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int i, int j, int k, ForgeDirection side)
	{
		return ForgeDirection.UP == side ? false : true;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return false;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return Item.getItemFromBlock(Blocks.dirt);
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
		tex = new IIcon[3];

		tex[0] = reg.registerIcon("dirt");
		tex[1] = reg.registerIcon("farmland_dry");
		tex[2] = reg.registerIcon("farmland_wet");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side > 1)
		{
			return tex[0];
		}
		else if (side == 0)
		{
			return tex[0];
		}
		else if (meta == 0)
		{
			return tex[1];
		}
		else
		{
			return tex[2];
		}
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderPaddy.id;
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
	public boolean canRenderInPass(int pass)
	{
		ClientProxy.renderPass = pass;
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderBlockPass()
	{
		return 1;
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
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axis, List list, Entity entity)
	{
		final int meta = world.getBlockMetadata(i, j, k);

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.875F, 1.0F);
		super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);

		final float thick = 0.125F;
		final float j1 = 0.875F;
		final float j2 = 1.0F;
		float i1 = 0.0F;
		float i2 = 1.0F;
		float k1 = 0.0F;
		float k2 = 1.0F;

		final boolean boolXPos = canConnectPaddyTo(world, i + 1, j, k, meta);
		final boolean boolXNeg = canConnectPaddyTo(world, i - 1, j, k, meta);
		final boolean boolYPos = canConnectPaddyTo(world, i, j, k + 1, meta);
		final boolean boolYNeg = canConnectPaddyTo(world, i, j, k - 1, meta);

		if (!boolXPos)
		{
			i1 = 1.0F - thick;
			i2 = 1.0F;
			k1 = 0.0F + thick;
			k2 = 1.0F - thick;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		if (!boolXNeg)
		{
			i1 = 0.0F;
			i2 = 0.0F + thick;
			k1 = 0.0F + thick;
			k2 = 1.0F - thick;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		if (!boolYPos)
		{
			i1 = 0.0F + thick;
			i2 = 1.0F - thick;
			k1 = 1.0F - thick;
			k2 = 1.0F;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		if (!boolYNeg)
		{
			i1 = 0.0F + thick;
			i2 = 1.0F - thick;
			k1 = 0.0F;
			k2 = 0.0F + thick;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		//corners
		if ((!canConnectPaddyTo(world, i - 1, j, k - 1, meta)) || (!boolXNeg) || (!boolYNeg))
		{
			this.setBlockBounds(0.0F, j1, 0.0F, 0.0F + thick, j2, 0.0F + thick);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		if ((!canConnectPaddyTo(world, i + 1, j, k - 1, meta)) || (!boolXPos) || (!boolYNeg))
		{
			this.setBlockBounds(1.0F - thick, j1, 0.0F, 1.0F, j2, 0.0F + thick);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		if ((!canConnectPaddyTo(world, i - 1, j, k + 1, meta)) || (!boolXNeg) || (!boolYPos))
		{
			this.setBlockBounds(0.0F, j1, 1.0F - thick, 0.0F + thick, j2, 1.0F);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		if ((!canConnectPaddyTo(world, i + 1, j, k + 1, meta)) || (!boolXPos) || (!boolYPos))
		{
			this.setBlockBounds(1.0F - thick, j1, 1.0F - thick, 1.0F, j2, 1.0F);
			super.addCollisionBoxesToList(world, i, j, k, axis, list, entity);
		}

		this.setBlockBoundsForItemRender();
	}

	public boolean canConnectPaddyTo(IBlockAccess world, int i, int j, int k, int m)
	{
		if (m > 0)
		{
			m = 1;
		}

		int meta = world.getBlockMetadata(i, j, k);

		if (meta > 0)
		{
			meta = 1;
		}

		return this == world.getBlock(i, j, k) && meta == m;
	}
}
