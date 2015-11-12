package growthcraft.bees.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockBeeBoxThaumcraft extends BlockBeeBox
{
	public BlockBeeBoxThaumcraft()
	{
		super();
		this.setBlockName("grc.BeeBox.Thaumcraft");
	}

	@Override
	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(block, 1, EnumBeeBoxThaumcraft.GREATWOOD.meta));
		list.add(new ItemStack(block, 1, EnumBeeBoxThaumcraft.SILVERWOOD.meta));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[2 * 4];
		registerBeeBoxIcons(reg, "/thaumcraft/greatwood/", EnumBeeBoxThaumcraft.GREATWOOD.meta);
		registerBeeBoxIcons(reg, "/thaumcraft/silverwood/", EnumBeeBoxThaumcraft.SILVERWOOD.meta);
	}
}
