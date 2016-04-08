package growthcraft.hops.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemHops extends GrcItemBase
{
	public ItemHops()
	{
		super();
		this.setUnlocalizedName("grc.hops");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grchops:hops");
	}
}
