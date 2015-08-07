package growthcraft.grapes;

import growthcraft.core.GrowthCraftCore;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import growthcraft.core.integration.AppleCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGrapeVine1 extends Block implements IPlantable
{
	private final float growth = GrowthCraftGrapes.grapeVine1_growth;

	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;
	public boolean graphicFlag;

	public BlockGrapeVine1()
	{
		super(Material.plants);
		this.setTickRandomly(true);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.grapeVine1");
		this.setCreativeTab(null);
	}

	void incrementGrowth(World world, int x, int y, int z, int meta)
	{
		int previousMetadata = meta;
		++meta;
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		AppleCore.announceGrowthTick(this, world, x, y, z, previousMetadata);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);

		// 0 - seedling
		// 1 - soft sapling
		// 2 - hard sapling
		// 3 - trunk

		Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, x, y, z, random);
		if (allowGrowthResult == Event.Result.DENY)
			return;

		int meta = world.getBlockMetadata(x, y, z);
		float f = this.getGrowthRate(world, x, y, z);

		boolean continueGrowth = random.nextInt((int)(this.growth / f) + 1) == 0;
		if (allowGrowthResult == Event.Result.ALLOW || continueGrowth)
		{
			/* Is here a rope block above this? */
			if (meta == 0 && world.getBlock(x, y + 1, z) == GrowthCraftCore.ropeBlock)
			{
				incrementGrowth(world, x, y, z, meta);
				world.setBlock(x, y + 1, z, GrowthCraftGrapes.grapeLeaves, 0, 3);
			}
			if (meta == 0 && world.isAirBlock(x, y + 1, z))
			{
				incrementGrowth(world, x, y, z, meta);
				world.setBlock(x, y + 1, z, this, 0, 3);
			}
			else if (meta == 0 && world.getBlock(x, y + 1, z) == GrowthCraftGrapes.grapeLeaves)
			{
				incrementGrowth(world, x, y, z, meta);
			}
		}
	}

	private boolean isGrapeVine(Block block)
	{
		return block == GrowthCraftGrapes.grapeVine0 || block == GrowthCraftGrapes.grapeVine1;
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		int j = y;
		if (world.getBlock(x, j - 1, z) == this && world.getBlock(x, j - 2, z) == Blocks.farmland)
		{
			j = y - 1;
		}
		Block l = world.getBlock(x, j, z - 1);
		Block i1 = world.getBlock(x, j, z + 1);
		Block j1 = world.getBlock(x - 1, j, z);
		Block k1 = world.getBlock(x + 1, j, z);
		Block l1 = world.getBlock(x - 1, j, z - 1);
		Block i2 = world.getBlock(x + 1, j, z - 1);
		Block j2 = world.getBlock(x + 1, j, z + 1);
		Block k2 = world.getBlock(x - 1, j, z + 1);
		boolean flag = this.isGrapeVine(j1) || this.isGrapeVine(k1);
		boolean flag1 = this.isGrapeVine(l) || this.isGrapeVine(i1);
		boolean flag2 = this.isGrapeVine(l1) || this.isGrapeVine(i2) || this.isGrapeVine(j2) || this.isGrapeVine(k2);

		for (int l2 = x - 1; l2 <= x + 1; ++l2)
		{
			for (int i3 = z - 1; i3 <= z + 1; ++i3)
			{
				Block block = world.getBlock(l2, j - 1, i3);
				float f1 = 0.0F;

				if (block != null && block == Blocks.farmland)
				{
					f1 = 1.0F;

					if (block.isFertile(world, l2, j - 1, i3))
					{
						f1 = 3.0F;
					}
				}

				if (l2 != x || i3 != z)
				{
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		if (flag2 || flag && flag1)
		{
			f /= 2.0F;
		}

		return f;
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, 2);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		Block soil = world.getBlock(x, y - 1, z);
		return (soil != null && soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this)) || world.getBlock(x, y - 1, z) == this;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapeSeeds;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return false;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return GrowthCraftGrapes.grapeSeeds;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[3];

		tex[0] = reg.registerIcon("grcgrapes:trunk");
		tex[1] = reg.registerIcon("grcgrapes:leaves");
		tex[2] = reg.registerIcon("grcgrapes:leaves_opaque");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[0];
	}

	public IIcon getLeafTexture()
	{
		this.graphicFlag = ((BlockLeaves)Blocks.leaves).isOpaqueCube();
		return !this.graphicFlag ? this.tex[1] : this.tex[2];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderGrapeVine1.id;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
	{
		int meta = world.getBlockMetadata(x, y, z);
		float f = 0.0625F;

		if (meta == 0)
		{
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 0.5F, 10*f);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
			this.setBlockBounds(4*f, 0.5F, 4*f, 12*f, 1.0F, 12*f);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}
		else if (meta == 1)
		{
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}

		this.setBlockBoundsBasedOnState(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		float f = 0.0625F;

		switch (meta)
		{
		case 0:
			this.setBlockBounds(4*f, 0.0F, 4*f, 12*f, 1.0F, 12*f);
			break;
		case 1:
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
			break;
		}
	}

	/************
	 * IPLANTABLE
	 ************/
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}
}
