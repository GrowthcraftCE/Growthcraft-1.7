package growthcraft.bamboo;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBamboo extends Block 
{
	public BlockBamboo() 
	{
		super(Material.wood);
		this.setStepSound(soundTypeWood);
		this.setResistance(5.0F);
		this.setHardness(2.0F);
		this.setCreativeTab(GrowthCraftCore.tab);
		this.setBlockName("grc.bambooBlock");
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
