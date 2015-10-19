package growthcraft.grapes.block;

import java.util.Random;

import growthcraft.core.block.IBlockRope;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.utils.BlockCheck;
import growthcraft.core.utils.BlockFlags;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.renderer.RenderGrapeLeaves;
import growthcraft.grapes.utils.GrapeBlockCheck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGrapeLeaves extends BlockLeavesBase implements IBlockRope
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	private final int grapeLeavesGrowthRate = GrowthCraftGrapes.getConfig().grapeLeavesGrowthRate;
	private final int grapeSpawnRate = GrowthCraftGrapes.getConfig().grapeSpawnRate;
	// how far can a grape leaf grow before it requires support from a trunk
	private final int grapeSupportedLength = GrowthCraftGrapes.getConfig().grapeSupportedLength;

	public BlockGrapeLeaves()
	{
		super(Material.leaves, false);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.grapeLeaves");
		this.setCreativeTab(null);
	}

	private boolean isTrunk(World world, int x, int y, int z)
	{
		return GrapeBlockCheck.isGrapeVineTrunk(world.getBlock(x, y, z));
	}

	public boolean isSupportedByTrunk(World world, int x, int y, int z)
	{
		return isTrunk(world, x, y - 1, z);
	}

	/**
	 * Use this method to check if the block can grow outwards on a rope
	 *
	 * @param world - the world
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true if the block can grow here, false otherwise
	 */
	public boolean canGrowOutwardsOnRope(World world, int x, int y, int z)
	{
		if (BlockCheck.isRope(world.getBlock(x + 1, y, z))) return true;
		if (BlockCheck.isRope(world.getBlock(x - 1, y, z))) return true;
		if (BlockCheck.isRope(world.getBlock(x, y, z + 1))) return true;
		if (BlockCheck.isRope(world.getBlock(x, y, z - 1))) return true;
		return false;
	}

	public boolean canGrowOutwards(World world, int x, int y, int z)
	{
		final boolean leavesTotheSouth = world.getBlock(x + 1, y, z) == this;
		final boolean leavesToTheNorth = world.getBlock(x - 1, y, z) == this;
		final boolean leavesToTheEast = world.getBlock(x, y, z + 1) == this;
		final boolean leavesToTheWest = world.getBlock(x, y, z - 1) == this;

		if (!leavesTotheSouth && !leavesToTheNorth && !leavesToTheEast && !leavesToTheWest) return false;

		for (int i = 1; i <= grapeSupportedLength; ++i)
		{
			if (leavesTotheSouth && isTrunk(world, x + i, y - 1, z)) return true;
			if (leavesToTheNorth && isTrunk(world, x - i, y - 1, z)) return true;
			if (leavesToTheEast && isTrunk(world, x, y - 1, z + i)) return true;
			if (leavesToTheWest && isTrunk(world, x, y - 1, z - i)) return true;
		}
		return false;
	}

	/**
	 * Variation of canGrowOutwards, use this method to check rope blocks
	 *
	 * @param world - the world
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true if the block can grow here, false otherwise
	 */
	public boolean canGrowHere(World world, int x, int y, int z)
	{
		if (BlockCheck.isRope(world.getBlock(x, y, z)))
		{
			return canGrowOutwards(world, x, y, z);
		}
		return false;
	}

	private void setGrapeBlock(World world, int x, int y, int z)
	{
		world.setBlock(x, y, z, GrowthCraftGrapes.grapeBlock.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
	}

	public boolean growGrapeBlock(World world, int x, int y, int z)
	{
		if (world.isAirBlock(x, y - 1, z))
		{
			if (!world.isRemote)
			{
				setGrapeBlock(world, x, y - 1, z);
			}
			return true;
		}
		return false;
	}

	private void grow(World world, int x, int y, int z, Random random)
	{
		if (world.isAirBlock(x, y - 1, z) && (random.nextInt(this.grapeSpawnRate) == 0))
		{
			setGrapeBlock(world, x, y - 1, z);
		}

		if (world.rand.nextInt(this.grapeLeavesGrowthRate) == 0)
		{
			if (canGrowOutwards(world, x, y, z))
			{
				final ForgeDirection dir = BlockCheck.DIR4[random.nextInt(4)];

				if (canGrowHere(world, x + dir.offsetX, y, z + dir.offsetZ))
				{
					world.setBlock(x + dir.offsetX, y, z + dir.offsetZ, this, 0, BlockFlags.UPDATE_CLIENT);
				}
			}
		}
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			world.setBlock(x, y, z, GrowthCraftCore.ropeBlock.getBlock());
		}
		else
		{
			grow(world, x, y, z, random);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1)
		{
			final double d0 = (double)((float)x + random.nextFloat());
			final double d1 = (double)y - 0.05D;
			final double d2 = (double)((float)z + random.nextFloat());
			world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		if (this.isSupportedByTrunk(world, x, y, z))
		{
			return true;
		}
		else
		{
			for (ForgeDirection dir : BlockCheck.DIR4)
			{
				for (int i = 1; i <= grapeSupportedLength; ++i)
				{
					final int bx = x + dir.offsetX * i;
					final int bz = z + dir.offsetZ * i;
					if (world.getBlock(bx, y, bz) != this)
					{
						break;
					}
					else if (isSupportedByTrunk(world, bx, y, bz))
					{
						return true;
					}
				}
			}
		}
		return false;
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
	public boolean isLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return false;
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) instanceof IBlockRope)
		{
			return true;
		}
		return false;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return GrowthCraftCore.rope.getItem();
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
		this.icons = new IIcon[4];

		icons[0] = reg.registerIcon("grcgrapes:leaves");
		icons[1] = reg.registerIcon("grcgrapes:leaves_opaque");
		icons[2] = reg.registerIcon("grccore:rope_1");
		icons[3] = reg.registerIcon("grcgrapes:leaves_half");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconByIndex(int index)
	{
		return icons[index];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return icons[isOpaqueCube() ? 1 : 0];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderGrapeLeaves.id;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return Blocks.leaves.isOpaqueCube();
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	/************
	 * COLORS
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		final double d0 = 0.5D;
		final double d1 = 1.0D;
		return ColorizerFoliage.getFoliageColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
		return ColorizerFoliage.getFoliageColorBasic();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);

		int r = 0;
		int g = 0;
		int b = 0;

		for (int l1 = -1; l1 <= 1; ++l1)
		{
			for (int i2 = -1; i2 <= 1; ++i2)
			{
				final int j2 = world.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor(x + i2, y, z + l1);
				r += (j2 & 16711680) >> 16;
				g += (j2 & 65280) >> 8;
				b += j2 & 255;
			}
		}

		return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
	}
}
