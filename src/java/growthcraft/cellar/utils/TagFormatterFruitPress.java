package growthcraft.cellar.utils;

import java.util.List;
import growthcraft.core.utils.ITagFormatter;

import growthcraft.core.utils.TagFormatterItem;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class TagFormatterFruitPress implements ITagFormatter
{
	public static final TagFormatterFruitPress INSTANCE = new TagFormatterFruitPress();

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		list.add(EnumChatFormatting.GRAY +
			StatCollector.translateToLocal("grc.cellar.fruitPress.itemslot.item") + " " +
			TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_press")));
		list.add(EnumChatFormatting.GRAY +
			StatCollector.translateToLocal("grc.cellar.fruitPress.itemslot.residue") + " " +
			TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_residue")));
		return list;
	}
}
