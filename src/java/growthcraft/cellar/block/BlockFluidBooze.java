package growthcraft.cellar.block;

import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidBooze extends BlockFluidClassic
{
	@SideOnly(Side.CLIENT)
	protected final IIcon[] icons = new IIcon[2];
	private int color;

	public BlockFluidBooze(Fluid fluid, int kolor)
	{
		super(fluid, Material.water);
		setBlockName(fluid.getUnlocalizedName());
		setCreativeTab(GrowthCraftCellar.tab);
		this.color = kolor;
	}

	public BlockFluidBooze(Fluid fluid)
	{
		this(fluid, 0xFFFFFF);
	}

	public BlockFluidBooze setColor(int kolor)
	{
		this.color = kolor;
		return this;
	}

	public int getColor()
	{
		return this.color;
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		return side != 0 && side != 1 ? this.icons[1] : this.icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.icons[0] = iconRegister.registerIcon("grccellar:booze_still");
		this.icons[1] = iconRegister.registerIcon("grccellar:booze_flow");
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) return false;
		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) return false;
		return super.displaceIfPossible(world, x, y, z);
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		return color;
	}
}
