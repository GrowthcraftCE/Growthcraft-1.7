package growthcraft.nether.common.block;

import growthcraft.nether.GrowthCraftNether;
import growthcraft.core.block.Materials;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockNetherMaliceLog extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockNetherMaliceLog()
	{
		super(Materials.fireproofWood);
		setHardness(2.0F);
		setBlockName("grcnether.netherMaliceLog");
		setBlockTextureName("grcnether:log_malice");
		setHarvestLevel("axe", 0);
		setCreativeTab(GrowthCraftNether.tab);
	}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		icons = new IIcon[2];
		icons[0] = reg.registerIcon(getTextureName() + "_top");
		icons[1] = reg.registerIcon(getTextureName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 0 || side == 1)
		{
			return icons[0];
		}
		return icons[1];
	}
}
