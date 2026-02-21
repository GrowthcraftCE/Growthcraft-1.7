package growthcraft.core.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabsGrowthcraft extends CreativeTabs {

    public CreativeTabsGrowthcraft(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return Item.getItemFromBlock(Blocks.sapling);
    }
}
