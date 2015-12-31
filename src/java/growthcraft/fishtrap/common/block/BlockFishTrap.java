package growthcraft.fishtrap.common.block;

import java.util.Random;

import growthcraft.api.fishtrap.FishTrapRegistry;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.core.Utils;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;
import growthcraft.fishtrap.GrowthCraftFishTrap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BlockFishTrap extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	private final float chance = GrowthCraftFishTrap.getConfig().fishTrapCatchRate;
	private Random rand = new Random();

	public BlockFishTrap()
	{
		super(Material.wood);
		this.isBlockContainer = true;
		this.setTickRandomly(true);
		this.setHardness(0.4F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.fishTrap");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);

		final TileEntityFishTrap te = (TileEntityFishTrap)world.getTileEntity(x, y, z);

		if (te != null)
		{
			if (canCatch(world, x, y, z))
			{
				doCatch(world, x, y, z, random, te, false);
			}
		}
	}

	private void doCatch(World world, int x, int y, int z, Random random, TileEntityFishTrap te, boolean debugFlag)
	{
		float f = this.getCatchRate(world, x, y, z);
		boolean flag;
		if (GrowthCraftFishTrap.getConfig().useBiomeDict)
		{
			final BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
			flag = BiomeDictionary.isBiomeOfType(biome, Type.WATER);
		}
		else
		{
			flag = Utils.isIDInList(world.getBiomeGenForCoords(x, z).biomeID, GrowthCraftFishTrap.getConfig().biomesList);
		}

		if (flag)
		{
			f *= 1 + (75 / 100);
		}

		if (random.nextInt((int)(this.chance / f) + 1) == 0 || debugFlag)
		{
			final ItemStack item = pickCatch(world);
			if (item != null)
			{
				te.addStack(item);
			}
		}
	}

	private ItemStack pickCatch(World world)
	{
		float f1 = world.rand.nextFloat();
		final float f2 = 0.1F;
		final float f3 = 0.05F;

		if (f1 < f2)
		{
			return FishTrapRegistry.instance().getJunkList(world);
		}
		f1 -= f2;

		if (f1 < f3)
		{
			return FishTrapRegistry.instance().getTreasureList(world);
		}
		f1 -= f3;

		return FishTrapRegistry.instance().getFishList(world);
	}

	private boolean isWater(Block block)
	{
		return BlockCheck.isWater(block);
	}

	private boolean canCatch(World world, int x, int y, int z)
	{
		return isWater(world.getBlock(x, y, z - 1)) ||
			isWater(world.getBlock(x, y, z + 1)) ||
			isWater(world.getBlock(x - 1, y, z)) ||
			isWater(world.getBlock(x + 1, y, z));
	}

	private float getCatchRate(World world, int x, int y, int z)
	{
		final int checkSize = 3;
		final int i = x - ((checkSize - 1) / 2);
		final int j = y - ((checkSize - 1) / 2);
		final int k = z - ((checkSize - 1) / 2);
		float f = 1.0F;

		for (int loopy = 0; loopy <= checkSize; loopy++)
		{
			for (int loopx = 0; loopx <= checkSize; loopx++)
			{
				for (int loopz = 0; loopz <= checkSize; loopz++)
				{
					final Block water = world.getBlock(i + loopx, j + loopy, k + loopz);
					float f1 = 0.0F;
					//1.038461538461538;

					if (water != null && isWater(water))
					{
						//f1 = 1.04F;
						f1 = 3.0F;
						//f1 = 17.48F;
					}

					f1 /= 4.0F;

					f += f1;
				}
			}
		}

		return f;
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			player.openGui(GrowthCraftFishTrap.instance, 0, world, x, y, z);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			((TileEntityFishTrap)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		final TileEntityFishTrap te = (TileEntityFishTrap)world.getTileEntity(x, y, z);

		if (te != null)
		{
			for (int index = 0; index < te.getSizeInventory(); ++index)
			{
				final ItemStack stack = te.getStackInSlot(index);

				if (stack != null)
				{
					final float f = this.rand.nextFloat() * 0.8F + 0.1F;
					final float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
					final float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

					while (stack.stackSize > 0)
					{
						int k1 = this.rand.nextInt(21) + 10;

						if (k1 > stack.stackSize)
						{
							k1 = stack.stackSize;
						}

						stack.stackSize -= k1;
						final EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(stack.getItem(), k1, stack.getItemDamage()));

						if (stack.hasTagCompound())
						{
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
						}

						final float f3 = 0.05F;
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
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(GrowthCraftFishTrap.fishTrap.getBlock());
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityFishTrap();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random random, int par3)
	{
		return GrowthCraftFishTrap.fishTrap.getItem();
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
		this.icons = new IIcon[1];

		icons[0] = reg.registerIcon("grcfishtrap:fishtrap");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return icons[0];
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
		if (this == world.getBlock(x, y, z)) return false;
		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 0;
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
		final TileEntityFishTrap te = (TileEntityFishTrap) world.getTileEntity(x, y, z);
		if (te != null)
		{
			return Container.calcRedstoneFromInventory(te);
		}
		return 0;
	}
}
