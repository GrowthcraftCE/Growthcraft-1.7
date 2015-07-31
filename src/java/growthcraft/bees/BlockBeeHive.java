package growthcraft.bees;

import growthcraft.core.GrowthCraftCore;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBeeHive extends Block
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockBeeHive()
	{
		super(Material.plants);
		this.setHardness(0.6F);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.beeHive");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * TICK
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		if (random.nextInt(24) == 0)
		{
			world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "grcbees:buzz", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}
	}

	/************
	 * TRIGGERS
	 ************/
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
			Block zneg = world.getBlock(x, y, z - 1);
			Block zpos = world.getBlock(x, y, z + 1);
			Block xneg = world.getBlock(x - 1, y, z);
			Block xpos = world.getBlock(x + 1, y, z);
			byte b0 = 3;

			if (zneg.func_149730_j() && !zpos.func_149730_j())
			{
				b0 = 3;
			}

			if (zpos.func_149730_j() && !zneg.func_149730_j())
			{
				b0 = 2;
			}

			if (xneg.func_149730_j() && !xpos.func_149730_j())
			{
				b0 = 5;
			}

			if (xpos.func_149730_j() && !xneg.func_149730_j())
			{
				b0 = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		int face = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (face == 0)
		{
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (face == 1)
		{
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (face == 2)
		{
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (face == 3)
		{
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		super.onNeighborBlockChange(world, x, y, z, par5);
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, 3);
		}
	}

	public boolean canBlockStay(World world, int x, int y, int z)
	{
		if (world.isAirBlock(x, y + 1, z))
		{
			return false;
		}
		return world.getBlock(x, y + 1, z).isLeaves(world, x, y + 1, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return true;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random rand, int par3)
	{
		return GrowthCraftBees.bee;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return rand.nextInt(8);
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, x, y, z, par5, par6, 0);
		if (!world.isRemote)
		{
			int max = new Random().nextInt(8);
			if (max > 0)
			{
				for (int loop = 0; loop < max; loop++)
				{
					int m = 0;
					if (new Random().nextInt(2) == 0)
					{
						m = 1;
					}
					this.dropBlockAsItem(world, x, y, z, new ItemStack(GrowthCraftBees.honeyComb, 1, m));
				}
			}
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("grcbees:beehive_front");
		tex[1] = reg.registerIcon("grcbees:beehive_sides");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return side != meta ? this.tex[1] : this.tex[0];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderBeeHive.id;
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
		float f = 0.0625F;
		this.setBlockBounds(2*f, 0.0F, 2*f, 14*f, 1.0F, 14*f);
		//		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity)
	{
		float f = 0.0625F;
		this.setBlockBounds(4*f, 0.0F, 4*f, 12*f, 14*f, 12*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(3*f, 1*f, 3*f, 13*f, 13*f, 13*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(2*f, 4*f, 2*f, 14*f, 10*f, 14*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(7*f, 14*f, 7*f, 9*f, 1.0F, 9*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBoundsForItemRender();
	}

	//	@Override
	//	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	//	{
	//		float f = 0.0625F;
	//		this.setBlockBounds(2*f, 0.0F, 2*f, 14*f, 1.0F, 14*f);
	//	}
}
