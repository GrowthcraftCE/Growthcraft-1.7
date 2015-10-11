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

import cpw.mods.fml.common.Loader;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Base class for integrating other mods with Growthcraft, this idea was
 * taken from AE2.
 */
public abstract class ModIntegrationBase
{
	public final String parentModID;
	public final String modID;

	public ModIntegrationBase(String parentMod, String integratingMod)
	{
		this.parentModID = parentMod;
		this.modID = integratingMod;
	}

	/**
	 * Implement this method
	 */
	abstract protected void integrate();

	public void init()
	{
		if (Loader.isModLoaded(modID))
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