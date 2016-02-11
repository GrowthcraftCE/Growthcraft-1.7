package growthcraft.cellar.util;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.util.ConstID;
import growthcraft.api.core.util.ITagFormatter;
import growthcraft.core.util.TagFormatterItem;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TagFormatterFermentBarrel implements ITagFormatter
{
	public static final TagFormatterFermentBarrel INSTANCE = new TagFormatterFermentBarrel();

	private void addFermentProgress(List<String> list, NBTTagCompound tag)
	{
		final int time = tag.getInteger("time");
		final int timeMax = tag.getInteger("time_max");
		final float prog = (float)time / (timeMax == 0 ? 1 : timeMax);
		list.add(EnumChatFormatting.GRAY + GrcI18n.translate("grc.cellar.fermentBarrel.progress_prefix") + " " +
			EnumChatFormatting.WHITE + GrcI18n.translate("grc.cellar.fermentBarrel.progress_format", (int)(prog * 100)));
	}

	private void addModifierLine(List<String> list, NBTTagCompound tag)
	{
		final NBTTagCompound modifierItem = tag.getCompoundTag("item_modifier");
		list.add(EnumChatFormatting.GRAY +
			GrcI18n.translate(
				"grc.cellar.fermentBarrel.itemslot.modifier",
				TagFormatterItem.INSTANCE.formatItem(modifierItem)
			)
		);
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
