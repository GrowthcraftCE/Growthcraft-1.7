package growthcraft.bees.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

/**
 * Deprecated in version 2.4.0
 * Remove this in version 2.5.x
 */
public class ItemHoneyComb extends Item
{
	private IIcon[] tex;

	public ItemHoneyComb()
	{
		super();
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName("grc.honeyComb");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	protected void writeModifierTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		list.add(StatCollector.translateToLocal("grc.bees.item.honeyComb.deprecated"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		writeModifierTooltip(stack, player, list, bool);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.tex = new IIcon[2];

		this.tex[0] = reg.registerIcon("grcbees:honeycomb.deprecated_0");
		this.tex[1] = reg.registerIcon("grcbees:honeycomb.deprecated_1");
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
	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List list)
	{
		for (int i = 0; i < 2; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
}
