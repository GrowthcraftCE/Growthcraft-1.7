package growthcraft.cellar.common.block;

import java.util.Random;

import growthcraft.cellar.client.render.RenderFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.event.BarrelDrainedEvent;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.CellarGuiType;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockFermentBarrel extends BlockCellarContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockFermentBarrel()
	{
		super(Material.wood);
		setTileEntityType(TileEntityFermentBarrel.class);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setBlockName("grc.fermentBarrel");
		setBlockTextureName("grccellar:ferment_barrel");
		setCreativeTab(GrowthCraftCellar.tab);
		setGuiType(CellarGuiType.FERMENT_BARREL);
	}

	@Override
	public boolean isRotatable(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	@Override
	protected boolean playerDrainTank(World world, int x, int y, int z, IFluidHandler tank, ItemStack held, EntityPlayer player)
	{
		final FluidStack available = Utils.playerDrainTank(world, x, y, z, tank, held, player);
		if (available != null && available.amount > 0)
		{
			GrowthCraftCellar.CELLAR_BUS.post(new BarrelDrainedEvent(player, world, x, y, z, available));
			return true;
		}
		return false;
	}

	private void setDefaultDirection(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			final Block southBlock = world.getBlock(x, y, z - 1);
			final Block northBlock = world.getBlock(x, y, z + 1);
			final Block westBlock = world.getBlock(x - 1, y, z);
			final Block eastBlock = world.getBlock(x + 1, y, z);
			byte meta = 3;

			if (southBlock.func_149730_j() && !northBlock.func_149730_j())
			{
				meta = 3;
			}

			if (northBlock.func_149730_j() && !southBlock.func_149730_j())
			{
				meta = 2;
			}

			if (westBlock.func_149730_j() && !eastBlock.func_149730_j())
			{
				meta = 5;
			}

			if (eastBlock.func_149730_j() && !westBlock.func_149730_j())
			{
				meta = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		setDefaultDirection(world, x, y, z);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		final int meta = BlockPistonBase.determineOrientation(world, x, y, z, entity);
		world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_AND_SYNC);

		if (stack.hasDisplayName())
		{
			final TileEntityFermentBarrel te = getTileEntity(world, x, y, z);
			te.setGuiDisplayName(stack.getDisplayName());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[4];
		final String basename = getTextureName();
		icons[0] = reg.registerIcon(String.format("%s/minecraft/oak/side", basename));
		icons[1] = reg.registerIcon(String.format("%s/minecraft/oak/1", basename));
		icons[2] = reg.registerIcon(String.format("%s/minecraft/oak/2", basename));
		icons[3] = reg.registerIcon(String.format("%s/minecraft/oak/3", basename));
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconByIndex(int index)
	{
		return icons[index];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta == 0 || meta == 1)
		{
			return side == 0 || side == 1 ? icons[1] : icons[0];
		}
		else if (meta == 2 || meta == 3)
		{
			return side == 2 || side == 3 ? icons[1] : icons[0];
		}
		else if (meta == 4 || meta == 5)
		{
			return side == 4 || side == 5 ? icons[1] : icons[0];
		}
		return icons[0];
	}

	@Override
	public int getRenderType()
	{
		return RenderFermentBarrel.RENDER_ID;
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
		final TileEntityFermentBarrel te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			return te.getFermentProgressScaled(15);
		}
		return 0;
	}
}
