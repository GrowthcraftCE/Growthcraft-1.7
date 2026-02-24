package growthcraft.cellar.util;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.util.ConstID;
import growthcraft.api.core.util.ITagFormatter;
import growthcraft.core.util.TagFormatterItem;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class TagFormatterFermentBarrel implements ITagFormatter
{
	public static final TagFormatterFermentBarrel INSTANCE = new TagFormatterFermentBarrel();

	private void addModifierLine(List<String> list, NBTTagCompound tag)
	{
		final NBTTagCompound modifierItem = tag.getCompoundTag("item_modifier");
		list.add(EnumChatFormatting.GRAY +
			GrcI18n.translate(
				"grc.cellar.ferment_barrel.itemslot.modifier",
				TagFormatterItem.INSTANCE.formatItem(modifierItem)
			)
		);
	}

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		final NBTTagCompound modifierItem = tag.getCompoundTag("item_modifier");
		final boolean hasModifierItem = modifierItem.getInteger("id") != ConstID.NO_ITEM;
		if (hasModifierItem) addModifierLine(list, tag);
		return list;
	}
}
