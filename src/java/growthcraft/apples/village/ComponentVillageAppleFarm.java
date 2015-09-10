package growthcraft.apples.village;

import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.lang.Math;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.apples.world.WorldGenAppleTree;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.utils.SchemaToVillage.BlockEntry;
import growthcraft.core.utils.SchemaToVillage.IBlockEntries;
import growthcraft.core.utils.SchemaToVillage;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentVillageAppleFarm extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	// Design by Ar97x
	static private final String appleFarmSchema[][] = {
		{
			"x---x x---x",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"x---------x"
		},
		{
			"fffffgfffff",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"fffffffffff"
		},
		{
			"t   t t   t",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"t         t"
		},
	};

	public ComponentVillageAppleFarm() {} // DO NOT REMOVE
	public ComponentVillageAppleFarm(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
	{
		super(startPiece, par2);
		this.coordBaseMode = coordBaseMode;
		this.boundingBox = boundingBox;
	}

	public static ComponentVillageAppleFarm buildComponent(Start startPiece, List list, Random random, int x, int y, int z, int coordBaseMode, int par7)
	{
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 11, 7, 11, coordBaseMode);
		if (canVillageGoDeeper(structureboundingbox)) {
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
				return new ComponentVillageAppleFarm(startPiece, par7, random, structureboundingbox, coordBaseMode);
			}
		}
		return null;
	}

	public void placeBlockAtCurrentPositionPub(World world, Block block, int meta, int x, int y, int z, StructureBoundingBox box)
	{
		placeBlockAtCurrentPosition(world, block, meta, x, y, z, box);
	}

	protected void placeWorldGenAt(World world, Random random, int tx, int ty, int tz, StructureBoundingBox bb, WorldGenerator generator)
    {
        int x = this.getXWithOffset(tx, tz);
        int y = this.getYWithOffset(ty);
        int z = this.getZWithOffset(tx, tz);

        if (bb.isVecInside(x, y, z))
        {
            generator.generate(world, random, x, y, z);
        }
    }

	public boolean addComponentParts(World world, Random random, StructureBoundingBox box)
	{
		if (this.field_143015_k < 0)
		{
			this.field_143015_k = this.getAverageGroundLevel(world, box);

			if (this.field_143015_k < 0)
			{
				return true;
			}

			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6, 0);
		}

		// clear entire bounding box
		this.fillWithBlocks(world, box, 0, 0, 0, 11, 3, 11, Blocks.air, Blocks.air, false);

		boolean vert = (this.coordBaseMode == 2 || this.coordBaseMode == 0);
		HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();
		map.put('x', new BlockEntry(Blocks.log, 0));
		map.put('-', new BlockEntry(Blocks.log, vert ? 4 : 8));
		map.put('|', new BlockEntry(Blocks.log, vert ? 8 : 4));

		map.put('f', new BlockEntry(Blocks.fence, 0));
		map.put('g', new BlockEntry(Blocks.fence_gate, this.getMetadataWithOffset(Blocks.fence_gate, 0)));
		map.put('t', new BlockEntry(Blocks.torch, 0));

		SchemaToVillage.drawSchema(this, world, random, box, appleFarmSchema, map, 0, 0, 0);

		WorldGenAppleTree genAppleTree = new WorldGenAppleTree(true);
		placeWorldGenAt(world, random, 3, 0, 3, box, genAppleTree);
		placeWorldGenAt(world, random, 7, 0, 3, box, genAppleTree);
		placeWorldGenAt(world, random, 3, 0, 7, box, genAppleTree);
		placeWorldGenAt(world, random, 7, 0, 7, box, genAppleTree);

		for (int row = 0; row < 11; ++row)
		{
			for (int col = 0; col < 11; ++col)
			{
				this.clearCurrentPositionBlocksUpwards(world, col, 7, row, box);
				this.func_151554_b(world, Blocks.dirt, 0, col, -1, row, box);
			}
		}
		return true;
	}
}
