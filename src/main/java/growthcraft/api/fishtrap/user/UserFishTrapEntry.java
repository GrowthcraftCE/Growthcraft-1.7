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
package growthcraft.api.fishtrap.user;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.fishtrap.FishTrapEntry;

import net.minecraft.item.ItemStack;

public class UserFishTrapEntry implements ICommentable
{
	public String comment = "";
	public String group;
	public int weight;
	public ItemKeySchema item;
	public float damage_variance;
	public boolean enchanted;

	/**
	 * @param g - item group, can be "treasure", "junk", or "fish"
	 * @param w - entry weight
	 * @param stack - item stack
	 * @param dam - damage variance, how much damage is applied to the item when fished up?
	 * @param enc - is the item enchanted?
	 */
	public UserFishTrapEntry(String g, int w, ItemStack stack, float dam, boolean enc)
	{
		this.weight = w;
		this.group = g;
		this.item = new ItemKeySchema(stack);
		this.damage_variance = dam;
		this.enchanted = enc;
	}

	public UserFishTrapEntry(String g, FishTrapEntry entry)
	{
		this(g, entry.itemWeight, entry.getItemStack(), entry.getDamage(), entry.getEnchanted());
	}

	public UserFishTrapEntry() {}

	@Override
	public String getComment()
	{
		return comment;
	}

	@Override
	public void setComment(String com)
	{
		this.comment = com;
	}

	public List<FishTrapEntry> getFishTrapEntries()
	{
		final List<FishTrapEntry> result = new ArrayList<FishTrapEntry>();
		for (ItemStack stack : item.getItemStacks())
		{
			final FishTrapEntry entry = new FishTrapEntry(stack, weight);
			entry.setDamage(damage_variance);
			entry.setEnchantable(enchanted);
			result.add(entry);
		}
		return result;
	}
}
