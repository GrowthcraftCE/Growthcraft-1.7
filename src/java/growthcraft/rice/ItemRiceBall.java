package growthcraft.rice;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemFood;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRiceBall extends ItemFood
{
	public ItemRiceBall() 
	{
		super(5, 0.6F, false);
		this.setUnlocalizedName("grc.riceBall");
		this.setCreativeTab(GrowthCraftCore.tab);
	}	

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcrice:rice_ball");
	}
}
