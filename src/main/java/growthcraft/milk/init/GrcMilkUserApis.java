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
package growthcraft.milk.init;

import java.io.File;

import growthcraft.api.core.module.IModule;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.user.AbstractUserJSONConfig;
import growthcraft.api.milk.churn.user.UserChurnRecipesConfig;

public class GrcMilkUserApis extends ModuleContainer
{
	public final UserChurnRecipesConfig churnRecipes;

	public GrcMilkUserApis()
	{
		super();
		this.churnRecipes = new UserChurnRecipesConfig();
		add(churnRecipes);
	}

	public void setConfigDirectory(File dir)
	{
		churnRecipes.setConfigFile(dir, "growthcraft/milk/churn_recipes.json");
	}

	public void loadConfigs()
	{
		for (IModule module : this)
		{
			if (module instanceof AbstractUserJSONConfig)
			{
				((AbstractUserJSONConfig)module).loadUserConfig();
			}
		}
	}
}
