package growthcraft.cellar.common.block;

import java.util.Random;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.client.particle.EntityFXDropParticle;
import growthcraft.core.common.block.Materials;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidBooze extends BlockFluidClassic
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	private int color;

	public BlockFluidBooze(Fluid fluid)
	{
		super(fluid, GrowthCraftCellar.getConfig().boozeIsWater ? Material.water : Materials.booze);
		setBlockName(fluid.getUnlocalizedName());
		setCreativeTab(GrowthCraftCellar.tab);
		refreshColor();
	}

	public void refreshColor()
	{
		setColor(getFluid().getColor());
	}

	public BlockFluidBooze setColor(int kolor)
	{
		this.color = kolor;
		// http://stackoverflow.com/questions/596216/formula-to-determine-brightness-of-rgb-color
		final float lum = 0.2126f * (((color >> 16) & 0xFF) / 255.0f) +
			0.7152f * (((color >> 8) & 0xFF) / 255.0f) +
			0.0722f * ((color & 0xFF) / 255.0f);
		setLightOpacity((int)((1f - lum) * 15));
		return this;
	}

	public int getColor()
	{
		return this.color;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return side != 0 && side != 1 ? this.icons[1] : this.icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.icons = new IIcon[2];
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

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		super.randomDisplayTick(world, x, y, z, rand);

		if (rand.nextInt(10) == 0 &&
			World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) &&
			!world.getBlock(x, y - 2, z).getMaterial().blocksMovement())
		{
			final double px = x + rand.nextFloat();
			final double py = y - 1.05D;
			final double pz = z + rand.nextFloat();
			final float particleRed = ((color >> 16) & 0xFF) / 255.0f;
			final float particleGreen = ((color >> 8) & 0xFF) / 255.0f;
			final float particleBlue = (color & 0xFF) / 255.0f;
			final EntityFX fx = new EntityFXDropParticle(world, px, py, pz, particleRed, particleGreen, particleBlue);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
		}
	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
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
}
