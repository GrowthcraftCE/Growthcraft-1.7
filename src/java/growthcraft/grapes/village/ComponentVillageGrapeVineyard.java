package growthcraft.grapes.village;

import java.util.List;
import java.util.Random;

import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentVillageGrapeVineyard extends StructureVillagePieces.Village
{
	public ComponentVillageGrapeVineyard(){}

	public ComponentVillageGrapeVineyard(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int par5)
	{
		super(startPiece, par2);
		this.coordBaseMode = par5;
		this.boundingBox = boundingBox;
	}

	public static ComponentVillageGrapeVineyard buildComponent(Start startPiece, List list, Random random, int par3, int par4, int par5, int par6, int par7)
	{
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 6, 9, par6);
		return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new ComponentVillageGrapeVineyard(startPiece, par7, random, structureboundingbox, par6) : null;
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

			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
		}

		this.fillWithBlocks(world, box, 0, 1, 0, 12, 6, 8, Blocks.air, Blocks.air, false);
		this.fillWithBlocks(world, box, 0, 0, 0, 0, 0, 8, Blocks.log, Blocks.log, false);
		this.fillWithBlocks(world, box, 12, 0, 0, 12, 0, 8, Blocks.log, Blocks.log, false);
		this.fillWithBlocks(world, box, 1, 0, 0, 11, 0, 0, Blocks.log, Blocks.log, false);
		this.fillWithBlocks(world, box, 1, 0, 8, 11, 0, 8, Blocks.log, Blocks.log, false);
		this.fillWithBlocks(world, box, 1, 0, 1, 11, 0, 7, Blocks.grass, Blocks.grass, false);
		int loop, loop2;

		for (loop = 1; loop < 12; loop = loop + 2)
		{
			this.fillWithBlocks(world, box, loop, 0, 2, loop, 0, 6, Blocks.water, Blocks.water, false);
			this.fillWithBlocks(world, box, loop, 0, 4, loop, 0, 4, Blocks.farmland, Blocks.farmland, false);
			this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, loop, 1, 1, box);
			this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, loop, 1, 7, box);
			this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, loop, 2, 1, box);
			this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, loop, 2, 7, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftCore.fenceRope, 0, loop, 3, 1, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftCore.fenceRope, 0, loop, 3, 7, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.grapeVine1, 1, loop, 1, 4, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.grapeVine1, 1, loop, 2, 4, box);
			for (loop2 = 2; loop2 <= 6; ++loop2)
			{
				this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.grapeLeaves, 0, loop, 3, loop2, box);
				if (MathHelper.getRandomIntegerInRange(random, 0, 2) != 0 && loop2 != 4)
				{
					this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.grapeBlock, 0, loop, 2, loop2, box);
				}
			}
		}

		for (loop = 0; loop < 9; ++loop)
		{
			for (loop2 = 0; loop2 < 13; ++loop2)
			{
				this.clearCurrentPositionBlocksUpwards(world, loop2, 6, loop, box);
				this.func_151554_b(world, Blocks.dirt, 0, loop2, -1, loop, box);
			}
		}

		return true;
	}
}
