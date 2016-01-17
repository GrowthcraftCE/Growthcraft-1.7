package growthcraft.cellar.common.block;

import growthcraft.cellar.client.render.RenderFermentJar;
import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.CellarGuiType;
import growthcraft.core.util.BoundUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockFermentJar extends BlockCellarContainer
{
	public BlockFermentJar()
	{
		super(Material.glass);
		setStepSound(soundTypeGlass);
		setBlockName("grc.fermentJar");
		setBlockTextureName("grccellar:ferment_jar_glass");
		setCreativeTab(GrowthCraftCellar.tab);
		setTileEntityType(TileEntityFermentJar.class);
		setGuiType(CellarGuiType.FERMENT_JAR);

		final float[] bounds = BoundUtils.newCubeToBounds(2f, 0f, 2f, 12f, 16f, 12f);
		BoundUtils.scaleBounds(bounds, 1 / 16.0f, bounds);
		setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
	}

	@Override
	public int getRenderType()
	{
		return RenderFermentJar.RENDER_ID;
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

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}
}
