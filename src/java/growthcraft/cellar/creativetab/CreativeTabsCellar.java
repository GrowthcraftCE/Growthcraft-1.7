package growthcraft.cellar.creativetab;

import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabsCellar extends CreativeTabs
{
	private ItemStack icon;

	public CreativeTabsCellar(String index)
	{
		super(index);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return GrowthCraftCellar.fermentBarrel.getItem();
	}
}
