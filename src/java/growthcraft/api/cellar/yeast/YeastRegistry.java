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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.item.ItemKey;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class YeastRegistry implements IYeastRegistry
{
	private Set<ItemKey> yeastList = new HashSet<ItemKey>();
	private Map<BiomeDictionary.Type, Set<ItemStack>> biomeTypeToYeast = new HashMap<BiomeDictionary.Type, Set<ItemStack>>();
	private Map<String, Set<ItemStack>> biomeNameToYeast = new HashMap<String, Set<ItemStack>>();
	private Map<ItemKey, Set<BiomeDictionary.Type>> yeastToBiomeType = new HashMap<ItemKey, Set<BiomeDictionary.Type>>();
	private Map<ItemKey, Set<String>> yeastToBiomeName = new HashMap<ItemKey, Set<String>>();
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
	public boolean isYeast(@Nullable ItemStack yeast)
	{
		if (yeast == null) return false;
		if (yeast.getItem() == null) return false;
		return yeastList.contains(stackToKey(yeast));
	}

	@Override
	public void addYeastToBiomeType(@Nonnull ItemStack yeast, @Nonnull BiomeDictionary.Type type)
	{
		addYeast(yeast);
		if (!biomeTypeToYeast.containsKey(type))
		{
			logger.debug("Initializing biome type to yeast set for %s", type);
			biomeTypeToYeast.put(type, new HashSet<ItemStack>());
		}
		final ItemKey yeastKey = stackToKey(yeast);
		if (!yeastToBiomeType.containsKey(yeastKey))
		{
			logger.debug("Initializing yeast to biome type set for %s", yeast);
			yeastToBiomeType.put(yeastKey, new HashSet<BiomeDictionary.Type>());
		}
		biomeTypeToYeast.get(type).add(yeast);
		yeastToBiomeType.get(yeastKey).add(type);
	}

	@Override
	public void addYeastToBiomeByName(@Nonnull ItemStack yeast, @Nonnull String name)
	{
		addYeast(yeast);
		final ItemKey yeastKey = stackToKey(yeast);
		if (!yeastToBiomeName.containsKey(yeastKey))
		{
			logger.debug("Initializing yeast to biome name set for %s", yeast);
			yeastToBiomeName.put(yeastKey, new HashSet<String>());
		}
		yeastToBiomeName.get(yeastKey).add(name);
		if (!biomeNameToYeast.containsKey(name))
		{
			logger.debug("Initializing biome name to yeast set for %s", name);
			biomeNameToYeast.put(name, new HashSet<ItemStack>());
		}
		biomeNameToYeast.get(name).add(yeast);
	}

	@Override
	public Set<ItemStack> getYeastListForBiomeType(@Nonnull BiomeDictionary.Type type)
	{
		return biomeTypeToYeast.get(type);
	}

	@Override
	public Set<ItemStack> getYeastListForBiomeName(@Nonnull String type)
	{
		return biomeNameToYeast.get(type);
	}

	@Override
	public Set<String> getBiomeNamesForYeast(@Nullable ItemStack yeast)
	{
		if (yeast == null) return null;
		return yeastToBiomeName.get(stackToKey(yeast));
	}

	@Override
	public Set<BiomeDictionary.Type> getBiomeTypesForYeast(@Nullable ItemStack yeast)
	{
		if (yeast == null) return null;
		return yeastToBiomeType.get(stackToKey(yeast));
	}

	@Override
	public boolean canYeastFormInBiome(@Nullable ItemStack yeast, @Nullable BiomeGenBase biome)
	{
		if (yeast == null || biome == null) return false;

		final Set<String> biomeNames = getBiomeNamesForYeast(yeast);
		if (biomeNames != null)
		{
			if (biomeNames.contains(biome.biomeName)) return true;
		}

		final Set<BiomeDictionary.Type> yeastBiomeList = getBiomeTypesForYeast(yeast);
		if (yeastBiomeList != null)
		{
			for (BiomeDictionary.Type t : BiomeDictionary.getTypesForBiome(biome))
			{
				if (yeastBiomeList.contains(t)) return true;
			}
		}

		return false;
	}
}
