package growthcraft.nether.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class CreativeTabsGrowthcraftNether extends CreativeTabs
{
	public CreativeTabsGrowthcraftNether()
	{
		super("tabGrowthCraftNether");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(Blocks.soul_sand);
	}
}
