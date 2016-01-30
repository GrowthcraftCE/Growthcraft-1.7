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
import com.google.gson.GsonBuilder;
import com.google.common.io.Files;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.module.IModule;

import net.minecraftforge.common.config.Configuration.UnicodeInputStreamReader;

/**
 * This is a base class for defining JSON config definitions, its purpose
 * is mostly to hide the dreaded File handling
 */
public abstract class JsonConfigDef implements ILoggable, IModule
{
	public static final String DEFAULT_ENCODING = "UTF-8";

	protected ILogger logger = NullLogger.INSTANCE;
	protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private File targetConfigFile;
	private File targetDefaultConfigFile;

	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	/**
	 * @return a default json configuration string
	 */
	protected abstract String getDefault();

	private void writeDefaultConfigTo(File file)
	{
		try
		{
			logger.info("Creating default json-config %s", file);
			if (file.getParentFile() != null)
				file.getParentFile().mkdirs();

			if (!file.exists())
			{
				if (!file.createNewFile())
				{
					logger.error("Could not create default config %s", file);
					return;
				}
			}

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

	/**
	 * Read the config file contents from the buffer
	 *
	 * @param buff - the buffer to read from
	 */
	protected abstract void loadFromBuffer(BufferedReader buff);

	public void setConfigFile(File dir, String filename)
	{
		this.targetConfigFile = new File(dir, filename);
		this.targetDefaultConfigFile = new File(dir, filename + ".default");
		logger.debug("Config file `%s` was set for `%s`", targetConfigFile, this);
		logger.debug("DEFAULT Config file `%s` was set for `%s`", targetDefaultConfigFile, this);
	}

	private void prepareUserConfig() throws IOException
	{
		if (!targetConfigFile.exists())
		{
			if (targetConfigFile.getParentFile() != null)
				targetConfigFile.getParentFile().mkdirs();

			if (!targetConfigFile.createNewFile())
			{
				logger.error("Could not create config file `%s`", targetConfigFile);
				return;
			}

			if (targetDefaultConfigFile.exists())
			{
				Files.copy(targetDefaultConfigFile, targetConfigFile);
			}
			else
			{
				logger.error("Could not copy default config file `%s` to `%s`", targetDefaultConfigFile, targetConfigFile);
			}
		}
	}

	private void loadUserConfig()
	{
		BufferedReader buffer = null;
		UnicodeInputStreamReader input = null;

		writeDefaultConfigTo(targetDefaultConfigFile);

		try
		{
			logger.debug("Loading json-config %s", targetConfigFile);

			prepareUserConfig();
			if (targetConfigFile.canRead())
			{
				input = new UnicodeInputStreamReader(new FileInputStream(targetConfigFile), DEFAULT_ENCODING);
				buffer = new BufferedReader(input);
				loadFromBuffer(buffer);
			}
			else
			{
				logger.error("Could not read config file %s", targetConfigFile);
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

	@Override
	public void preInit() {}

	@Override
	public void register() {}

	@Override
	public void init()
	{
		loadUserConfig();
	}

	@Override
	public void postInit() {}
}
