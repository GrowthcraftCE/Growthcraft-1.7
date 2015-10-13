package growthcraft.bees.utils;

import java.util.List;

import growthcraft.core.utils.TagFormatterItem;
import growthcraft.core.utils.ITagFormatter;
import growthcraft.core.utils.UnitFormatter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class TagFormatterBeeBox implements ITagFormatter
{
	public static final TagFormatterBeeBox INSTANCE = new TagFormatterBeeBox();

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.bees.bonus_prefix") + " " +
			EnumChatFormatting.WHITE + UnitFormatter.booleanAsValue(tag.getBoolean("has_bonus")));

		list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.bees.bees_prefix") + " " +
			TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("bee")));

		list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.bees.honey_prefix") + " " +
			UnitFormatter.fraction(
				"" + EnumChatFormatting.WHITE + tag.getInteger("honeycomb_count"),
				"" + EnumChatFormatting.YELLOW + tag.getInteger("honey_count"),
				"" + EnumChatFormatting.WHITE + tag.getInteger("honeycomb_max")
			)
		);

		return list;
	}
}
