package growthcraft.cellar.common.item;

import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.item.Item;

public class ItemYeast extends Item
{
	public ItemYeast()
	{
		super();
		setTextureName("grccellar:powder_yeast");
		setUnlocalizedName("grc.yeast");
		setCreativeTab(GrowthCraftCellar.tab);
	}
}
