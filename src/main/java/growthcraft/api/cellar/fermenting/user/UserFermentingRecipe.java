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
package growthcraft.api.cellar.fermenting.user;

import javax.annotation.Nonnull;

import growthcraft.api.core.schema.MultiFluidStackSchema;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.schema.ItemKeySchema;

public class UserFermentingRecipe implements ICommentable
{
	public String comment = "";
	public ItemKeySchema item;
	public MultiFluidStackSchema input_fluid;
	public FluidStackSchema output_fluid;
	public int time;

	public UserFermentingRecipe(@Nonnull ItemKeySchema itemSchema, @Nonnull MultiFluidStackSchema inp_fluid, @Nonnull FluidStackSchema out_fluid, int t)
	{
		this.item = itemSchema;
		this.input_fluid = inp_fluid;
		this.output_fluid = out_fluid;
		this.time = t;
	}

	public UserFermentingRecipe() {}

	@Override
	public String toString()
	{
		return String.format("UserFermentingRecipe((`%s` + `%s`) / %d = `%s`)", item, input_fluid, time, output_fluid);
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
