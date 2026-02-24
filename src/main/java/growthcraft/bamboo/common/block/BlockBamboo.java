package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.block.GrcBlockBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockBamboo extends GrcBlockBase
{
	public BlockBamboo()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setBlockName("grc.bambooBlock");
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.blockIcon = reg.registerIcon("grcbamboo:block");
	}
}
