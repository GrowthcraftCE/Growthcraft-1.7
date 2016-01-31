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
package growthcraft.api.cellar.pressing.user;

import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.schema.ResidueSchema;

public class UserPressingRecipe implements ICommentable
{
	public String comment = "";
	public ItemKeySchema item;
	public FluidStackSchema fluid;
	public ResidueSchema residue;
	public int time;

	public UserPressingRecipe(ItemKeySchema itm, FluidStackSchema fl, int tm, ResidueSchema res)
	{
		this.item = itm;
		this.fluid = fl;
		this.time = tm;
		this.residue = res;
	}

	public UserPressingRecipe() {}

	@Override
	public String toString()
	{
		return String.format("UserPressingRecipe(`%s` / %d = `%s` & `%s`)", item, time, fluid, residue);
	}

	@Override
	public void setComment(String comm)
	{
		this.comment = comm;
	}

	@Override
	public String getComment()
	{
		return comment;
	}
}
