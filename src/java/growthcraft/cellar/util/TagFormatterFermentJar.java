package growthcraft.cellar.util;

import java.util.List;
import growthcraft.core.util.ITagFormatter;

import growthcraft.core.util.TagFormatterItem;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class TagFormatterFermentJar implements ITagFormatter
{
	public static final TagFormatterFermentJar INSTANCE = new TagFormatterFermentJar();

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		list.add(EnumChatFormatting.GRAY +
			StatCollector.translateToLocalFormatted(
				"grc.cellar.fermentJar.itemslot.yeast",
				TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_yeast"))
			)
		);
		return list;
	}
}
