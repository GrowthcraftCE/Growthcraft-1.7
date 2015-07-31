package growthcraft.grapes;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemFood;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGrapes extends ItemFood
{
	public ItemGrapes()
	{
		super(2, 0.3F, false);
		this.setUnlocalizedName("grc.grapes");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcgrapes:grapes");
	}
}
