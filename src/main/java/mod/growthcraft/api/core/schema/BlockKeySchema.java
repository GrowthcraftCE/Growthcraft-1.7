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

import growthcraft.api.core.util.BlockKey;

import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BlockKeySchema implements ICommentable, IValidatable
{
	public String comment;
	public String mod_id;
	public String name;
	public int meta;

	public BlockKeySchema(String pmod_id, String pname, int pmeta)
	{
		this.mod_id = pmod_id;
		this.name = pname;
		this.meta = pmeta;
		this.comment = "";
	}

	public BlockKeySchema(Block block, int pmeta)
	{
		final UniqueIdentifier uuid = GameRegistry.findUniqueIdentifierFor(block);
		this.mod_id = uuid.modId;
		this.name = uuid.name;
		this.meta = pmeta;
		this.comment = block.getLocalizedName();
	}

	public BlockKeySchema(BlockKey blockKey)
	{
		this(blockKey.block, blockKey.meta);
	}

	public BlockKeySchema()
	{
		this.comment = "";
	}

	public Block getBlock()
	{
		if (mod_id != null && name != null)
		{
			return GameRegistry.findBlock(mod_id, name);
		}
		return null;
	}

	public BlockKey toBlockKey()
	{
		final Block block = getBlock();
		if (block != null)
		{
			return new BlockKey(block, meta);
		}
		return null;
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

	@Override
	public boolean isValid()
	{
		return getBlock() != null;
	}

	@Override
	public boolean isInvalid()
	{
		return !isValid();
	}

	@Override
	public String toString()
	{
		return String.format("Schema<BlockKey>(mod_id: '%s', name: '%s', meta: %d)", mod_id, name, meta);
	}
}
