package growthcraft.cellar.utils;

import java.util.List;

import growthcraft.core.utils.ConstID;
import growthcraft.core.utils.ITagFormatter;
import growthcraft.core.utils.TagFormatterItem;
import growthcraft.core.utils.UnitFormatter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TagFormatterFermentBarrel implements ITagFormatter
{
	public final static TagFormatterFermentBarrel INSTANCE = new TagFormatterFermentBarrel();

	private void addFermentProgress(List<String> list, NBTTagCompound tag)
	{
		final int time = tag.getInteger("time");
		final int timeMax = tag.getInteger("time_max");
		final float prog = (float)time / (timeMax == 0 ? 1 : timeMax);
		list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.cellar.fermentBarrel.progress_prefix") + " " +
			EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted("grc.cellar.fermentBarrel.progress_format", (int)(prog * 100)));
	}

	private void addModifierLine(List<String> list, NBTTagCompound tag)
	{
		final NBTTagCompound modifierItem = tag.getCompoundTag("item_modifier");
		list.add(EnumChatFormatting.GRAY +
			StatCollector.translateToLocal("grc.cellar.fermentBarrel.itemslot.modifier") + " " +
			TagFormatterItem.INSTANCE.formatItem(modifierItem));
	}

	public List<String> format(List<String> list, NBTTagCompound tag)
	{
		final NBTTagCompound modifierItem = tag.getCompoundTag("item_modifier");
		final boolean hasModifierItem = modifierItem.getInteger("id") != ConstID.NO_ITEM;
		if (hasModifierItem) addModifierLine(list, tag);
		final int boozeID = tag.getInteger("booze_id");
		if (boozeID != ConstID.NO_FLUID)
		{
			final Fluid booze = FluidRegistry.getFluid(boozeID);
			if (booze != null)
			{
				if (hasModifierItem) addFermentProgress(list, tag);
			}
		}
		return list;
	}
}
