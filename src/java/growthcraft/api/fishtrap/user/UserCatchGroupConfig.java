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
import java.util.Map;

import growthcraft.api.core.user.AbstractUserJSONConfig;
import growthcraft.api.fishtrap.FishTrapRegistry;

public class UserCatchGroupConfig extends AbstractUserJSONConfig
{
	private final UserCatchGroupEntries defaultEntries = new UserCatchGroupEntries();
	private UserCatchGroupEntries entries;

	public void addDefault(String group, int weight, String comment)
	{
		final UserCatchGroupEntry entry = new UserCatchGroupEntry(weight);
		entry.setComment(comment);
		defaultEntries.data.put(group, entry);
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader buff) throws IllegalStateException
	{
		this.entries = gson.fromJson(buff, UserCatchGroupEntries.class);
	}

	private void addCatchGroupEntry(String name, UserCatchGroupEntry entry)
	{
		if (entry == null)
		{
			logger.error("Invalid Entry");
			return;
		}

		if (entry.weight <= 0)
		{
			logger.error("Invalid weight for entry {%s}", entry);
			return;
		}

		logger.debug("Adding Catch Group %s", name);
		FishTrapRegistry.instance().addCatchGroup(name, entry.weight);
	}

	@Override
	public void postInit()
	{
		if (entries != null)
		{
			if (entries.data != null)
			{
				logger.debug("Adding %d user catch groups.", entries.data.size());
				for (Map.Entry<String, UserCatchGroupEntry> pair : entries.data.entrySet())
				{
					addCatchGroupEntry(pair.getKey(), pair.getValue());
				}
			}
			else
			{
				logger.error("Config contains invalid data.");
			}
		}
	}
}
