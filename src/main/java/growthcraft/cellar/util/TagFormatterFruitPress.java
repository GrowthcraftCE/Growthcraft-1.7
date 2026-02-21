package growthcraft.cellar.util;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.util.ITagFormatter;
import growthcraft.core.util.TagFormatterItem;

public class TagFormatterFruitPress implements ITagFormatter {

    public static final TagFormatterFruitPress INSTANCE = new TagFormatterFruitPress();

    public List<String> format(List<String> list, NBTTagCompound tag) {
        list.add(
            EnumChatFormatting.GRAY + GrcI18n.translate(
                "grc.cellar.fruit_press.itemslot.item",
                TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_press"))));
        list.add(
            EnumChatFormatting.GRAY + GrcI18n.translate(
                "grc.cellar.fruit_press.itemslot.residue",
                TagFormatterItem.INSTANCE.formatItem(tag.getCompoundTag("item_residue"))));
        return list;
    }
}
