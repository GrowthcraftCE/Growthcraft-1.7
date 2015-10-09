package growthcraft.core.utils;

import java.util.List;

import growthcraft.core.utils.ConstID;
import growthcraft.core.utils.ITagFormatter;
import growthcraft.core.utils.UnitFormatter;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * Tag Formatter for item NBT data
 */
public class TagFormatterItem implements ITagFormatter
{
	public final static TagFormatterItem INSTANCE = new TagFormatterItem();

	public String formatItem(NBTTagCompound tag)
	{
		final int id = tag.getInteger("id");
		final int damage = tag.getInteger("damage");
		final int size = tag.getInteger("size");
		if (id == ConstID.NO_ITEM)
		{
			return UnitFormatter.noItem();
		}
		else
		{
			final Item item = Item.getItemById(id);
			if (item != null)
			{
				final ItemStack stack = new ItemStack(item, size, damage);
				return EnumChatFormatting.WHITE +
					StatCollector.translateToLocalFormatted("grc.format.itemslot.item",
						StatCollector.translateToLocal(stack.getUnlocalizedName() + ".name"),
						stack.stackSize);
			}
			else
			{
				return UnitFormatter.invalidItem();
			}
		}
	}

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		list.add(formatItem(tag));
		return list;
	}
}
