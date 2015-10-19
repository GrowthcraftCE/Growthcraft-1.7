package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemBee extends Item
{
	public ItemBee()
	{
		super();
		this.setUnlocalizedName("grc.bee");
		this.setCreativeTab(GrowthCraftBees.tab);
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
