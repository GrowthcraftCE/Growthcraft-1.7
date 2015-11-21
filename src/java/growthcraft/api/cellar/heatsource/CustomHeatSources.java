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
package growthcraft.api.cellar.heatsource;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import growthcraft.api.core.util.ItemKey;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.util.JsonConfigDef;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * Allows you to load Custom heat source defintions from a JSON file
 */
public class CustomHeatSources extends JsonConfigDef
{
	public static class HeatSourceEntry
	{
		public String mod_id;
		public String block_name;
		public Map<Integer, Float> states;

		public HeatSourceEntry() {}

		public HeatSourceEntry(String m, String b, Map<Integer, Float> s)
		{
			this.mod_id = m;
			this.block_name = b;
			this.states = s;
		}

		public static Map<Integer, Float> wildcardHeat(float h)
		{
			final Map<Integer, Float> map = new HashMap<Integer, Float>();
			map.put(ItemKey.WILDCARD_VALUE, h);
			return map;
		}
	}

	private static final HeatSourceEntry[] DEFAULT_ENTRIES = {
		new HeatSourceEntry("minecraft", "fire", HeatSourceEntry.wildcardHeat(1.0f)),
		new HeatSourceEntry("minecraft", "flowing_lava", HeatSourceEntry.wildcardHeat(0.7f)),
		new HeatSourceEntry("minecraft", "lava", HeatSourceEntry.wildcardHeat(0.7f))
	};

	private HeatSourceEntry[] entries;

	@Override
	protected String getDefault()
	{
		return gson.toJson(DEFAULT_ENTRIES, HeatSourceEntry[].class);
	}

	@Override
	protected void loadFromBuffer(BufferedReader buff)
	{
		entries = gson.fromJson(buff, HeatSourceEntry[].class);
	}

	private void addHeatSource(HeatSourceEntry heatsource)
	{
		final Block block = GameRegistry.findBlock(heatsource.mod_id, heatsource.block_name);
		if (block != null)
		{
			if (heatsource.states == null || heatsource.states.size() == 0)
			{
				logger.warn("Block contains invalid states, we will assume a wildcard, but you should probably set this. mod_id=%s block=%s", heatsource.mod_id, heatsource.block_name);
				CellarRegistry.instance().heatSource().addHeatSource(block, ItemKey.WILDCARD_VALUE);
			}
			else
			{
				for (Map.Entry<Integer, Float> entry : heatsource.states.entrySet())
				{
					int key = entry.getKey();
					if (key < 0) key = ItemKey.WILDCARD_VALUE;
					CellarRegistry.instance().heatSource().addHeatSource(block, key, entry.getValue());
				}
			}
		}
		else
		{
			logger.error("Block could not be found, and will not be added as heat source. mod_id=%s block=%s", heatsource.mod_id, heatsource.block_name);
		}
	}

	public void register()
	{
		if (entries != null)
		{
			logger.info("Registering %d heat sources.", entries.length);
			for (HeatSourceEntry heatsource : entries) addHeatSource(heatsource);
		}
	}
}
