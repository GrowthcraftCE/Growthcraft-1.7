package growthcraft.apples.village;

import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.lang.Math;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.utils.SchemaToVillage.BlockEntry;
import growthcraft.core.utils.SchemaToVillage.IBlockEntries;
import growthcraft.core.utils.SchemaToVillage;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentVillageAppleFarm extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	// Design by Ar97x
	static private final String appleFarmSchema[][] = {
		{
			"x---------x",
			"|         |",
			"|         |",
			"|  s   s  |",
			"|         |",
			"|         |",
			"|         |",
			"|  s   s  |",
			"|         |",
			"|         |",
			"x---x x---x"
		},
		{
			"fffffffffff",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"fffffgfffff"
		},
		{
			"t         t",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"t   t t   t"
		},
	};

	public ComponentVillageAppleFarm(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
	{
		super(startPiece, par2);
		this.coordBaseMode = coordBaseMode;
		this.boundingBox = boundingBox;
	}

	public static ComponentVillageAppleFarm buildComponent(Start startPiece, List list, Random random, int x, int y, int z, int coordBaseMode, int par7)
	{
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 13, 4, 13, coordBaseMode);
		if (canVillageGoDeeper(structureboundingbox)) {
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
				return new ComponentVillageAppleFarm(startPiece, par7, random, structureboundingbox, coordBaseMode);
			}
		}
		return null;
	}

	@Override
	public void placeBlockAtCurrentPosition(World world, Block block, int meta, int x, int y, int z, StructureBoundingBox box)
	{
		super.placeBlockAtCurrentPosition(world, block, meta, x, y, z, box);
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

			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
		}

		boolean vert = (this.coordBaseMode == 2 || this.coordBaseMode == 0);
		HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();
		map.put('x', new BlockEntry(Blocks.log, 0));
		map.put('-', new BlockEntry(Blocks.log, vert ? 4 : 8));
		map.put('|', new BlockEntry(Blocks.log, vert ? 8 : 4));

		map.put('f', new BlockEntry(Blocks.fence, 0));
		map.put('g', new BlockEntry(Blocks.fence_gate, this.getMetadataWithOffset(Blocks.fence_gate, 0)));
		map.put('t', new BlockEntry(Blocks.torch, 0));

		// not sure how to place an apple tree, but we can place a sapling with its maximum metadata set, by the next tick
		// this should grow into an apple tree
		map.put('s', new BlockEntry(GrowthCraftApples.appleSapling, 8));

		SchemaToVillage.drawSchema(this, world, random, box, appleFarmSchema, map);
		return true;
	}
}
