package growthcraft.nether.common.block;

import java.util.Random;

import javax.annotation.Nonnull;

import growthcraft.core.block.BlockPaddyBase;
import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockNetherPaddy extends BlockPaddyBase
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	private final int paddyFieldMax = GrowthCraftNether.getConfig().paddyFieldMax;

	public BlockNetherPaddy()
	{
		super(Material.sand);
		this.setHardness(0.5F);
		this.setStepSound(soundTypeSand);
		this.setBlockName("grcnether.netherPaddyField");
		this.setCreativeTab(null);
	}

	/**
	 * Returns the fluid block used to fill this paddy
	 *
	 * @return fluid block
	 */
	@Override
	@Nonnull public Block getFluidBlock()
	{
		return Blocks.lava;
	}

	@Override
	@Nonnull public Fluid getFillingFluid()
	{
		return FluidRegistry.LAVA;
	}

	@Override
	public int getMaxPaddyMeta(IBlockAccess world, int x, int y, int z)
	{
		return paddyFieldMax;
	}

	@Override
	public boolean isBelowFillingFluid(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlock(x, y + 1, z).getMaterial() == Material.lava;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(Blocks.soul_sand);
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return Item.getItemFromBlock(Blocks.soul_sand);
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
		icons = new IIcon[3];

		icons[0] = reg.registerIcon("soul_sand");
		icons[1] = reg.registerIcon("grcnether:soul_sand_paddy_dry");
		icons[2] = reg.registerIcon("grcnether:soul_sand_paddy_wet");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			if (meta == 0)
			{
				return icons[1];
			}
			else
			{
				return icons[2];
			}
		}
		return icons[0];
	}
}
