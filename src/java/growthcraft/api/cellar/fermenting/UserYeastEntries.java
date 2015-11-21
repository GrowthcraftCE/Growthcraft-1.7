/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.api.cellar.fermenting;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.util.BiomeUtils;
import growthcraft.api.core.util.JsonConfigDef;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;

/**
 * This allows users to define new yeast entries and map them to a biome
 * for generation in the Ferment Jar.
 */
public class UserYeastEntries extends JsonConfigDef
{
	public static class UserYeastEntry
	{
		public ItemKeySchema item;
		public List<String> biomes;

		public UserYeastEntry(@Nonnull ItemKeySchema i, @Nonnull List<String> b)
		{
			this.item = i;
			this.biomes = b;
		}

		public UserYeastEntry() {}

		public String toString()
		{
			return "" + item + " [" + biomes + "]";
		}
	}

	private UserYeastEntry[] entries;

	@Override
	protected String getDefault()
	{
		final List<UserYeastEntry> defaultEntries = new ArrayList<UserYeastEntry>();
		final UserYeastEntry brewers = new UserYeastEntry(new ItemKeySchema("Growthcraft|Cellar", "grc.yeast", 1, 0), new ArrayList<String>());
		final UserYeastEntry lager = new UserYeastEntry(new ItemKeySchema("Growthcraft|Cellar", "grc.yeast", 1, 1), new ArrayList<String>());
		final UserYeastEntry ethereal = new UserYeastEntry(new ItemKeySchema("Growthcraft|Cellar", "grc.yeast", 1, 3), new ArrayList<String>());
		for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.values())
		{
			switch (biomeType)
			{
				case COLD:
					lager.biomes.add(biomeType.name());
					break;
				case MAGICAL:
				case MUSHROOM:
					ethereal.biomes.add(biomeType.name());
					break;
				default:
					brewers.biomes.add(biomeType.name());
			}
		}
		defaultEntries.add(brewers);
		defaultEntries.add(lager);
		defaultEntries.add(ethereal);
		final UserYeastEntry[] ary = defaultEntries.toArray(new UserYeastEntry[defaultEntries.size()]);
		return gson.toJson(ary, UserYeastEntry[].class);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader)
	{
		this.entries = gson.fromJson(reader, UserYeastEntry[].class);
	}

	private void addYeastEntry(UserYeastEntry entry)
	{
		if (entry == null)
		{
			logger.error("Yeast entry was invalid.");
			return;
		}

		if (entry.item == null)
		{
			logger.error("Yeast item was invalid %s", entry);
			return;
		}

		if (entry.biomes == null)
		{
			logger.error("Yeast biomes was invalid %s", entry);
			return;
		}

		final ItemStack itemstack = entry.item.asStack();

		final FermentingRegistry reg = CellarRegistry.instance().fermenting();
		for (String biome : entry.biomes)
		{
			try
			{
				final BiomeDictionary.Type biomeType = BiomeUtils.fetchBiomeType(biome);
				reg.addYeastToBiomeType(itemstack, biomeType);
				logger.info("Added yeast %s to biome %s", itemstack, biome);
			}
			catch (BiomeUtils.BiomeTypeNotFound ex)
			{
				logger.error("A biome type %s for entry %s could not be found.", biome, entry);
			}
		}
	}

	public void register()
	{
		if (entries != null)
		{
			logger.info("Adding %d yeast entries.", entries.length);
			for (UserYeastEntry entry : entries) addYeastEntry(entry);
		}
	}
}
