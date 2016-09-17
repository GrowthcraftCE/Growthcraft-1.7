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

import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.schema.ItemKeySchema;

import net.minecraft.item.ItemStack;

public class UserBaitEntry implements ICommentable
{
	public String comment = "";
	public float base_rate;
	public float multiplier;
	public ItemKeySchema item;

	/**
	 * @param g - item group, can be "treasure", "junk", or "fish"
	 * @param w - entry weight
	 * @param stack - item stack
	 * @param dam - damage variance, how much damage is applied to the item when fished up?
	 * @param enc - is the item enchanted?
	 */
	public UserBaitEntry(ItemStack stack, float base, float mul)
	{
		this.item = new ItemKeySchema(stack);
		this.base_rate = base;
		this.multiplier = mul;
	}

	public UserBaitEntry() {}

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
}
