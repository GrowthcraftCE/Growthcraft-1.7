package growthcraft.bamboo.village;

import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.lang.Math;

import growthcraft.bamboo.GrowthCraftBamboo;
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

public class ComponentVillageBambooYard extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	// Design by Ar97x
	static private final String bambooYardSchema[][] = {
		{
			"           ",
			"           ",
			"  p     p  ",
			"     ~     ",
			"    ~~~    ",
			"   ~~~~~   ",
			"    ~~~    ",
			"     ~     ",
			"  p     p  ",
			"           ",
			"           ",
			"           "
		},
		{
			"ppppppppppp",
			"p         p",
			"p t     t p",
			"p   bsb   p",
			"p  bs sb  p",
			"p  s   s  p",
			"p  bs sb  p",
			"p   bsb   p",
			"p t     t p",
			"p         p",
			"ppppp ppppp",
			"    pDp    "
		},
		{
			"W         W",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"W   W W   W",
			"    WdW    "
		},
		{
			"WWWWWWWWWWW",
			"W         W",
			"W         W",
			"W         W",
			"W         W",
			"W         W",
			"W         W",
			"W         W",
			"W         W",
			"W         W",
			"WWWWW WWWWW",
			"    WWW    "
		},
	};

	public ComponentVillageBambooYard(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
	{
		super(startPiece, par2);
		this.coordBaseMode = coordBaseMode;
		this.boundingBox = boundingBox;
	}

	public static ComponentVillageBambooYard buildComponent(Start startPiece, List list, Random random, int x, int y, int z, int coordBaseMode, int par7)
	{
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 13, 4, 13, coordBaseMode);
		if (canVillageGoDeeper(structureboundingbox)) {
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
				return new ComponentVillageBambooYard(startPiece, par7, random, structureboundingbox, coordBaseMode);
			}
		}
		return null;
	}

	public void placeBlockAtCurrentPositionPub(World world, Block block, int meta, int x, int y, int z, StructureBoundingBox box)
	{
		placeBlockAtCurrentPosition(world, block, meta, x, y, z, box);
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

			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3 - 1, 0);
		}

		boolean vert = (this.coordBaseMode == 2 || this.coordBaseMode == 0);
		HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();

		map.put('b', new BlockEntry(GrowthCraftBamboo.bambooShoot, 1));
		map.put('D', new BlockEntry(GrowthCraftBamboo.bambooDoor, this.getMetadataWithOffset(GrowthCraftBamboo.bambooDoor, 2))); // okay folks, no BIG D jokes here
		map.put('d', new BlockEntry(GrowthCraftBamboo.bambooDoor, this.getMetadataWithOffset(GrowthCraftBamboo.bambooDoor, 8 | 1))); // top of the door brought forward
		map.put('p', new BlockEntry(GrowthCraftBamboo.bambooBlock, 0));
		map.put('s', new BlockEntry(GrowthCraftBamboo.bambooSingleSlab, 0));
		map.put('t', new BlockEntry(Blocks.torch, 0));
		map.put('~', new BlockEntry(Blocks.water, 0));
		map.put('W', new BlockEntry(GrowthCraftBamboo.bambooWall, 0));

		SchemaToVillage.drawSchema(this, world, random, box, bambooYardSchema, map);
		return true;
	}
}
