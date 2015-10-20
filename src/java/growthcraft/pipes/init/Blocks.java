package growthcraft.pipes.init;

import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.pipes.common.tileentity.TileEntityPipeBase;
import growthcraft.pipes.common.block.BlockPipeBase;
import growthcraft.pipes.common.item.ItemBlockPipeBase;
import growthcraft.pipes.util.PipeType;

import cpw.mods.fml.common.registry.GameRegistry;

public class Blocks
{
	public BlockTypeDefinition<BlockPipeBase> pipeBase;
	public BlockTypeDefinition<BlockPipeBase> pipeVacuum;

	public void preInit()
	{
		pipeBase = new BlockTypeDefinition<BlockPipeBase>(new BlockPipeBase(PipeType.BASE));
		pipeBase.getBlock().setBlockName("grc.pipeBase");
		pipeVacuum = new BlockTypeDefinition<BlockPipeBase>(new BlockPipeBase(PipeType.VACUUM));
		pipeVacuum.getBlock().setBlockName("grc.pipeVacuum");

		GameRegistry.registerBlock(pipeBase.getBlock(), ItemBlockPipeBase.class, "grc.pipeBase");
		GameRegistry.registerBlock(pipeVacuum.getBlock(), ItemBlockPipeBase.class, "grc.pipeVacuum");

		GameRegistry.registerTileEntity(TileEntityPipeBase.class, "grc.tileentity.pipeBase");
	}
}
