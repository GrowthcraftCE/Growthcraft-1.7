package growthcraft.cellar.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import growthcraft.cellar.GrowthCraftCellar;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

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
