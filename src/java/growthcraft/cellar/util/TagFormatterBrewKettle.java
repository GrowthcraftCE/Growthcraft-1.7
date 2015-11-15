package growthcraft.cellar.util;

import java.util.List;

import growthcraft.core.util.ITagFormatter;
import growthcraft.core.util.TagFormatterItem;
import growthcraft.core.util.UnitFormatter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class TagFormatterBrewKettle implements ITagFormatter
{
	public static final TagFormatterBrewKettle INSTANCE = new TagFormatterBrewKettle();

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		list.add(EnumChatFormatting.GRAY +
			StatCollector.translateToLocal("grc.cellar.brewKettle.brewing_prefix") + " " +
			EnumChatFormatting.WHITE + UnitFormatter.booleanAsValue(tag.getBoolean("can_brew")));

		list.add(EnumChatFormatting.GRAY +
			StatCollector.translateToLocalFormatted(
				"grc.cellar.brewKettle.itemslot.item",
				TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_brew"))
			)
		);

		list.add(EnumChatFormatting.GRAY +
			StatCollector.translateToLocalFormatted(
				"grc.cellar.brewKettle.itemslot.residue",
				TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_residue"))
			)
		);
		return list;
	}
}
