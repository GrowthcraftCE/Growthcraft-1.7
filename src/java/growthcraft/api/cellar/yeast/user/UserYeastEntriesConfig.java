/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.api.cellar.yeast.user;

import java.io.BufferedReader;
import java.util.ArrayList;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.util.BiomeUtils;
import growthcraft.api.core.user.AbstractUserJSONConfig;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;

/**
 * This allows users to define new yeast entries and map them to a biome
 * for generation in the Ferment Jar.
 */
public class UserYeastEntriesConfig extends AbstractUserJSONConfig
{
	private final UserYeastEntries defaultEntries = new UserYeastEntries();
	private UserYeastEntries entries;

	@Override
	protected String getDefault()
	{
		final ItemKeySchema brewersYeast = new ItemKeySchema("Growthcraft|Cellar", "grc.yeast", 1, 0);
		brewersYeast.setComment("Brewers Yeast");

		final ItemKeySchema lagerYeast = new ItemKeySchema("Growthcraft|Cellar", "grc.yeast", 1, 1);
		lagerYeast.setComment("Lager Yeast");

		final ItemKeySchema etherealYeast = new ItemKeySchema("Growthcraft|Cellar", "grc.yeast", 1, 3);
		etherealYeast.setComment("Ethereal Yeast");

		final ItemKeySchema originYeast = new ItemKeySchema("Growthcraft|Cellar", "grc.yeast", 1, 4);
		etherealYeast.setComment("Origin Yeast");

		final UserYeastEntry brewers = new UserYeastEntry(brewersYeast, 1, new ArrayList<String>());
		brewers.setComment("Brewers yeast is the default yeast, which appears in all other biomes that are filled by the Lager or Ethereal");

		final UserYeastEntry lager = new UserYeastEntry(lagerYeast, 10, new ArrayList<String>());
		lager.setComment("Lager yeast is found in COLD biomes, think snow places!");

		final UserYeastEntry ethereal = new UserYeastEntry(etherealYeast, 10, new ArrayList<String>());
		ethereal.setComment("Ethereal yeast is found in MAGICAL biomes, because its special");

		final UserYeastEntry origin = new UserYeastEntry(originYeast, 10, new ArrayList<String>());
		origin.setComment("Origin yeast is found in MUSHROOM biomes.");

		for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.values())
		{
			final String biomeTypeName = biomeType.name();
			switch (biomeType)
			{
				case COLD:
					lager.biome_types.add(biomeTypeName);
					break;
				case MAGICAL:
					ethereal.biome_types.add(biomeTypeName);
					break;
				case MUSHROOM:
					origin.biome_types.add(biomeTypeName);
					break;
				default:
					brewers.biome_types.add(biomeTypeName);
			}
		}
		defaultEntries.data.add(brewers);
		defaultEntries.data.add(ethereal);
		defaultEntries.data.add(lager);
		defaultEntries.data.add(origin);
		defaultEntries.setComment("Default Yeast Config v1.1.0");
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader) throws IllegalStateException
	{
		this.entries = gson.fromJson(reader, UserYeastEntries.class);
	}

	private void addYeastEntry(UserYeastEntry entry)
	{
		if (entry == null)
		{
			logger.error("Yeast entry was invalid.");
			return;
		}

		if (entry.item == null || entry.item.isInvalid())
		{
			logger.error("Yeast item was invalid {%s}", entry);
			return;
		}

		for (ItemStack itemstack : entry.item.getItemStacks())
		{
			if (entry.biome_types != null)
			{
				for (String biome : entry.biome_types)
				{
					try
					{
						final BiomeDictionary.Type biomeType = BiomeUtils.fetchBiomeType(biome);
						CellarRegistry.instance().yeast().addYeastToBiomeType(itemstack, entry.weight, biomeType);
						logger.info("Added user yeast {%s} to biome type '%s'", itemstack, biome);
					}
					catch (BiomeUtils.BiomeTypeNotFound ex)
					{
						logger.error("A biome type '%s' for entry {%s} could not be found.", biome, entry);
					}
				}
			}

			if (entry.biome_names != null)
			{
				for (String biomeName : entry.biome_names)
				{
					CellarRegistry.instance().yeast().addYeastToBiomeByName(itemstack, entry.weight, biomeName);
					logger.info("Added user yeast {%s} to biome '%s'", itemstack, biomeName);
				}
			}
		}
	}

	@Override
	public void postInit()
	{
		if (entries != null)
		{
			if (entries.data != null)
			{
				logger.info("Adding %d yeast entries.", entries.data.size());
				for (UserYeastEntry entry : entries.data) addYeastEntry(entry);
			}
			else
			{
				logger.error("Invalid yeast entries data");
			}
		}
	}
}
