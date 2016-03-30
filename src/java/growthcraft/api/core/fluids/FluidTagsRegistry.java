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
package growthcraft.api.core.fluids;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import growthcraft.api.core.common.DuplicateRegistrationError;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

public class FluidTagsRegistry implements IFluidTagsRegistry
{
	private ILogger logger = NullLogger.INSTANCE;
	private Map<String, FluidTag> nameToTag = new HashMap<String, FluidTag>();

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void registerTag(@Nonnull FluidTag tag)
	{
		if (nameToTag.containsKey(tag.getName()))
		{
			throw DuplicateRegistrationError.newFor(tag);
		}
		nameToTag.put(tag.getName(), tag);
	}

	@Override
	public FluidTag createTag(@Nonnull String name)
	{
		final FluidTag tag = new FluidTag(name);
		registerTag(tag);
		return tag;
	}

	@Override
	public Collection<String> getNames()
	{
		return nameToTag.keySet();
	}

	@Override
	public Collection<FluidTag> getTags()
	{
		return nameToTag.values();
	}

	@Override
	public FluidTag findTag(@Nonnull String name)
	{
		return nameToTag.get(name);
	}

	@Override
	public List<FluidTag> expandTagNames(@Nonnull List<String> tagNames)
	{
		final List<FluidTag> tags = new ArrayList<FluidTag>();
		for (String name : tagNames) tags.add(findTag(name));
		return tags;
	}
}
