package growthcraft.core.integration.witchery;

import cpw.mods.fml.common.registry.GameRegistry;
import growthcraft.core.integration.EBXL.EBXLPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Locale;

public enum EnumWitcheryWoodType {
    ROWAN,
    ALDER,
    HAWTHORN;

    public static final EnumWitcheryWoodType[] VALUES = values();
    public final String name;
    public final int meta;

    EnumWitcheryWoodType() {
        this.name = name().toLowerCase(Locale.ENGLISH);
        this.meta = ordinal();
    }

    public ItemStack asPlanksItemStack(int size) {
        final Block block = GameRegistry.findBlock(WitcheryPlatform.MOD_ID, "planks");
        if (block != null) {
            final ItemStack result = new ItemStack(block, size, meta);
            return result;
        }
        return null;
    }

    public ItemStack asPlanksItemStack() {
        return asPlanksItemStack(1);
    }
}
