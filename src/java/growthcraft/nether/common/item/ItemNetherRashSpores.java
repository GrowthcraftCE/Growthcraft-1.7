package growthcraft.nether.common.item;

import growthcraft.nether.GrowthCraftNether;

import net.minecraft.item.Item;

public class ItemNetherRashSpores extends Item
{
	public ItemNetherRashSpores()
	{
		super();
		setUnlocalizedName("grcnether.netherRashSpores");
		setTextureName("grcnether:netherrash_spores");
		setCreativeTab(GrowthCraftNether.tab);
	}
}
