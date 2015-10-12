package growthcraft.core.utils;

/**
 * Constant space for block flags, use these instead of magic numbers
 */
public final class BlockFlags
{
	// Cause the block to update
	public static final int BLOCK_UPDATE = 1;
	// Send change to clients
	public static final int SEND_TO_CLIENT = 2;
	// Stop the block from re-rendering
	public static final int SUPRESS_RENDER = 4;

	public static final int UPDATE_CLIENT = BLOCK_UPDATE | SEND_TO_CLIENT;
	public static final int ALL = UPDATE_CLIENT | SUPRESS_RENDER;

	private BlockFlags() {}
}
