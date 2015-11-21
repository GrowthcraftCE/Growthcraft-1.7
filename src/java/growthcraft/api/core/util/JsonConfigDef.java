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
package growthcraft.api.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;

import net.minecraftforge.common.config.Configuration.UnicodeInputStreamReader;

/**
 * This is a base class for defining JSON config definitions, its purpose
 * is mostly to hide the dreaded File handling
 */
public abstract class JsonConfigDef implements ILoggable
{
	public static final String DEFAULT_ENCODING = "UTF-8";

	protected ILogger logger = NullLogger.INSTANCE;
	protected final Gson gson = new Gson();

	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	protected abstract String getDefault();

	public void saveDefault(File file)
	{
		try
		{
			if (file.getParentFile() != null)
			{
				file.getParentFile().mkdirs();
			}

			if (!file.createNewFile())
				return;

			if (file.canWrite())
			{
				try (FileWriter writer = new FileWriter(file))
				{
					writer.write(getDefault());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected abstract void loadFromBuffer(BufferedReader buff);

	public void load(File dir, String filename)
	{
		final File file = new File(dir, filename);
		BufferedReader buffer = null;
		UnicodeInputStreamReader input = null;
		try
		{
			if (!file.exists()) saveDefault(file);

			if (file.canRead())
			{
				input = new UnicodeInputStreamReader(new FileInputStream(file), DEFAULT_ENCODING);
				buffer = new BufferedReader(input);
				loadFromBuffer(buffer);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (buffer != null)
			{
				try
				{
					buffer.close();
				}
				catch (IOException e) {}
			}
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e) {}
			}
		}
	}
}
