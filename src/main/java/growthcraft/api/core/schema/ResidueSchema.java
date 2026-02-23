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
package growthcraft.api.core.schema;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.common.Residue;

import net.minecraft.item.ItemStack;

public class ResidueSchema extends ItemKeySchema
{
	public float pomace;

	public ResidueSchema(@Nonnull Residue res)
	{
		super(res.residueItem);
		this.pomace = res.pomaceRate;
	}

	public ResidueSchema()
	{
		super();
		this.pomace = 1.0f;
	}

	public Residue asResidue()
	{
		final ItemStack itemStack = asStack();
		if (itemStack == null) return null;
		return new Residue(itemStack, pomace);
	}

	@Override
	public String toString()
	{
		return String.format("%s~(pomace: %.4f)", super.toString(), pomace);
	}
}
