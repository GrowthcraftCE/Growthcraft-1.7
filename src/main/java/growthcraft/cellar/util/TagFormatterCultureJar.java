package growthcraft.cellar.util;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.util.ITagFormatter;
import growthcraft.core.util.TagFormatterItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class TagFormatterCultureJar implements ITagFormatter {

    public static final TagFormatterCultureJar INSTANCE = new TagFormatterCultureJar();

    public List<String> format(List<String> list, NBTTagCompound tag) {
        list.add(
            EnumChatFormatting.GRAY + GrcI18n.translate(
                "grc.cellar.ferment_jar.itemslot.yeast",
                TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_yeast"))));
        return list;
    }
}
