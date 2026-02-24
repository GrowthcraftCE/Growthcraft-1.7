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
package growthcraft.api.bees.user;

import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.schema.BlockKeySchema;

import net.minecraft.block.Block;

public class UserFlowerEntry implements ICommentable
{
	public String comment = "";
	public String entry_type = "generic";
	public BlockKeySchema block;

	public UserFlowerEntry(String modId, String name, int meta)
	{
		this.block = new BlockKeySchema(modId, name, meta);
	}

	public UserFlowerEntry(Block pBlock, int meta)
	{
		this.block = new BlockKeySchema(pBlock, meta);
	}

	public UserFlowerEntry() {}

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

	public String getEntryType()
	{
		return entry_type;
	}

	public UserFlowerEntry setEntryType(String type)
	{
		this.entry_type = type;
		return this;
	}

	@Override
	public String toString()
	{
		return String.format("UserFlowerEntry(block: `%s`)", block);
	}
}
