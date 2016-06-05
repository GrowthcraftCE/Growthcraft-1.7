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
package growthcraft.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import growthcraft.api.core.util.TagParser;

import net.minecraftforge.common.config.Configuration;

public abstract class ConfigTypeHandler
{
	public static List<ConfigTypeHandler> handlers = new ArrayList<ConfigTypeHandler>();
	public abstract boolean canHandle(Field field);
	public abstract Object handle(Field field, Configuration config);

	public static class TagHandler extends ConfigTypeHandler
	{
		@Override
		public boolean canHandle(Field field)
		{
			final Class typeClass = field.getType();
			return typeClass.isArray() &&
				TagParser.Tag.class.equals(typeClass.getComponentType());
		}

		@Override
		public Object handle(Field field, Configuration config)
		{
			final ConfigBase.ConfigOption opt = field.getAnnotation(ConfigBase.ConfigOption.class);
			final String value = opt.def();
			final TagParser parser = opt.opt().equals("scsv") ?
				TagParser.scsv :
				(opt.opt().equals("cosv") ? TagParser.cosv : TagParser.csv);

			return parser.parse(
				config.get(
					opt.catergory(),
					opt.name(),
					value,
					opt.desc() + ConfigBase.DEFAULT_STR + value
				).getString()
			);
		}
	}

	public static class TagTableHandler extends ConfigTypeHandler
	{
		@Override
		public boolean canHandle(Field field)
		{
			final Class typeClass = field.getType();
			if (typeClass.isArray())
			{
				final Class componentClass = typeClass.getComponentType();
				return componentClass.isArray() &&
					TagParser.Tag.class.equals(componentClass.getComponentType());
			}
			return false;
		}

		@Override
		public Object handle(Field field, Configuration config)
		{
			final ConfigBase.ConfigOption opt = field.getAnnotation(ConfigBase.ConfigOption.class);
			final String value = opt.def();
			final String[] rows = TagParser.scsv.parseToArray(
				config.get(
					opt.catergory(),
					opt.name(),
					value,
					opt.desc() + ConfigBase.DEFAULT_STR + value
				).getString()
			);

			final TagParser.Tag[][] table = new TagParser.Tag[rows.length][];
			int i = 0;
			for (String row : rows)
			{
				table[i] = TagParser.csv.parse(row);
				i++;
			}
			return table;
		}
	}

	static
	{
		handlers.add(new TagHandler());
		handlers.add(new TagTableHandler());
	}
}
