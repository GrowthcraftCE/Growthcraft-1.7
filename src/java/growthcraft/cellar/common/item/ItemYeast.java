package growthcraft.cellar.common.item;

import java.util.List;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.YeastType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemYeast extends Item
{
	protected IIcon[] icons;

	public ItemYeast()
	{
		super();
		setTextureName("grccellar:yeast");
		setUnlocalizedName("grc.yeast");
		setCreativeTab(GrowthCraftCellar.tab);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (YeastType ytype : YeastType.values())
		{
			list.add(ytype.asStack());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.icons = new IIcon[YeastType.length];
		icons[YeastType.BAYANUS.ordinal()] = reg.registerIcon(getIconString() + "_bayanus");
		icons[YeastType.BREWERS.ordinal()] = reg.registerIcon(getIconString() + "_brewers");
		icons[YeastType.ETHEREAL.ordinal()] = reg.registerIcon(getIconString() + "_ethereal");
		icons[YeastType.LAGER.ordinal()] = reg.registerIcon(getIconString() + "_lager");
		icons[YeastType.ORIGIN.ordinal()] = reg.registerIcon(getIconString() + "_origin");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return icons[MathHelper.clamp_int(meta, 0, icons.length - 1)];
	}
}
