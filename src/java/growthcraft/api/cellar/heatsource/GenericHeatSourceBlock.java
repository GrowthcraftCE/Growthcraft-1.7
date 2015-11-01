package growthcraft.api.cellar.heatsource;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class GenericHeatSourceBlock implements IHeatSourceBlock
{
	private Block block;
	private float heat;

	public GenericHeatSourceBlock(Block blk, float ht)
	{
		this.block = blk;
		this.heat = ht;
	}

	@Override
	public float getHeat(World world, int x, int y, int z)
	{
		return heat;
	}
}
