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
package growthcraft.api.core.vines.user;

import java.io.BufferedReader;
import javax.annotation.Nonnull;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.schema.BlockKeySchema;
import growthcraft.api.core.user.AbstractUserJSONConfig;
import growthcraft.api.core.util.BlockKey;

import net.minecraft.block.Block;

public class UserVinesConfig extends AbstractUserJSONConfig
{
	private final UserVineEntries defaultEntries = new UserVineEntries();
	private UserVineEntries entries;

	public UserVineEntry addDefault(@Nonnull UserVineEntry entry)
	{
		defaultEntries.data.add(entry);
		return entry;
	}

	public UserVineEntry addDefault(@Nonnull Block block, int meta)
	{
		return addDefault(new UserVineEntry(block, meta));
	}

	public UserVineEntry addDefault(@Nonnull Block block)
	{
		return addDefault(new UserVineEntry(block));
	}

	public UserVineEntry addDefault(@Nonnull BlockKeySchema block)
	{
		return addDefault(new UserVineEntry(block));
	}

	public UserVineEntry addDefault(@Nonnull BlockKey block)
	{
		return addDefault(new UserVineEntry(block));
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultEntries);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader) throws IllegalStateException
	{
		this.entries = gson.fromJson(reader, UserVineEntries.class);
	}

	private void addVineEntry(UserVineEntry entry)
	{
		if (entry == null)
		{
			logger.warn("UserVinesConfig entry is invalid!");
			return;
		}

		if (entry.block == null || entry.block.isInvalid())
		{
			logger.warn("UserVinesConfig entry has invalid block {%s}!", entry);
			return;
		}

		CoreRegistry.instance().vineDrops().addVineEntry(entry.block.getBlock(), entry.block.meta);
	}

	@Override
	public void postInit()
	{
		if (entries != null)
		{
			if (entries.data != null)
			{
				logger.info("Adding %d user vine entries.", entries.data.size());
				for (UserVineEntry entry : entries.data) addVineEntry(entry);
			}
			else
			{
				logger.warn("UserVinesConfig entries.data is invalid!");
			}
		}
		else
		{
			logger.warn("UserVinesConfig entries is invalid!");
		}
	}
}
