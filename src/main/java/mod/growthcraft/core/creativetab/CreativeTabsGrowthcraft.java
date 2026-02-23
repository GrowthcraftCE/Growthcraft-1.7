package growthcraft.core.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class CreativeTabsGrowthcraft extends CreativeTabs
{
	public CreativeTabsGrowthcraft(String name)
	{
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(Blocks.sapling);
	}
}
