package growthcraft.hops.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.item.GrcItemBase;

public class ItemHops extends GrcItemBase {

    public ItemHops() {
        super();
        this.setUnlocalizedName("grc.hops");
        this.setCreativeTab(GrowthCraftCore.creativeTab);
    }

    /************
     * TEXTURES
     ************/
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.itemIcon = reg.registerIcon("grchops:hops");
    }
}
