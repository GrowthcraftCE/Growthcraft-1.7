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
package growthcraft.api.cellar.yeast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import javax.annotation.Nonnull;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.ItemKey;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class YeastRegistry implements IYeastRegistry
{
	private Set<ItemKey> yeastList = new HashSet<ItemKey>();
	private Map<BiomeDictionary.Type, List<ItemStack>> biomeToYeast = new HashMap<BiomeDictionary.Type, List<ItemStack>>();
	private Map<ItemKey, List<BiomeDictionary.Type>> yeastToBiome = new HashMap<ItemKey, List<BiomeDictionary.Type>>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	private ItemKey stackToKey(@Nonnull ItemStack stack)
	{
		return new ItemKey(stack);
	}

	@Override
	public void addYeast(@Nonnull ItemStack yeast)
	{
		yeastList.add(stackToKey(yeast));
	}

	@Override
	public boolean isYeast(ItemStack yeast)
	{
		if (yeast == null) return false;
		if (yeast.getItem() == null) return false;
		return yeastList.contains(stackToKey(yeast));
	}

	@Override
	public void addYeastToBiomeType(@Nonnull ItemStack yeast, BiomeDictionary.Type type)
	{
		addYeast(yeast);
		if (!biomeToYeast.containsKey(type))
		{
			biomeToYeast.put(type, new ArrayList<ItemStack>());
		}
		final ItemKey yeastKey = stackToKey(yeast);
		if (!yeastToBiome.containsKey(yeastKey))
		{
			yeastToBiome.put(yeastKey, new ArrayList<BiomeDictionary.Type>());
		}
		biomeToYeast.get(type).add(yeast);
		yeastToBiome.get(yeastKey).add(type);
	}

	@Override
	public List<ItemStack> getYeastListForBiomeType(BiomeDictionary.Type type)
	{
		return biomeToYeast.get(type);
	}

	@Override
	public List<BiomeDictionary.Type> getBiomeTypesForYeast(ItemStack yeast)
	{
		return yeastToBiome.get(stackToKey(yeast));
	}

	@Override
	public boolean canYeastFormInBiome(ItemStack yeast, BiomeGenBase biome)
	{
		if (yeast == null || biome == null) return false;

		final List<BiomeDictionary.Type> yeastBiomeList = getBiomeTypesForYeast(yeast);
		if (yeastBiomeList == null) return false;

		for (BiomeDictionary.Type t : BiomeDictionary.getTypesForBiome(biome))
		{
			if (yeastBiomeList.contains(t)) return true;
		}

		return false;
	}
}
