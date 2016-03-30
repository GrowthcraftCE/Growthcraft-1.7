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
package growthcraft.api.fishtrap.user;

import java.io.BufferedReader;

import growthcraft.api.core.user.AbstractUserJSONConfig;
import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.FishTrapRegistry;

public class UserFishTrapConfig extends AbstractUserJSONConfig
{
	private final UserFishTrapEntries defaultEntries = new UserFishTrapEntries();
	private UserFishTrapEntries entries;

	public void addDefault(String group, FishTrapEntry entry)
	{
		defaultEntries.data.add(new UserFishTrapEntry(group, entry));
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader buff) throws IllegalStateException
	{
		this.entries = gson.fromJson(buff, UserFishTrapEntries.class);
	}

	private void addFishTrapEntry(UserFishTrapEntry entry)
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

		for (FishTrapEntry obj : entry.getFishTrapEntries())
		{
			switch (entry.group)
			{
				case "treasure":
					FishTrapRegistry.instance().addTrapTreasure(obj);
					break;
				case "fish":
					FishTrapRegistry.instance().addTrapFish(obj);
					break;
				case "junk":
					FishTrapRegistry.instance().addTrapJunk(obj);
					break;
				default:
					logger.error("There is no '%s' group for entry {%s}", entry.group, entry);
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
				logger.info("Adding %d user fish trap entries.", entries.data.size());
				for (UserFishTrapEntry entry : entries.data) addFishTrapEntry(entry);
			}
			else
			{
				logger.error("Config contains invalid data.");
			}
		}
	}
}
