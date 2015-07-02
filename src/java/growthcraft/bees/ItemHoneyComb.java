package growthcraft.bees;

import growthcraft.core.GrowthCraftCore;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHoneyComb extends Item 
{
	private IIcon[] tex;

	public ItemHoneyComb() 
	{
		super();   
		this.setHasSubtypes(true); 
		this.setMaxDamage(0);
		this.setUnlocalizedName("grc.honeyComb");
		this.setCreativeTab(GrowthCraftCore.tab);
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
		int i = MathHelper.clamp_int(meta, 0, 1);
		return this.tex[i];
	}

	/************
	 * NAME
	 ************/
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 1);
		String s = i == 1 ? "full" : "empty";
		return super.getUnlocalizedName() + "." + s;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list)
	{
		for (int loop = 0; loop < 2; loop++)
		{
			list.add(new ItemStack(par1, 1, loop));
		}
	}
}
