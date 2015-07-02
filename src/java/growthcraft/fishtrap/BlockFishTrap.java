package growthcraft.fishtrap;

import growthcraft.api.fishtrap.FishTrapRegistry;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.Utils;

import java.util.Random;

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
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFishTrap extends BlockContainer
{
	//constants
	private final float chance = GrowthCraftFishTrap.fishTrap_catchRate;

	Random rand = new Random();
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

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
		TileEntityFishTrap te = (TileEntityFishTrap)world.getTileEntity(x, y, z);

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
		if (GrowthCraftFishTrap.instance.fishTrap_useBiomeDict)
		{
			BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
			flag = (BiomeDictionary.isBiomeOfType(biome, Type.WATER));
		}
		else
		{
			flag = Utils.isIDInList(world.getBiomeGenForCoords(x, z).biomeID, GrowthCraftFishTrap.instance.fishTrap_biomesList);
		}

		if (flag)
		{
			f *= (1 + (75/100));
		}

		if (random.nextInt((int)(this.chance / f) + 1) == 0 || debugFlag)
		{			
			ItemStack item = pickCatch(world);
			if (item != null)
			{
				te.addStack(item);
			}
		}
	}

	private ItemStack pickCatch(World world)
	{
		float f1 = world.rand.nextFloat();
		float f2 = 0.1F; //- 0 * 0.025F - 0 * 0.01F;
		float f3 = 0.05F; //+ 0 * 0.01F - 0 * 0.01F;
		//System.out.println(f2 + " " + f3);

		f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
		f3 = MathHelper.clamp_float(f3, 0.0F, 1.0F);

		ItemStack item;
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
		return block != null && block.getMaterial() != null ? block.getMaterial() == Material.water : false;
	}

	private boolean canCatch(World world, int x, int y, int z)
	{
		return isWater(world.getBlock(x, y, z - 1)) || isWater(world.getBlock(x, y, z + 1)) || isWater(world.getBlock(x - 1, y, z)) || isWater(world.getBlock(x + 1, y, z));
	}

	private float getCatchRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		int checkSize = 3;
		int i = x - ((checkSize - 1) / 2);
		int j = y - ((checkSize - 1) / 2);
		int k = z - ((checkSize - 1) / 2);

		for (int loopy = 0; loopy <= checkSize; loopy++)
		{
			for (int loopx = 0; loopx <= checkSize; loopx++)
			{
				for (int loopz = 0; loopz <= checkSize; loopz++)
				{
					Block water = world.getBlock(i + loopx, j + loopy, k + loopz);
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
			//TileEntityFishTrap te = (TileEntityFishTrap)world.getTileEntity(x, y, z);

			/*if (te != null)
			{
				doCatch(world, x, y, z, world.rand, te, true);
				doCatch(world, x, y, z, world.rand, te, true);
				doCatch(world, x, y, z, world.rand, te, true);
				doCatch(world, x, y, z, world.rand, te, true);
			}*/
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
		TileEntityFishTrap te = (TileEntityFishTrap)world.getTileEntity(x, y, z);

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
	 * STUFF
	 ************/	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(GrowthCraftFishTrap.fishTrap);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityFishTrap();
	}


	/*@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int id, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(id, 1, 0));
		list.add(new ItemStack(id, 1, 1));
		list.add(new ItemStack(id, 1, 2));
		list.add(new ItemStack(id, 1, 3));
		list.add(new ItemStack(id, 1, 4));
	}*/

	/************
	 * DROPS
	 ************/	
	@Override
	public Item getItemDropped(int par1, Random rand, int par3)
	{
		return Item.getItemFromBlock(GrowthCraftFishTrap.fishTrap);
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 1;
	}

	/*@Override
	public int damageDropped(int meta)
	{
		return MathHelper.clamp_int(meta, 0, 4);
	}*/

	/************
	 * TEXTURES
	 ************/	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		//tex = new Icon[6];
		tex = new IIcon[1];

		tex[0] = reg.registerIcon("grcfishtrap:fishtrap");
		/*tex[1] = reg.registerIcon("grcfishtrap:fishtrap_1");
		tex[2] = reg.registerIcon("grcfishtrap:fishtrap_2");
		tex[3] = reg.registerIcon("grcfishtrap:fishtrap_3");
		tex[4] = reg.registerIcon("grcfishtrap:fishtrap_4");
		tex[5] = reg.registerIcon("grcfishtrap:fishtraplead");*/
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		//return this.tex[MathHelper.clamp_int(meta, 0, 4)];
		return this.tex[0];
	}

	/************
	 * RENDERS
	 ************/	
	/*@Override
	public int getRenderType()
	{
		return RenderFishTrap.id;
	}*/

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
		return world.getBlock(x, y, z) == this ? false : super.shouldSideBeRendered(world, x, y, z, side);
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
		TileEntityFishTrap te = (TileEntityFishTrap) world.getTileEntity(x, y, z);
		if (te != null)
		{
			return Container.calcRedstoneFromInventory(te);
		}
		return 0;
	}
}
