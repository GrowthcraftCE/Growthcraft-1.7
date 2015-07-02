package growthcraft.bamboo;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBambooCoal extends Item 
{
	public ItemBambooCoal()
	{
		super();
		this.setUnlocalizedName("grc.bambooCoal");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcbamboo:coal");
	}
}
