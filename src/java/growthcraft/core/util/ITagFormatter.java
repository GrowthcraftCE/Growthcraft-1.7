package growthcraft.core.util;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Allows formatter interfaces, look at TagFormatterFluidHandler for an example
 */
public interface ITagFormatter
{
	public List<String> format(List<String> list, NBTTagCompound tag);
}
