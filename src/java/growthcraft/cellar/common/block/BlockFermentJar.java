package growthcraft.cellar.common.block;

import growthcraft.cellar.client.render.RenderFermentJar;

import net.minecraft.block.material.Material;
import growthcraft.cellar.GrowthCraftCellar;

public class BlockFermentJar extends BlockCellarContainer implements ICellarFluidHandler
{
	public BlockFermentJar()
	{
		super(Material.wood);
		setBlockName("grc.fermentJar");
		setCreativeTab(GrowthCraftCellar.tab);
	}

	@Override
	public int getRenderType()
	{
		return RenderFermentJar.RENDER_ID;
	}
}
