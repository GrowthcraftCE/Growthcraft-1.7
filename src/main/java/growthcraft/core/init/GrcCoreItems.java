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
package growthcraft.core.init;

import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleItems;
import growthcraft.core.common.item.ItemBottleSalt;
import growthcraft.core.common.item.ItemBucketSalt;
import growthcraft.core.common.item.ItemCrowbar;
import growthcraft.core.common.item.ItemRope;
import growthcraft.core.common.item.ItemSalt;

import net.minecraftforge.oredict.OreDictionary;

public class GrcCoreItems extends GrcModuleItems
{
	public ItemDefinition rope;
	public ItemDefinition salt;
	public ItemDefinition saltBucket;
	public ItemDefinition saltBottle;
	public ItemDefinition crowbar;

	@Override
	public void preInit()
	{
		this.rope = newDefinition(new ItemRope());
		this.salt = newDefinition(new ItemSalt());
		this.saltBottle = newDefinition(new ItemBottleSalt());
		this.saltBucket = newDefinition(new ItemBucketSalt());
		this.crowbar = newDefinition(new ItemCrowbar());
	}

	@Override
	public void register()
	{
		rope.register("grc.rope");
		salt.register("grccore.salt");
		saltBottle.register("grccore.saltBottle");
		saltBucket.register("grccore.saltBucket");
		crowbar.register("grccore.crowbar");
	}

	@Override
	public void init()
	{
		OreDictionary.registerOre("materialRope", rope.getItem());
		OreDictionary.registerOre("materialSalt", salt.getItem());
		OreDictionary.registerOre("foodSalt", salt.getItem());
		OreDictionary.registerOre("dustSalt", salt.getItem());
		OreDictionary.registerOre("lumpSalt", salt.getItem());
		OreDictionary.registerOre("bottleSalt", saltBottle.getItem());
		OreDictionary.registerOre("bucketSalt", saltBucket.getItem());
	}
}
