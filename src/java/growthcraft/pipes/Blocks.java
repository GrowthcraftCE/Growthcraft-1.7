package growthcraft.pipes;

import growthcraft.pipes.tileentity.TileEntityPipeBase;
import growthcraft.pipes.block.BlockPipeBase;
import growthcraft.pipes.utils.PipeType;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;

public class Blocks
{
	public BlockPipeBase pipeBase;
	public BlockPipeBase pipeVacuum;

	public void preInit()
	{
		pipeBase = new BlockPipeBase(PipeType.BASE);
		pipeVacuum = new BlockPipeBase(PipeType.VACUUM);

		GameRegistry.registerBlock(pipeBase, "grc.pipeBase");
		GameRegistry.registerBlock(pipeVacuum, "grc.pipeVacuum");
		GameRegistry.registerTileEntity(TileEntityPipeBase.class, "grc.tileentity.pipeBase");
	}
}
