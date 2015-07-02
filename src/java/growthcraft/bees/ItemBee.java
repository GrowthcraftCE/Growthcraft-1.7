package growthcraft.bees;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBee extends Item 
{
	public ItemBee() 
	{
		super();
		this.setUnlocalizedName("grc.bee");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcbees:bee");
	}
}
