package growthcraft.rice.village;

import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.lang.Math;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.utils.SchemaToVillage;
import growthcraft.core.utils.SchemaToVillage.BlockEntry;
import growthcraft.rice.GrowthCraftRice;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentVillageRiceField extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	static final String riceFieldSchema[][] = {
		{
			"x         x",
			" x-------x ",
			" |ppppppp| ",
			" |pwwwwwp| ",
			" |pwpppwp| ",
			" |pwpppwp| ",
			" |pwpppwp| ",
			" |pwwwwwp| ",
			" |ppppppp| ",
			" x-------x ",
			"x    |    x",
			"    sss    "
		},
		{
			"x---------x",
			"|         |",
			"| rrrrrrr |",
			"| r     r |",
			"| r rrr r |",
			"| r rrr r |",
			"| r rrr r |",
			"| r     r |",
			"| rrrrrrr |",
			"|         |",
			"x---- ----x",
			"           "
		},
		{
			"f         f",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"f   fgf   f",
			"           "
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
			"t   t t   t",
			"           "
		}
	};

	public ComponentVillageRiceField(){}

	public ComponentVillageRiceField(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int par5)
	{
		super(startPiece, par2);
		this.coordBaseMode = par5;
		this.boundingBox = boundingBox;
	}

	public static ComponentVillageRiceField buildComponent(Start startPiece, List list, Random random, int x, int y, int z, int direction, int par7)
	{
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 13, 4, 13, direction);
		if (canVillageGoDeeper(structureboundingbox)) {
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
				return new ComponentVillageRiceField(startPiece, par7, random, structureboundingbox, direction);
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

		HashMap<Character, BlockEntry> map = new HashMap<Character, BlockEntry>();
		map.put('x', new BlockEntry(Blocks.log, 0));
		map.put('-', new BlockEntry(Blocks.log, 4));
		map.put('|', new BlockEntry(Blocks.log, 8));

		map.put('f', new BlockEntry(Blocks.fence, 0));
		map.put('g', new BlockEntry(Blocks.fence_gate, 0));
		map.put('t', new BlockEntry(Blocks.torch, 0));
		map.put('w', new BlockEntry(Blocks.water, 0));
		map.put('s', new BlockEntry(Blocks.oak_stairs, 2));

		map.put('p', new BlockEntry(GrowthCraftRice.paddyField, GrowthCraftRice.paddyFieldMax));
		map.put('r', new BlockEntry(GrowthCraftRice.riceBlock, 6));

		SchemaToVillage.drawSchema(this, world, random, box, riceFieldSchema, map);
		return true;
	}
}
