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

/**
 * Used to parse config csvs
 * EG.
 *   S:biomes: "+FOREST,HILL"
 */
public class TagParser
{
	public static class Tag
	{
		public String value;
		// the tag SHOULD NOT be included
		public boolean exclude;
		// the tag MUST be present
		public boolean must;

		public Tag(String val)
		{
			this.value = val;
		}

		public Tag setExcludeFlag()
		{
			this.exclude = true;
			return this;
		}

		public Tag setMustFlag()
		{
			this.must = true;
			return this;
		}

		@Override
		public String toString()
		{
			if (exclude)
			{
				return "-" + value;
			}
			else if (must)
			{
				return "+" + value;
			}
			return value;
		}

		public static Tag parse(String value)
		{
			if (value.startsWith("-"))
			{
				return new Tag(value.substring(1)).setExcludeFlag();
			}
			else if (value.startsWith("+"))
			{
				return new Tag(value.substring(1)).setMustFlag();
			}
			// another way of saying "eh, it can be present"
			else if (value.startsWith("~"))
			{
				return new Tag(value.substring(1));
			}
			return new Tag(value);
		}
	}

	public static final TagParser csv = new TagParser();
	public static final TagParser scsv = new TagParser(";");
	public static final TagParser cosv = new TagParser(":");

	private String seperator;

	public TagParser(String sep)
	{
		this.seperator = sep;
	}

	public TagParser()
	{
		this(",");
	}

	/**
	 * Parses the tag strings as is
	 *
	 * @param value - value to split by seperator
	 * @return array of tag strings
	 */
	public String[] parseToArray(String value)
	{
		final String[] strings = value.split(seperator);
		for (int i = 0; i < strings.length; ++i)
		{
			strings[i] = strings[i].trim();
		}
		return strings;
	}

	/**
	 * @param value - a *sv string to split and convert to tags
	 * @return array of tags
	 */
	public Tag[] parse(String value)
	{
		final String[] strings = parseToArray(value);
		final Tag[] tags = new Tag[strings.length];
		for (int i = 0; i < strings.length; ++i)
		{
			tags[i] = Tag.parse(strings[i]);
		}
		return tags;
	}
}
