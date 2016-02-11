package growthcraft.cellar.util;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.util.ITagFormatter;
import growthcraft.core.util.TagFormatterItem;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class TagFormatterCultureJar implements ITagFormatter
{
	public static final TagFormatterCultureJar INSTANCE = new TagFormatterCultureJar();

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		list.add(EnumChatFormatting.GRAY +
			GrcI18n.translate(
				"grc.cellar.fermentJar.itemslot.yeast",
				TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_yeast"))
			)
		);
		return list;
	}
}
