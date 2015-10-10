package growthcraft.grapes.block;

import java.util.Random;

import growthcraft.core.block.ICropDataProvider;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.core.integration.AppleCore;
import growthcraft.core.utils.BlockCheck;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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

public class BlockGrapeVine0 extends Block implements IPlantable, ICropDataProvider
{
	private final float growth = GrowthCraftGrapes.getConfig().grapeVineSeedlingGrowthRate;

	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockGrapeVine0()
	{
		super(Material.plants);
		this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.grapeVine0");
		this.setCreativeTab(null);
	}

	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta)
	{
		return (float)(meta / 1.0);
	}

	public void incrementGrowth(World world, int x, int y, int z, int meta)
	{
		int previousMetadata = meta;
		++meta;
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
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

		if (world.getBlockLightValue(x, y + 1, z) >= 9)
		{
			Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, x, y, z, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			int meta = world.getBlockMetadata(x, y, z);
			float f = this.getGrowthRate(world, x, y, z);

			boolean continueGrowth = random.nextInt((int)(this.growth / f) + 1) == 0;
			if (allowGrowthResult == Event.Result.ALLOW || continueGrowth)
			{
				if (meta == 0)
				{
					incrementGrowth(world, x, y, z, meta);
				}
				else
				{
					world.setBlock(x, y, z, GrowthCraftGrapes.grapeVine1.getBlock(), 0, 3);
				}
			}
		}
	}

	private boolean isGrapeVine(Block block)
	{
		return block == GrowthCraftGrapes.grapeVine0.getBlock() || block == GrowthCraftGrapes.grapeVine1.getBlock();
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		Block l = world.getBlock(x, y, z - 1);
		Block i1 = world.getBlock(x, y, z + 1);
		Block j1 = world.getBlock(x - 1, y, z);
		Block k1 = world.getBlock(x + 1, y, z);
		Block l1 = world.getBlock(x - 1, y, z - 1);
		Block i2 = world.getBlock(x + 1, y, z - 1);
		Block j2 = world.getBlock(x + 1, y, z + 1);
		Block k2 = world.getBlock(x - 1, y, z + 1);
		boolean flag = this.isGrapeVine(j1) || this.isGrapeVine(k1);
		boolean flag1 = this.isGrapeVine(l) || this.isGrapeVine(i1);
		boolean flag2 = this.isGrapeVine(l1) || this.isGrapeVine(i2) || this.isGrapeVine(j2) || this.isGrapeVine(k2);

		for (int l2 = x - 1; l2 <= x + 1; ++l2)
		{
			for (int i3 = z - 1; i3 <= z + 1; ++i3)
			{
				Block block = world.getBlock(l2, y - 1, i3);
				float f1 = 0.0F;

				if (block != null && block == Blocks.farmland)
				{
					f1 = 1.0F;

					if (block.isFertile(world, l2, y - 1, i3))
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
			world.setBlock(x, y, z, Blocks.air, 0, 3);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return BlockCheck.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapeSeeds.getItem();
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
		return null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("grcgrapes:vine_0");
		tex[1] = reg.registerIcon("grcgrapes:vine_1");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[meta];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return 1;
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		float f = 0.0625F;

		switch (meta)
		{
		case 0:
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 5*f, 10*f);
			break;
		case 1:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
