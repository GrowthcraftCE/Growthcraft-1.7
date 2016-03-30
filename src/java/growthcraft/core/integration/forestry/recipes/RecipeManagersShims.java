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
package growthcraft.core.integration.forestry.recipes;

import growthcraft.api.core.util.ObjectUtils;

import forestry.api.recipes.ICarpenterManager;
import forestry.api.recipes.ICentrifugeManager;
import forestry.api.recipes.IFabricatorManager;
import forestry.api.recipes.IFermenterManager;
import forestry.api.recipes.IMoistenerManager;
import forestry.api.recipes.ISqueezerManager;
import forestry.api.recipes.IStillManager;
import forestry.api.recipes.RecipeManagers;

import cpw.mods.fml.common.Optional;

public class RecipeManagersShims
{
	private static RecipeManagersShims INSTANCE;
	public ICarpenterManager carpenterManager = ObjectUtils.<ICarpenterManager>maybe(RecipeManagers.carpenterManager, new CarpenterManagerShim());
	public ICentrifugeManager centrifugeManager = ObjectUtils.<ICentrifugeManager>maybe(RecipeManagers.centrifugeManager, new CentrifugeManagerShim());
	public IFabricatorManager fabricatorManager = ObjectUtils.<IFabricatorManager>maybe(RecipeManagers.fabricatorManager, new FabricatorManagerShim());
	public IFermenterManager fermenterManager = ObjectUtils.<IFermenterManager>maybe(RecipeManagers.fermenterManager, new FermenterManagerShim());
	public IMoistenerManager moistenerManager = ObjectUtils.<IMoistenerManager>maybe(RecipeManagers.moistenerManager, new MoistenerManagerShim());
	public ISqueezerManager squeezerManager = ObjectUtils.<ISqueezerManager>maybe(RecipeManagers.squeezerManager, new SqueezerManagerShim());
	public IStillManager stillManager = ObjectUtils.<IStillManager>maybe(RecipeManagers.stillManager, new StillManagerShim());

	@Optional.Method(modid="ForestryAPI|recipes")
	public static RecipeManagersShims instance()
	{
		if (INSTANCE == null) INSTANCE = new RecipeManagersShims();
		return INSTANCE;
	}
}
