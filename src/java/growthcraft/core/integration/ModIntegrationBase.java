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
package growthcraft.core.integration;

import growthcraft.core.common.IModule;

import cpw.mods.fml.common.Loader;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Base class for integrating other mods with Growthcraft, this idea was
 * taken from AE2.
 */
public abstract class ModIntegrationBase implements IModule
{
	public final String parentModID;
	public final String modID;
	private boolean modLoaded;

	public ModIntegrationBase(String parentMod, String integratingMod)
	{
		this.parentModID = parentMod;
		this.modID = integratingMod;
	}

	public boolean modIsLoaded()
	{
		return modLoaded;
	}

	protected void doPreInit()
	{

	}

	public final void preInit()
	{
		this.modLoaded = Loader.isModLoaded(modID);
		if (modIsLoaded())
		{
			doPreInit();
		}
	}

	protected void doInit()
	{

	}

	public final void init()
	{
		if (modIsLoaded())
		{
			doInit();
		}
	}

	protected void doRegister()
	{

	}

	public final void register()
	{
		if (modIsLoaded())
		{
			doRegister();
		}
	}

	protected void doPostInit()
	{

	}

	public final void postInit()
	{
		if (modIsLoaded())
		{
			doPostInit();
		}

		// This method is kept around for reasons, you should use the
		// IModule "do" methods instead
		tryToIntegrate();
	}

	/**
	 * Implement this method
	 */
	protected void integrate()
	{

	}

	public void tryToIntegrate()
	{
		if (modIsLoaded())
		{
			FMLLog.log(parentModID, Level.INFO, "Attemping to integrate with %s.", modID);
			try
			{
				integrate();
				FMLLog.log(parentModID, Level.INFO, "Successfully integrated with %s.", modID);
			}
			catch (Exception e)
			{
				FMLLog.log(parentModID, Level.WARN, "%s integration failed.", modID);
			}
		}
		else
		{
			FMLLog.log(parentModID, Level.INFO, "%s not found; No integration made.", modID);
		}
	}
}
