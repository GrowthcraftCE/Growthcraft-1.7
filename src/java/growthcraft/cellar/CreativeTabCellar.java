package growthcraft.cellar;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabCellar extends CreativeTabs
{
	private ItemStack icon;

	public CreativeTabCellar(String index)
	{
		super(index);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(GrowthCraftCellar.fermentBarrel);
	}
}
