package growthcraft.cellar.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import growthcraft.cellar.GrowthCraftCellar;

public class CreativeTabsCellar extends CreativeTabs {

    public CreativeTabsCellar(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return GrowthCraftCellar.blocks.fermentBarrel.getItem();
    }
}
