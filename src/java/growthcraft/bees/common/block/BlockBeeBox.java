package growthcraft.bees.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.bees.client.renderer.RenderBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.util.ItemUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBeeBox extends GrcBlockContainer
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	// bonus
	private int flammability;
	private int fireSpreadSpeed;

	public BlockBeeBox(Material material)
	{
		super(material);
		setBlockTextureName("grcbees:beebox");
		setTickRandomly(true);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setBlockName("grc.BeeBox.Minecraft");
		setCreativeTab(GrowthCraftBees.tab);
		setTileEntityType(TileEntityBeeBox.class);
	}

	public BlockBeeBox()
	{
		this(Material.wood);
	}

	public BlockBeeBox setFlammability(int flam)
	{
		this.flammability = flam;
		return this;
	}

	public BlockBeeBox setFireSpreadSpeed(int speed)
	{
		this.fireSpreadSpeed = speed;
		return this;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return flammability;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return fireSpreadSpeed;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		for (int i = 0; i < 6; ++i)
		{
			list.add(new ItemStack(block, 1, i));
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		super.updateTick(world, x, y, z, rand);
		final TileEntityBeeBox te = getTileEntity(world, x, y, z);
		if (te != null) te.updateBlockTick();
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
		if (super.onBlockActivated(world, x, y, z, player, meta, par7, par8, par9)) return true;
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
			if (te != null)
			{
				player.openGui(GrowthCraftBees.instance, 0, world, x, y, z);
				return true;
			}
			return false;
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

				ItemUtils.spawnBrokenItemStack(world, x, y, z, stack, world.rand);
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
		icons[offset * 4] = reg.registerIcon(getTextureName() + basename + "bottom");
		icons[offset * 4 + 1] = reg.registerIcon(getTextureName() + basename + "top");
		icons[offset * 4 + 2] = reg.registerIcon(getTextureName() + basename + "side");
		icons[offset * 4 + 3] = reg.registerIcon(getTextureName() + basename + "side_honey");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[6 * 4];

		registerBeeBoxIcons(reg, "/minecraft/oak/", 0);
		registerBeeBoxIcons(reg, "/minecraft/spruce/", 1);
		registerBeeBoxIcons(reg, "/minecraft/birch/", 2);
		registerBeeBoxIcons(reg, "/minecraft/jungle/", 3);
		registerBeeBoxIcons(reg, "/minecraft/acacia/", 4);
		registerBeeBoxIcons(reg, "/minecraft/darkoak/", 5);
	}

	@SideOnly(Side.CLIENT)
	protected int calculateIconOffset(int meta)
	{
		return MathHelper.clamp_int(meta, 0, icons.length / 4 - 1) * 4;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final int offset = calculateIconOffset(meta);
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
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
			if (te != null && te.isHoneyEnough(6))
			{
				return icons[offset + 3];
			}
		}
		return icons[offset + 2];
	}

	@SideOnly(Side.CLIENT)
	@Override
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
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity)
	{
		final float f = 0.0625F;
		// LEGS
		setBlockBounds(3*f, 0.0F, 3*f, 5*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBounds(11*f, 0.0F, 3*f, 13*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBounds(3*f, 0.0F, 11*f, 5*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBounds(11*f, 0.0F, 11*f, 13*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		// BODY
		setBlockBounds(1*f, 3*f, 1*f, 15*f, 10*f, 15*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		// ROOF
		setBlockBounds(0.0F, 10*f, 0.0F, 1.0F, 13*f, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBounds(2*f, 13*f, 2*f, 14*f, 1.0F, 14*f);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
		setBlockBoundsForItemRender();
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
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
		return te.countHoney() * 15 / 27;
	}
}
