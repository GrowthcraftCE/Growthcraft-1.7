package growthcraft.bamboo.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

import growthcraft.bamboo.GrowthCraftBamboo;

public class ItemBambooSlab extends ItemSlab {

    public ItemBambooSlab(Block block) {
        super(
            block,
            GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock(),
            GrowthCraftBamboo.blocks.bambooDoubleSlab.getBlock(),
            GrowthCraftBamboo.blocks.bambooDoubleSlab.getBlock() == block);
        setUnlocalizedName("grc.bambooSlab");
    }
}
