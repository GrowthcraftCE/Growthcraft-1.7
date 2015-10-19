package growthcraft.nether.common.block;

import growthcraft.nether.GrowthCraftNether;
import growthcraft.core.util.BlockFlags;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockNetherSquash extends BlockDirectional
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public BlockNetherSquash()
	{
		super(Blocks.pumpkin.getMaterial());
		setTickRandomly(true);
		setBlockTextureName("grcnether:soulsquash");
		setBlockName("grcnether.netherSquash");
		setCreativeTab(GrowthCraftNether.tab);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
	{
		final int meta = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.SEND_TO_CLIENT);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		icons = new IIcon[3];

		icons[0] = reg.registerIcon(getTextureName() + "_side");
		icons[1] = reg.registerIcon(getTextureName() + "_top");
		icons[2] = reg.registerIcon(getTextureName() + "_face");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			return icons[1];
		}
		else if (side == 0)
		{
			return icons[0];
		}
		else
		{
			switch (meta)
			{
				case 0:
					if (side == 3) return icons[2];
					break;
				case 1:
					if (side == 4) return icons[2];
					break;
				case 2:
					if (side == 2) return icons[2];
					break;
				case 3:
					if (side == 5) return icons[2];
					break;
				default:
					break;
			}
		}
		return icons[0];
	}
}
