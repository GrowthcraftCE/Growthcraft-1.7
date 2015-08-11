package growthcraft.core.utils;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class SchemaToVillage
{
	// Until I come up with a better name, DWI.
	public static interface IVillage {
		public void placeBlockAtCurrentPosition(World world, Block block, int meta, int x, int y, int z, StructureBoundingBox box);
	}

	public static class BlockEntry {
		public Block block;
		public int meta;

		public BlockEntry(Block block, int meta)
		{
			this.block = block;
			this.meta = meta;
		}
	}

	public static void drawSchema(IVillage village, World world, Random random, StructureBoundingBox box, String schema[][], Map<Character, BlockEntry> map)
	{
		for (int z = 0; z < schema.length; ++z)
		{
			String layer[] = schema[z];
			for (int y = 0; y < layer.length; ++y)
			{
				String row = layer[y];
				for (int x = 0; x < row.length(); ++x)
				{
					int meta = 0;
					Block block = Blocks.air;
					BlockEntry entry = map.get(row.charAt(x));
					if (entry != null) {
						block = entry.block;
						meta = entry.meta;
					}
					village.placeBlockAtCurrentPosition(world, block, meta, x, z, y, box);
				}
			}
		}
	}
}
