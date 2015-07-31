package growthcraft.bamboo;

import growthcraft.core.GrowthCraftCore;

import java.util.Random;

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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooScaffold extends Block
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockBambooScaffold()
	{
		super(Material.wood);
		this.setStepSound(soundTypeWood);
		this.setResistance(0.2F);
		this.setHardness(0.5F);
		this.setCreativeTab(GrowthCraftCore.tab);
		this.setBlockName("grc.bambooScaffold");
		//setBlockBounds(0.0F, 0.02083333F, 0.0F, 1.0F, 0.9791667F, 1.0F);
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
		ItemStack itemstack = player.inventory.getCurrentItem();
		if (itemstack != null)
		{
			if (itemstack.getItem() == Item.getItemFromBlock(this))
			{
				int j = y + 1;
				for (int loop = world.getActualHeight(); j < loop; j++)
				{
					Block block = world.getBlock(x, j, z);
					if ((block == null) || (world.isAirBlock(x, j, z)) || (block.isReplaceable(world, x, j, z)))
					{
						if (!world.isRemote)
						{
							if ((world.setBlock(x, j, z, this, 0, 3) & !player.capabilities.isCreativeMode))
							{
								itemstack.stackSize -= 1;
								if (itemstack.stackSize == 0)
								{
									itemstack = null;
								}
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
		//world.scheduleBlockUpdate(x, y, z, this.blockID, 1);
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, 3);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		/*if ((entity instanceof EntityPlayerMP))
		{
			((EntityPlayerMP)entity).playerNetServerHandler.floatingTickCount = 0;
		}*/

		entity.fallDistance = 0.0F;
		if (entity.isCollidedHorizontally)
		{
			entity.motionY = 0.2D;
		}
		else if (entity.isSneaking())
		{
			double d = entity.prevPosY - entity.posY;
			entity.boundingBox.minY += d;
			entity.boundingBox.maxY += d;
			entity.posY = entity.prevPosY;
		}
		else
		{
			entity.motionY = -0.1D;
		}


		/*entity.fallDistance = 0.0F;
		if (entity.isCollidedHorizontally)
		{
			entity.motionY = 0.2D;
		}
		else
		{
			entity.motionY = 0.0D;
		}*/
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
		boolean flag = world.getBlock(x + 1, y, z) == this;
		boolean flag1 = world.getBlock(x - 1, y, z) == this;
		boolean flag2 = world.getBlock(x, y, z + 1) == this;
		boolean flag3 = world.getBlock(x, y, z - 1) == this;

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
	/*@Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity)
	{
		return true;
	}*/

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return side == ForgeDirection.UP;
	}

	/************
	 * TEXTURES
	 ************/
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("grcbamboo:block");
		tex[1] = reg.registerIcon("grcbamboo:scaffold");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[0];
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

	/************
	 * BOXES
	 ************/
	/*@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
	{
		//float f = 0.0625F;
		//float minz = f*5;
		//float maxz = f*11;
		//float minx = f*5;
		//float maxx = f*11;

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}*/

	/*@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
	{
		float f = 0.0625F;
		float minz = f*5;
		float maxz = f*11;
		float minx = f*5;
		float maxx = f*11;

		this.setBlockBounds(minx, 0.0F, minz, maxx, 1.0F, maxz);
		AxisAlignedBB bb = getCollisionBoundingBoxFromPool(world, x, y, z);
		if ((bb != null) && (aabb.intersectsWith(bb)))
		{
			list.add(bb);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}*/

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		float f = 0.125F;
		return AxisAlignedBB.getBoundingBox(x + this.minX + f, y + this.minY, z + this.minZ + f, x + this.maxX - f, y + this.maxY, z + this.maxZ - f);
		//float f = 0.1F;
		//return AxisAlignedBB.getAABBPool().getAABB(x + f, y, z + f, x + 1 - f, y + 1, z + 1 - f);
	}
}
