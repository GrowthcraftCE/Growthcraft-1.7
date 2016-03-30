/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.api.core.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.annotation.Nonnull;

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
public abstract class AbstractUserJSONConfig implements ILoggable, IModule
{
	public static final String DEFAULT_ENCODING = "UTF-8";

	protected ILogger logger = NullLogger.INSTANCE;
	protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private File targetConfigFile;
	private File targetDefaultConfigFile;

	@Override
	public void setLogger(@Nonnull ILogger l)
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
	protected abstract void loadFromBuffer(BufferedReader buff) throws IllegalStateException;

	public AbstractUserJSONConfig setConfigFile(File dir, String filename)
	{
		this.targetConfigFile = new File(dir, filename);
		this.targetDefaultConfigFile = new File(dir, filename + ".default");
		logger.debug("Config file `%s` was set for `%s`", targetConfigFile, this);
		logger.debug("DEFAULT Config file `%s` was set for `%s`", targetDefaultConfigFile, this);
		return this;
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

	private void readUserConfigFile(File file)
	{
		BufferedReader buffer = null;
		UnicodeInputStreamReader input = null;

		try
		{
			logger.debug("Loading json-config %s", file);

			prepareUserConfig();
			if (file.canRead())
			{
				input = new UnicodeInputStreamReader(new FileInputStream(file), DEFAULT_ENCODING);
				buffer = new BufferedReader(input);
				loadFromBuffer(buffer);
			}
			else
			{
				logger.error("Could not read config file %s", file);
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

	public void loadUserConfig()
	{
		writeDefaultConfigTo(targetDefaultConfigFile);
		try
		{
			readUserConfigFile(targetConfigFile);
			return;
		}
		catch (IllegalStateException e)
		{
			logger.error("JSON Config '%s' contains errors", targetConfigFile);
			e.printStackTrace();
		}
		logger.warn("Falling back to default config file");
		readUserConfigFile(targetDefaultConfigFile);
	}

	@Override
	public void preInit() {}

	@Override
	public void register() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}
}
