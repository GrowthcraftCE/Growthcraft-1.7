package growthcraft.bees;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBeeBox extends BlockContainer
{
	//honeycomb
	private final float growth = GrowthCraftBees.beeBox_growth;
	//honey + bee + flower
	private final float growth2 = GrowthCraftBees.beeBox_growth2;
	// bonus
	private final float bonus = 2.50F;

	private Random rand = new Random();
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockBeeBox()
	{
		super(Material.wood);
		this.isBlockContainer = true;
		this.setTickRandomly(true);
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.beeBox");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);
		TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);

		if (te != null)
		{
			if (!world.canLightningStrikeAt(x, y + 1, z) && world.getBlockLightValue(x, y + 1, z) >= 9);
			{
				if (te.countBees() != 0)
				{
					float f = this.getGrowthRate(world, x, y, z);

					if (te.countCombs() < 27)
					{
						if (te.countBees() == 64)
						{
							if (random.nextInt((int)(this.growth / f) + 1) == 0)
							{
								te.spawnHoneyComb();
							}
						}
						else
						{
							if (random.nextInt(5) == 0)
							{
								if (random.nextInt((int)(this.growth2 / f) + 1) == 0)
								{
									te.spawnBee();
								}
							}
							else
							{
								if (random.nextInt((int)(this.growth / f) + 1) == 0)
								{
									te.spawnHoneyComb();
								}
							}
						}
					}
					else
					{
						if (random.nextInt((int)(this.growth2 / f) + 1) == 0)
						{
							if (te.countBees() == 64)
							{
								te.fillHoneyComb();
							}
							else
							{
								if (random.nextInt(5) == 0)
								{
									te.spawnBee();
								}
								else
								{
									te.fillHoneyComb();
								}
							}
						}
					}

					f = 7.48F / (2.0F - (0.015625F * te.countBees()));
					if (te.hasBonus())
					{
						f *= this.bonus;
					}

					if (random.nextInt((int)(this.growth2 / f) + 1) == 0)
					{
						//System.out.println("spread initiated");

						int checkSize = 5;
						int i = x - ((checkSize - 1) / 2);
						int k = z - ((checkSize - 1) / 2);
						List<List> list = new ArrayList<List>();
						Block flower;
						int fm;

						for (int loopx = 0; loopx < checkSize; loopx++)
						{
							for (int loopz = 0; loopz < checkSize; loopz++)
							{
								flower = world.getBlock(i + loopx, y, k + loopz);
								fm = world.getBlockMetadata(i + loopx, y, k + loopz);
								if (isBlockFlower(flower, fm))
								{
									list.add(Arrays.asList(world.getBlock(i + loopx, y, k + loopz), world.getBlockMetadata(i + loopx, y, k + loopz)));
								}
							}
						}

						if (list.size() > 0)
						{
							int random_x = i + random.nextInt(checkSize);//MathHelper.getRandomIntegerInRange(random, i, i + (checkSize - 1));
							int random_z = k + random.nextInt(checkSize);//MathHelper.getRandomIntegerInRange(random, k, k + (checkSize - 1));
							List random_list = list.get(random.nextInt(list.size()));
							Block block = (Block) random_list.get(0);
							if (block != null)
							{
								if (block.canPlaceBlockAt(world, random_x, y, random_z))
								{
									world.setBlock(random_x, y, random_z, (Block)random_list.get(0), (Integer) random_list.get(1), 3);
									//System.out.println("SUCCESS!" + " " + random_x + " " + random_z + " " + block);
								}
								//else
								//{
								//	System.out.println("FAIL!" + " " + random_x + " " + random_z + " " + block);
								//}
							}
						}
					}
				}
			}

		}
	}

	private boolean isBlockFlower(Block block, int meta)
	{
		return BeesRegistry.instance().isBlockFlower(block, meta);
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		int checkSize = 5;
		int i = x - ((checkSize - 1) / 2);
		int k = z - ((checkSize - 1) / 2);

		for (int loopx = 0; loopx < checkSize; loopx++)
		{
			for (int loopz = 0; loopz < checkSize; loopz++)
			{
				Block flower = world.getBlock(i + loopx, y, k + loopz);
				int fm = world.getBlockMetadata(i + loopx, y, k + loopz);
				Block soil = world.getBlock(i + loopx, y - 1, k + loopz);
				float f1 = 0.0F;

				if (soil == Blocks.grass)
				{
					//f1 = 1.0F;
					f1 = 0.36F;

					if (isBlockFlower(flower, fm))
					{
						//f1 = 3.0F;
						f1 = 1.08F;
					}
				}
				else if (flower == Blocks.flower_pot && (world.getBlockMetadata(i + loopx, y, k + loopz) == 1 || world.getBlockMetadata(i + loopx, y, k + loopz) == 2))
				{
					//f1 = 2.0F;
					f1 = 0.72F;
				}

				f1 /= 4.0F;

				f += f1;
			}
		}

		TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);

		if (te != null)
		{
			int bees = te.countBees();
			float div = 2.0F - (0.015625F * bees);

			f /= div;

			if (te.hasBonus())
			{
				f *= this.bonus;
			}
		}

		return f;
	}

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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
			if (te != null)
			{
				ItemStack itemstack = player.inventory.getCurrentItem();

				if (itemstack != null && itemstack.getItem() == Items.flower_pot && te.isHoneyEnough())
				{
					Utils.addStack(new ItemStack(GrowthCraftBees.honeyJar), player, world, x, y, z, false);
					Utils.decreaseStack(itemstack, player);
					te.decreaseHoney();
					te.markDirty();
					world.markBlockForUpdate(x, y, z);
					return true;
				}
				else if (itemstack != null && itemstack.getItem() == Items.dye && itemstack.getItemDamage() == 9)
				{
					te.setTime(9600);
					world.playAuxSFX(2005, x, y, z, 0);
					if (!player.capabilities.isCreativeMode)
					{
						--itemstack.stackSize;
					}
					te.markDirty();
					world.markBlockForUpdate(x, y, z);
					return true;
				}
				else if (itemstack != null && itemstack.getItem() == Items.dye && itemstack.getItemDamage() == 13)
				{
					te.setTime(4800);
					world.playAuxSFX(2005, x, y, z, 0);
					if (!player.capabilities.isCreativeMode)
					{
						--itemstack.stackSize;
					}
					te.markDirty();
					world.markBlockForUpdate(x, y, z);
					return true;
				}
				else
				{
					player.openGui(GrowthCraftBees.instance, 0, world, x, y, z);
				}
			}

			return true;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			((TileEntityBeeBox)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);

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

			world.func_147453_f(x, y, z, par5);
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	/************
	 * CONDITIONS
	 ************/	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return side == side.UP;
	}

	/************
	 * STUFF
	 ************/	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(GrowthCraftBees.beeBox);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityBeeBox();
	}

	/************
	 * DROPS
	 ************/	
	@Override
	public Item getItemDropped(int par1, Random rand, int par3)
	{
		return Item.getItemFromBlock(GrowthCraftBees.beeBox);
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
		tex = new IIcon[3];

		tex[0] = reg.registerIcon("grcbees:beebox_bottom");
		tex[1] = reg.registerIcon("grcbees:beebox_side");
		tex[2] = reg.registerIcon("grcbees:beebox_top");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 0)
		{
			return this.tex[0];
		}
		else if (side == 1)
		{
			return this.tex[2];
		}
		else
		{
			return this.tex[1];
		}
	}

	/************
	 * RENDERS
	 ************/	
	@Override
	public int getRenderType()
	{
		return RenderBeeBox.id;
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
		float f = 0.0625F;
		// LEGS
		this.setBlockBounds(3*f, 0.0F, 3*f, 5*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(11*f, 0.0F, 3*f, 13*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(3*f, 0.0F, 11*f, 5*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(11*f, 0.0F, 11*f, 13*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		// BODY
		this.setBlockBounds(1*f, 3*f, 1*f, 15*f, 10*f, 15*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		// ROOF
		this.setBlockBounds(0.0F, 10*f, 0.0F, 1.0F, 13*f, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		this.setBlockBounds(2*f, 13*f, 2*f, 14*f, 1.0F, 14*f);
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
		TileEntityBeeBox te = (TileEntityBeeBox) world.getTileEntity(x, y, z);
		return te.countHoney() * 15 / 27;
	}
}
