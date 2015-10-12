package growthcraft.core.utils;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * Tag Formatter for item NBT data
 */
public class TagFormatterItem implements ITagFormatter
{
	public static final TagFormatterItem INSTANCE = new TagFormatterItem();

	public String formatItem(NBTTagCompound tag)
	{
		final int id = tag.getInteger("id");
		if (id == ConstID.NO_ITEM)
		{
			return UnitFormatter.noItem();
		}
		else
		{
			final ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
			if (stack != null)
			{
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
