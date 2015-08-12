package growthcraft.bamboo.block;

import growthcraft.core.GrowthCraftCore;
import growthcraft.bamboo.GrowthCraftBamboo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockBambooFenceGate extends BlockFenceGate
{
	public BlockBambooFenceGate()
	{
		super();
		this.setStepSound(soundTypeWood);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setBlockName("grc.bambooFenceGate");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return GrowthCraftBamboo.bambooBlock.getBlockTextureFromSide(par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg){}
}
