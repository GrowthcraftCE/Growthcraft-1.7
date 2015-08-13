package growthcraft.bees.village;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.utils.SchemaToVillage.BlockEntry;
import growthcraft.core.utils.SchemaToVillage.IBlockEntries;
import growthcraft.core.utils.SchemaToVillage.MultiBlockEntries;
import growthcraft.core.utils.SchemaToVillage;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.World;

public class ComponentVillageApiarist extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	// Design by Ar97x, modified by IceDragon (I made the tree levaves a 3x3x3 cube, makes it look neat)
	static private final String apiaristSchema[][] = {
		{
			"         ",
			"         ",
			"ddddddd  ",
			"ddddddd  ",
			"ddddddd  ",
			"ddddddd  ",
			"ddcdddd  ",
			"ccccccccc",
			"xpppppppx",
			"cpppppppc",
			"cpppppppc",
			"xpppppppx",
			"cxccxppcc",
			"     22  "
		},
		{
			"         ",
			"         ",
			"ffffxff  ",
			"f,,,,,f  ",
			"f,,,,,f  ",
			"f,,,,,f  ",
			"f, ,,,f  ",
			"xx xxxxxx",
			"x   - - x",
			"Y   x-x Y",
			"B       B",
			"x       x",
			"xxxxx  xx",
			"         "
		},
		{
			"         ",
			"         ",
			"    x    ",
			"    H    ",
			"t     t  ",
			"         ",
			"         ",
			"cx pxggxc",
			"x       x",
			"g    +  g",
			"g       g",
			"x       x",
			"cxggx  xc",
			"         "
		},
		{ // the torches for this level are placed manually
			"         ",
			"   lll   ",
			"   lxl   ",
			"   lll   ",
			"         ",
			"         ",
			"888888888",
			"cxppxppxc",
			"x   ___ x",
			"Y   ___ Y",
			"B       B",
			"x       x",
			"cxppxppxc",
			"222222222"
		},
		{
			"         ",
			"   lll   ",
			"   lll   ",
			"   lll   ",
			"         ",
			"         ",
			"         ",
			"888888888",
			"xpppppppx",
			"Bp     pB",
			"Yp     pY",
			"xpppppppx",
			"222222222",
			"         "
		},
		{
			"         ",
			"   lll   ",
			"   lll   ",
			"   lll   ",
			"         ",
			"         ",
			"         ",
			"         ",
			"788888889",
			"4BYBYBYB6",
			"4YBYBYBY6",
			"122222223",
			"         ",
			"         "
		}
	};

	private static final WeightedRandomChestContent[] apiaristChestContents = new WeightedRandomChestContent[] {
		new WeightedRandomChestContent(GrowthCraftBees.bee, 0, 1, 2, 3),
		new WeightedRandomChestContent(GrowthCraftBees.honeyComb, 0, 1, 3, 5),
		new WeightedRandomChestContent(GrowthCraftBees.honeyJar, 0, 1, 1, 10),
		new WeightedRandomChestContent(Item.getItemFromBlock(GrowthCraftBees.beeBox), 0, 1, 2, 5)
	};

	public ComponentVillageApiarist(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
	{
		super(startPiece, par2);
		this.coordBaseMode = coordBaseMode;
		this.boundingBox = boundingBox;
	}

	public static ComponentVillageApiarist buildComponent(Start startPiece, List list, Random random, int x, int y, int z, int coordBaseMode, int par7)
	{
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 12, 7, 15, coordBaseMode);
		if (canVillageGoDeeper(structureboundingbox)) {
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
				return new ComponentVillageApiarist(startPiece, par7, random, structureboundingbox, coordBaseMode);
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

			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
		}

		HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();
		boolean vert = (this.coordBaseMode == 2 || this.coordBaseMode == 0);

		map.put('c', new BlockEntry(Blocks.cobblestone));
		map.put('d', new BlockEntry(Blocks.grass, 0));
		map.put('p', new BlockEntry(Blocks.planks));
		map.put('x', new BlockEntry(Blocks.log));
		map.put('l', new BlockEntry(Blocks.leaves));
		map.put('g', new BlockEntry(Blocks.stained_glass, 15));

		map.put('f', new BlockEntry(Blocks.fence));
		map.put('-', new BlockEntry(Blocks.wooden_slab, 8)); // high slab
		map.put('_', new BlockEntry(Blocks.wooden_slab, 0)); // low slab
		map.put(',', new MultiBlockEntries(new BlockEntry[]{
			new BlockEntry(Blocks.red_flower, 4),
			new BlockEntry(Blocks.red_flower, 5),
			new BlockEntry(Blocks.red_flower, 6),
			new BlockEntry(Blocks.red_flower, 7)
		}));

		map.put('1', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2)));
		map.put('2', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2)));
		map.put('3', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2)));

		map.put('4', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 0)));
		map.put('6', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 1)));

		map.put('7', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3)));
		map.put('8', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3)));
		map.put('9', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3)));

		map.put('t', new BlockEntry(Blocks.torch));

		map.put('Y', new BlockEntry(Blocks.wool, 4));
		map.put('B', new BlockEntry(Blocks.wool, 15));

		map.put('H', new BlockEntry(GrowthCraftBees.beeHive, 0));
		map.put('+', new BlockEntry(GrowthCraftBees.beeBox, this.getMetadataWithOffset(GrowthCraftBees.beeBox, 2)));

		SchemaToVillage.drawSchema(this, world, random, box, apiaristSchema, map);

		this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 1, 3, 8, box);
		this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 7, 3, 8, box);
		this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 1, 3, 11, box);
		this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 7, 3, 11, box);

		this.placeDoorAtCurrentPosition(world, box, random, 2, 1, 7, this.getMetadataWithOffset(Blocks.wooden_door, 1));
		this.placeDoorAtCurrentPosition(world, box, random, 5, 1, 12, this.getMetadataWithOffset(Blocks.wooden_door, 1));
		this.placeDoorAtCurrentPosition(world, box, random, 6, 1, 12, this.getMetadataWithOffset(Blocks.wooden_door, 1));

		this.generateStructureChestContents(world, box, random, 1, 1, 11, apiaristChestContents, 3 + random.nextInt(6));
		return true;
	}

	@Override
	protected int getVillagerType(int par1)
	{
		return GrowthCraftBees.villagerApiarist_id;
	}
}
