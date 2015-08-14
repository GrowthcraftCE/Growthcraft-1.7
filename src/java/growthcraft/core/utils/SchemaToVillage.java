package growthcraft.core.utils;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class SchemaToVillage
{
	public static interface IVillage
	{
		public void placeBlockAtCurrentPositionPub(World world, Block block, int meta, int x, int y, int z, StructureBoundingBox box);
	}

	public static interface IBlockEntries
	{
		public BlockEntry getBlockEntry(Random random);
	}

	public static class BlockEntry implements IBlockEntries {
		private Block block;
		private int meta;

		public BlockEntry(Block block, int meta)
		{
			this.block = block;
			this.meta = meta;
		}

		public BlockEntry(Block block)
		{
			this(block, 0);
		}

		public BlockEntry getBlockEntry(Random random)
		{
			return this;
		}

		public Block getBlock()
		{
			return this.block;
		}

		public int getMetadata()
		{
			return this.meta;
		}
	}

	public static class MultiBlockEntries implements IBlockEntries {
		private BlockEntry entries[];

		public MultiBlockEntries(BlockEntry blockEntries[])
		{
			this.entries = blockEntries;
		}

		public BlockEntry getBlockEntry(Random random)
		{
			if (this.entries.length == 0) {
				return null;
			}
			return entries[random.nextInt(this.entries.length)];
		}
	}

	public static void drawSchema(IVillage village, World world, Random random, StructureBoundingBox box, String schema[][], Map<Character, IBlockEntries> map)
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
					Block block = null;
					IBlockEntries entries = map.get(row.charAt(x));
					if (entries != null)
					{
						BlockEntry entry = entries.getBlockEntry(random);
						if (entry != null)
						{
							block = entry.getBlock();
							meta = entry.getMetadata();
						}
					}
					if (block != null) {
						village.placeBlockAtCurrentPositionPub(world, block, meta, x, z, y, box);
					}
				}
			}
		}
	}
}
