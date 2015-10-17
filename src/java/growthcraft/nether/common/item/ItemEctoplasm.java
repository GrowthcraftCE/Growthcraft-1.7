package growthcraft.nether.common.item;

import growthcraft.nether.GrowthCraftNether;

import net.minecraft.item.Item;

public class ItemEctoplasm extends Item
{
	public ItemEctoplasm()
	{
		super();
		setUnlocalizedName("grcnether.ectoplasm");
		setTextureName("grcnether:ectoplasm");
		setCreativeTab(GrowthCraftNether.tab);
	}
}
