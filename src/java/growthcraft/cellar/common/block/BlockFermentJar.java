package growthcraft.cellar.common.block;

import growthcraft.cellar.client.render.RenderFermentJar;
import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockFermentJar extends BlockCellarContainer implements ICellarFluidHandler
{
	public BlockFermentJar()
	{
		super(Material.wood);
		setBlockName("grc.fermentJar");
		setCreativeTab(GrowthCraftCellar.tab);
		setTileEntityType(TileEntityFermentJar.class);
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
