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
package growthcraft.api.cellar.heatsource.user;

import java.io.BufferedReader;
import java.util.Map;

import growthcraft.api.core.item.ItemKey;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.user.AbstractUserJSONConfig;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * Provides users with the ability to set blocks as heat sources for Growthcraft
 * blocks
 */
public class UserHeatSourcesConfig extends AbstractUserJSONConfig
{
	private final UserHeatSourceEntries defaultEntries = new UserHeatSourceEntries();
	private UserHeatSourceEntries entries;

	public UserHeatSourceEntry addDefault(String m, String b, Map<Integer, Float> s)
	{
		final UserHeatSourceEntry entry = new UserHeatSourceEntry(m, b, s);
		defaultEntries.data.add(entry);
		return entry;
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader buff) throws IllegalStateException
	{
		this.entries = gson.fromJson(buff, UserHeatSourceEntries.class);
	}

	private void addHeatSource(UserHeatSourceEntry entry)
	{
		final Block block = GameRegistry.findBlock(entry.mod_id, entry.block_name);
		if (block != null)
		{
			if (entry.states == null || entry.states.size() == 0)
			{
				logger.warn("Block contains invalid states, we will assume a wildcard, but you should probably set this. mod_id='%s' block='%s'", entry.mod_id, entry.block_name);
				CellarRegistry.instance().heatSource().addHeatSource(block, ItemKey.WILDCARD_VALUE);
			}
			else
			{
				for (Map.Entry<Integer, Float> pair : entry.states.entrySet())
				{
					int key = pair.getKey();
					if (key < 0) key = ItemKey.WILDCARD_VALUE;
					CellarRegistry.instance().heatSource().addHeatSource(block, key, pair.getValue());
				}
			}
		}
		else
		{
			logger.error("Block could not be found, and will not be added as heat source. mod_id='%s' block='%s'", entry.mod_id, entry.block_name);
		}
	}

	@Override
	public void postInit()
	{
		if (entries != null)
		{
			if (entries.data != null)
			{
				logger.info("Registering %d heat sources.", entries.data.size());
				for (UserHeatSourceEntry entry : entries.data) addHeatSource(entry);
			}
			else
			{
				logger.error("Config data is invalid");
			}
		}
	}
}
