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
import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.user.AbstractUserJSONConfig;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UserBeesConfig extends AbstractUserJSONConfig
{
	private final UserBeesEntries defaultEntries = new UserBeesEntries();
	private UserBeesEntries entries;

	public UserBeeEntry addDefault(ItemStack bee)
	{
		final UserBeeEntry entry = new UserBeeEntry(bee);
		defaultEntries.data.add(entry);
		return entry;
	}

	public UserBeeEntry addDefault(Item bee)
	{
		return addDefault(new ItemStack(bee, ItemKey.WILDCARD_VALUE));
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader buff) throws IllegalStateException
	{
		this.entries = gson.fromJson(buff, UserBeesEntries.class);
	}

	private void addBeeEntry(UserBeeEntry entry)
	{
		if (entry == null)
		{
			logger.error("Invalid Entry");
			return;
		}

		if (entry.item == null || entry.item.isInvalid())
		{
			logger.error("Invalid item for entry {%s}", entry);
			return;
		}

		for (ItemStack stack : entry.item.getItemStacks())
		{
			BeesRegistry.instance().addBee(stack);
		}
	}

	@Override
	public void postInit()
	{
		if (entries != null)
		{
			if (entries.data != null)
			{
				logger.debug("Adding %d user bee entries.", entries.data.size());
				for (UserBeeEntry entry : entries.data) addBeeEntry(entry);
			}
			else
			{
				logger.error("Config contains invalid data.");
			}
		}
	}
}
