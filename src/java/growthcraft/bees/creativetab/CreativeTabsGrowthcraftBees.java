package growthcraft.bees.creativetab;

import growthcraft.bees.GrowthCraftBees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabsGrowthcraftBees extends CreativeTabs
{
	public CreativeTabsGrowthcraftBees()
	{
		super("tabGrowthCraftBees");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return GrowthCraftBees.bee.getItem();
	}
}
