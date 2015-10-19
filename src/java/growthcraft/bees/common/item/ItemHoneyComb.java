package growthcraft.bees.common.item;

import java.util.List;

import growthcraft.bees.GrowthCraftBees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemHoneyComb extends Item
{
	private IIcon[] tex;

	public ItemHoneyComb()
	{
		super();
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName("grc.honeyComb");
		this.setCreativeTab(GrowthCraftBees.tab);
		this.setContainerItem(this);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.tex = new IIcon[2];

		this.tex[0] = reg.registerIcon("grcbees:honeycomb_0");
		this.tex[1] = reg.registerIcon("grcbees:honeycomb_1");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return this.tex[MathHelper.clamp_int(meta, 0, 1)];
	}

	/************
	 * NAME
	 ************/
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		final int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 1);
		final String s = i == 1 ? "full" : "empty";
		return super.getUnlocalizedName() + "." + s;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List list)
	{
		for (int i = 0; i < 2; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
}
