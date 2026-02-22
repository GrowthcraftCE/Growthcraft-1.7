package growthcraft.bamboo.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import growthcraft.bamboo.GrowthCraftBamboo;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockBambooFenceGate extends BlockFenceGate {

    public BlockBambooFenceGate() {
        super();
        setStepSound(soundTypeWood);
        setHardness(2.0F);
        setResistance(5.0F);
        setBlockName("grc.bambooFenceGate");
        setCreativeTab(GrowthCraftBamboo.creativeTab);
    }

    /************
     * TEXTURES
     ************/
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int _meta) {
        return GrowthCraftBamboo.blocks.bambooBlock.getBlock()
                .getBlockTextureFromSide(side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
    }
}
