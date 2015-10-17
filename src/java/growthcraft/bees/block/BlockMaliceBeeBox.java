package growthcraft.bees.block;

import java.util.List;

import growthcraft.bees.tileentity.TileEntityBeeBox;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockMaliceBeeBox extends BlockBeeBox
{
	public BlockMaliceBeeBox()
	{
		super();
		this.setBlockName("grc.maliceBeeBox");
	}

	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(block, 1, 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		icons = new IIcon[4];

		icons[0] = reg.registerIcon("grcbees:beebox_bottom_malice");
		icons[1] = reg.registerIcon("grcbees:beebox_top_malice");
		icons[2] = reg.registerIcon("grcbees:beebox_side_malice");
		icons[3] = reg.registerIcon("grcbees:beebox_side_malice_honey");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
		if (side == 0)
		{
			return this.icons[0];
		}
		else if (side == 1)
		{
			return this.icons[1];
		}
		else
		{
			if (te != null && te.isHoneyEnough())
			{
				return this.icons[3];
			}
		}
		return this.icons[2];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		final int offset = MathHelper.clamp_int(meta, 0, 5) * 4;
		if (side == 0)
		{
			return this.icons[0];
		}
		else if (side == 1)
		{
			return this.icons[1];
		}
		return this.icons[2];
	}
}
