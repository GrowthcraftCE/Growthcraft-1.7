package growthcraft.cellar.block;

import java.util.Random;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.renderer.RenderFruitPresser;
import growthcraft.cellar.tileentity.TileEntityFruitPresser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFruitPresser extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockFruitPresser()
	{
		super(Material.piston);
		this.isBlockContainer = true;
		this.setHardness(0.5F);
		this.setStepSound(soundTypePiston);
		this.setBlockName("grc.fruitPresser");
		this.setCreativeTab(null);
		this.setBlockBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.9375F, 0.8125F);
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		return false;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		int m = world.getBlockMetadata(x,  y - 1, z);
		world.setBlockMetadataWithNotify(x, y, z, m, 2);

		if (!world.isRemote)
		{
			this.updatePressState(world, x, y, z);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		int m = world.getBlockMetadata(x,  y - 1, z);
		world.setBlockMetadataWithNotify(x, y, z, m, 2);

		if (!world.isRemote)
		{
			this.updatePressState(world, x, y, z);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			world.func_147480_a(x, y, z, true);
		}

		if (!world.isRemote)
		{
			this.updatePressState(world, x, y, z);
		}
	}

	private void updatePressState(World world, int x, int y, int z)
	{
		int     meta = world.getBlockMetadata(x, y, z);
		boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);

		if (flag && (meta == 0 || meta == 1))
		{
			world.setBlockMetadataWithNotify(x, y, z, meta | 2, 2);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "tile.piston.out", 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
		}
		else if (!flag && (meta == 2 || meta == 3))
		{
			world.setBlockMetadataWithNotify(x, y, z, meta & 1, 2);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "tile.piston.in", 0.5F, world.rand.nextFloat() * 0.15F + 0.6F);
		}

		world.markBlockForUpdate(x, y, z);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return world.getBlock(x, y - 1, z) == GrowthCraftCellar.fruitPress;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 0 || meta == 2)
		{
			return (side == side.EAST || side == side.WEST);
		}
		else if (meta == 1 || meta == 3)
		{
			return (side == side.NORTH || side == side.SOUTH);
		}

		return isNormalCube(world, x, y, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(GrowthCraftCellar.fruitPress);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityFruitPresser();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random random, int par3)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[4];

		tex[0] = reg.registerIcon("grccellar:fruitpresser_0");
		tex[1] = reg.registerIcon("grccellar:fruitpresser_1");
		tex[2] = reg.registerIcon("grccellar:fruitpresser_2");
		tex[3] = reg.registerIcon("planks_oak");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return side == 0 ? tex[0] : (side == 1 ? tex[1] : tex[2]);
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderFruitPresser.id;
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
}
