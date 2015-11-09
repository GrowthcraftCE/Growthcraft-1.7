package growthcraft.bees.common.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.bees.client.renderer.RenderBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.util.ItemUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBeeBox extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	private final float honeyCombSpawnRate = GrowthCraftBees.getConfig().beeBoxHoneyCombSpawnRate;
	private final float honeySpawnRate = GrowthCraftBees.getConfig().beeBoxHoneySpawnRate;
	private final float beeSpawnRate = GrowthCraftBees.getConfig().beeBoxBeeSpawnRate;
	private final float flowerSpawnRate = GrowthCraftBees.getConfig().beeBoxFlowerSpawnRate;
	// bonus
	private final float bonus = 2.50F;

	private Random rand = new Random();

	public BlockBeeBox(Material material)
	{
		super(material);
		this.isBlockContainer = true;
		setBlockTextureName("grcbees:beebox");
		setTickRandomly(true);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setBlockName("grc.beeBox");
		setCreativeTab(GrowthCraftBees.tab);
	}

	public BlockBeeBox()
	{
		this(Material.wood);
	}

	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		for (int i = 0; i < 6; ++i)
		{
			list.add(new ItemStack(block, 1, i));
		}
	}

	// for lack of a better name, can this BeeBox do any work?
	private boolean canDoWork(World world, int x, int y, int z)
	{
		if (world.canLightningStrikeAt(x, y + 1, z))
			return false;
		return world.getBlockLightValue(x, y + 1, z) >= 9;
	}

	private boolean isBlockFlower(Block block, int meta)
	{
		return BeesRegistry.instance().isBlockFlower(block, meta);
	}

	private void gatherFlowersInRadius(World world, int x, int y, int z, int checkSize, List<List> list)
	{
		final int i = x - ((checkSize - 1) / 2);
		final int k = z - ((checkSize - 1) / 2);

		for (int loopx = 0; loopx < checkSize; loopx++)
		{
			for (int loopz = 0; loopz < checkSize; loopz++)
			{
				final int fx = i + loopx;
				final int fy = y;
				final int fz = k + loopz;
				if (!world.isAirBlock(fx, fy, fz))
				{
					final Block flower = world.getBlock(fx, y, fz);
					final int fm = world.getBlockMetadata(fx, y, fz);
					if (flower != null)
					{
						if (isBlockFlower(flower, fm))
						{
							list.add(Arrays.asList(flower, fm));
						}
					}
				}
			}
		}
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);

		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);

		if (te == null)
			return;

		if (!canDoWork(world, x, y, z))
			return;

		if (!te.hasBees())
			return;

		float f = this.getGrowthRate(world, x, y, z);

		if (te.countCombs() < 27)
		{
			if (te.hasMaxBees())
			{
				if (random.nextInt((int)(this.honeyCombSpawnRate / f) + 1) == 0)
				{
					te.spawnHoneyComb();
				}
			}
			else
			{
				if (random.nextInt(5) == 0)
				{
					if (random.nextInt((int)(this.beeSpawnRate / f) + 1) == 0)
					{
						te.spawnBee();
					}
				}
				else
				{
					if (random.nextInt((int)(this.honeyCombSpawnRate / f) + 1) == 0)
					{
						te.spawnHoneyComb();
					}
				}
			}
		}
		else
		{
			if (random.nextInt((int)(this.honeySpawnRate / f) + 1) == 0)
			{
				if (te.hasMaxBees())
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

		if (random.nextInt((int)(this.flowerSpawnRate / f) + 1) == 0)
		{
			final int checkSize = 5;
			final List<List> list = te.flowerList;

			list.clear();
			gatherFlowersInRadius(world, x, y, z, checkSize, list);

			if (list.size() > 0)
			{
				final int i = x - ((checkSize - 1) / 2);
				final int k = z - ((checkSize - 1) / 2);
				final int random_x = i + random.nextInt(checkSize);
				final int random_z = k + random.nextInt(checkSize);
				final List random_list = list.get(random.nextInt(list.size()));
				final Block block = (Block)random_list.get(0);
				if (block != null)
				{
					if (block.canPlaceBlockAt(world, random_x, y, random_z))
					{
						world.setBlock(random_x, y, random_z, (Block)random_list.get(0), (Integer)random_list.get(1), 3);
					}
				}
			}
		}
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		final int checkSize = 5;
		final int i = x - ((checkSize - 1) / 2);
		final int k = z - ((checkSize - 1) / 2);
		float f = 1.0F;

		for (int loopx = 0; loopx < checkSize; loopx++)
		{
			for (int loopz = 0; loopz < checkSize; loopz++)
			{
				final Block flower = world.getBlock(i + loopx, y, k + loopz);
				final int fm = world.getBlockMetadata(i + loopx, y, k + loopz);
				final Block soil = world.getBlock(i + loopx, y - 1, k + loopz);
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

		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);

		if (te != null)
		{
			final int bees = te.countBees();
			final float div = 2.0F - (0.015625F * bees);

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
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
			if (te != null)
			{
				if (te.hasBees())
				{
					world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F),
						"grcbees:buzz", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
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
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
			if (te != null)
			{
				final ItemStack itemstack = player.inventory.getCurrentItem();

				if (itemstack != null && itemstack.getItem() == Items.flower_pot && te.isHoneyEnough())
				{
					ItemUtils.addStackToPlayer(GrowthCraftBees.honeyJar.asStack(), player, world, x, y, z, false);
					ItemUtils.decreaseStackOnPlayer(itemstack, player);
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
	public int onBlockPlaced(World world, int x, int y, int z, int side, float fx, float fy, float fz, int meta)
	{
		return super.onBlockPlaced(world, x, y, z, side, fx, fy, fz, meta);
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
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);

		if (te != null)
		{
			for (int index = 0; index < te.getSizeInventory(); ++index)
			{
				final ItemStack stack = te.getStackInSlot(index);

				ItemUtils.spawnItemFromStack(world, x, y, z, stack, rand);
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
		return ForgeDirection.UP == side;
	}

	/************
	 * STUFF
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)
	//public Item getItem(World world, int x, int y, int z)
	//{
	//	return GrowthCraftBees.beeBox.getItem();
	//}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityBeeBox();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public int damageDropped(int damage)
	{
		return damage;
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par3)
	{
		return GrowthCraftBees.beeBox.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	/************
	 * TEXTURES
	 ************/
	@SideOnly(Side.CLIENT)
	protected void registerBeeBoxIcons(IIconRegister reg, String basename, int offset)
	{
		icons[offset * 4] = reg.registerIcon(getTextureName() + "_bottom_" + basename);
		icons[offset * 4 + 1] = reg.registerIcon(getTextureName() + "_top_" + basename);
		icons[offset * 4 + 2] = reg.registerIcon(getTextureName() + "_side_" + basename);
		icons[offset * 4 + 3] = reg.registerIcon(getTextureName() + "_side_" + basename + "_honey");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		icons = new IIcon[6 * 4];

		for (int i = 0; i < 6; ++i)
		{
			registerBeeBoxIcons(reg, "" + i, i);
		}
	}

	@SideOnly(Side.CLIENT)
	protected int calculateIconOffset(int meta)
	{
		return MathHelper.clamp_int(meta, 0, icons.length / 4 - 1) * 4;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final int offset = calculateIconOffset(meta);
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
		if (side == 0)
		{
			return icons[offset];
		}
		else if (side == 1)
		{
			return icons[offset + 1];
		}
		else
		{
			if (te != null && te.isHoneyEnough())
			{
				return icons[offset + 3];
			}
		}
		return icons[offset + 2];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		final int offset = calculateIconOffset(meta);
		if (side == 0)
		{
			return icons[offset];
		}
		else if (side == 1)
		{
			return icons[offset + 1];
		}
		return icons[offset + 2];
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
		final float f = 0.0625F;
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
		final TileEntityBeeBox te = (TileEntityBeeBox) world.getTileEntity(x, y, z);
		return te.countHoney() * 15 / 27;
	}
}
