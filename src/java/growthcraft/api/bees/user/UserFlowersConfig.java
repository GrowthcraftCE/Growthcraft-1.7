/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.api.bees.user;

import java.io.BufferedReader;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.api.bees.ForcedFlowerBlockEntry;
import growthcraft.api.core.util.ItemKey;
import growthcraft.api.core.util.JsonConfigDef;

import net.minecraft.block.Block;

public class UserFlowersConfig extends JsonConfigDef
{
	private final UserFlowersEntries defaultEntries = new UserFlowersEntries();
	private UserFlowersEntries entries;

	public UserFlowerEntry addDefault(UserFlowerEntry entry)
	{
		defaultEntries.data.add(entry);
		return entry;
	}

	public UserFlowerEntry addDefault(Block flower, int meta)
	{
		return addDefault(new UserFlowerEntry(flower, meta));
	}

	public UserFlowerEntry addDefault(Block flower)
	{
		return addDefault(flower, ItemKey.WILDCARD_VALUE);
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader buff)
	{
		this.entries = gson.fromJson(buff, UserFlowersEntries.class);
	}

	private void addFlowerEntry(UserFlowerEntry entry)
	{
		if (entry == null)
		{
			logger.error("Invalid Entry");
			return;
		}

		if (entry.block == null || entry.block.isInvalid())
		{
			logger.error("Invalid block for entry {%s}", entry);
			return;
		}

		switch (entry.entry_type)
		{
			case "generic":
				BeesRegistry.instance().addFlower(entry.block.getBlock(), entry.block.meta);
				break;
			case "forced":
				BeesRegistry.instance().addFlower(new ForcedFlowerBlockEntry(entry.block.getBlock(), entry.block.meta));
				break;
			default:
				logger.error("Invalid entry_type '%s' for entry {%s}", entry.entry_type, entry);
		}

	}

	@Override
	public void postInit()
	{
		if (entries != null)
		{
			if (entries.data != null)
			{
				logger.info("Adding %d user flower entries.", entries.data.size());
				for (UserFlowerEntry entry : entries.data) addFlowerEntry(entry);
			}
			else
			{
				logger.error("Config contains invalid data.");
			}
		}
	}
}
