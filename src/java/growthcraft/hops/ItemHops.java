package growthcraft.hops;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHops extends Item 
{
	public ItemHops() 
	{
		super();
		this.setUnlocalizedName("grc.hops");
		this.setCreativeTab(GrowthCraftCore.tab);
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
