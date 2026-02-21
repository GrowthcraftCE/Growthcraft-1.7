package growthcraft.bees.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import growthcraft.bees.GrowthCraftBees;

public class CreativeTabsGrowthcraftBees extends CreativeTabs {

    public CreativeTabsGrowthcraftBees(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return GrowthCraftBees.items.bee.getItem();
    }
}
