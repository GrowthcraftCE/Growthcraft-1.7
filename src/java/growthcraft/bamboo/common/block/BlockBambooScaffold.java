package growthcraft.bamboo.common.block;

import java.util.Random;

import growthcraft.bamboo.client.renderer.RenderBambooScaffold;
import growthcraft.core.GrowthCraftCore;

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

public class BlockBambooScaffold extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockBambooScaffold()
	{
		super(Material.wood);
		this.setStepSound(soundTypeWood);
		this.setResistance(0.2F);
		this.setHardness(0.5F);
		this.setCreativeTab(GrowthCraftCore.creativeTab);
		this.setBlockName("grc.bambooScaffold");
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		this.onNeighborBlockChange(world, x, y, z, null);
	}

	/************
	 * TRIGGERS
	 ************/
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float float7, float float8, float float9)
	{
		final ItemStack itemstack = player.inventory.getCurrentItem();
		if (itemstack != null)
		{
			if (itemstack.getItem() == Item.getItemFromBlock(this))
			{
				final int loop = world.getActualHeight();
				for (int j = y + 1; j < loop; j++)
				{
					final Block block = world.getBlock(x, j, z);
					if ((block == null) || (world.isAirBlock(x, j, z)) || (block.isReplaceable(world, x, j, z)))
					{
						if (!world.isRemote)
						{
							if (world.setBlock(x, j, z, this, 0, 3) && !player.capabilities.isCreativeMode)
							{
								itemstack.stackSize -= 1;
							}
							return true;
						}
					}
					if (block != this)
					{
						return false;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, 3);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		entity.fallDistance = 0.0F;
		if (entity.isCollidedHorizontally)
		{
			entity.motionY = 0.2D;
		}
		else if (entity.isSneaking())
		{
			final double d = entity.prevPosY - entity.posY;
			entity.boundingBox.minY += d;
			entity.boundingBox.maxY += d;
			entity.posY = entity.prevPosY;
		}
		else
		{
			entity.motionY = -0.1D;
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return canBlockStay(world, x, y, z);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y -1 , z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP)) return true;
		if (checkSides(world, x, y, z)) return true;

		return false;
	}

	private boolean checkSides(World world, int x, int y, int z)
	{
		final boolean flag = world.getBlock(x + 1, y, z) == this;
		final boolean flag1 = world.getBlock(x - 1, y, z) == this;
		final boolean flag2 = world.getBlock(x, y, z + 1) == this;
		final boolean flag3 = world.getBlock(x, y, z - 1) == this;

		if (!flag && !flag1 && !flag2 && !flag3) return false;

		if (flag && world.getBlock(x + 1, y - 1, z).isSideSolid(world, x + 1, y - 1, z, ForgeDirection.UP)) return true;
		if (flag1 && world.getBlock(x - 1, y - 1, z).isSideSolid(world, x - 1, y - 1, z, ForgeDirection.UP)) return true;
		if (flag2 && world.getBlock(x, y - 1, z + 1).isSideSolid(world, x, y - 1, z + 1, ForgeDirection.UP)) return true;
		if (flag3 && world.getBlock(x, y - 1, z - 1).isSideSolid(world, x, y - 1, z - 1, ForgeDirection.UP)) return true;

		if (flag && world.getBlock(x + 2, y - 1, z).isSideSolid(world, x + 2, y - 1, z, ForgeDirection.UP)) return true;
		if (flag1 && world.getBlock(x - 2, y - 1, z).isSideSolid(world, x - 2, y - 1, z, ForgeDirection.UP)) return true;
		if (flag2 && world.getBlock(x, y - 1, z + 2).isSideSolid(world, x, y - 1, z + 2, ForgeDirection.UP)) return true;
		if (flag3 && world.getBlock(x, y - 1, z - 2).isSideSolid(world, x, y - 1, z - 2, ForgeDirection.UP)) return true;

		return false;
	}

	/************
	 * STUFF
	 ************/

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return ForgeDirection.UP == side;
	}

	/************
	 * TEXTURES
	 ************/
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[2];

		icons[0] = reg.registerIcon("grcbamboo:block");
		icons[1] = reg.registerIcon("grcbamboo:scaffold");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			return icons[0];
		}
		return icons[1];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderBambooScaffold.id;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		final float f = 0.125F;
		return AxisAlignedBB.getBoundingBox(x + this.minX + f, y + this.minY, z + this.minZ + f, x + this.maxX - f, y + this.maxY, z + this.maxZ - f);
	}
}
