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
package growthcraft.api.core.util;

import net.minecraft.util.ResourceLocation;

/**
 * You will get frustrated having to import ResourceLocation and then remembering the domain you want the resource from.
 * NO MORE I SAY, I WANT MORE MAGIC IN MY LIFE.
 */
public class DomainResourceLocationFactory
{
	private final String domain;

	/**
	 * @param p_domain name of the domain that will be prefixed to the resource locations created by the factory
	 */
	public DomainResourceLocationFactory(String p_domain)
	{
		this.domain = p_domain;
	}

	/**
	 * @return domain the domain this factory serves up
	 */
	public String getDomainName()
	{
		return domain;
	}

	/**
	 * @param str str to append to the domain
	 * @return domained string
	 */
	public String join(String str)
	{
		return String.format("%s:%s", getDomainName(), str);
	}

	/**
	 * @return resource location
	 */
	public ResourceLocation create(String name)
	{
		return new ResourceLocation(getDomainName(), name);
	}
}
