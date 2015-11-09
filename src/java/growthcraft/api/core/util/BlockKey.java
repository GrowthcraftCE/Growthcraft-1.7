package growthcraft.api.core.util;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;

/**
 * As the name implies, this class is used in place of a List for Block keys
 */
public class BlockKey extends HashKey
{
	public final Block block;
	public final int meta;

	public BlockKey(@Nonnull Block bblock, int imeta)
	{
		super();
		this.block = bblock;
		this.meta = imeta;
		generateHashCode();
	}

	public void generateHashCode()
	{
		this.hash = block.hashCode();
		this.hash = 31 * hash + meta;
	}
}
