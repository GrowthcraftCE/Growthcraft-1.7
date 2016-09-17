/*
 * The MIT License (MIT)
 *
 * Copyright (c) < 2014, Gwafu
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.fishtrap.common.block;

import java.util.Random;

import growthcraft.api.fishtrap.FishTrapRegistry;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.common.tileentity.feature.IInteractionObject;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.core.Utils;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;
import growthcraft.fishtrap.GrowthCraftFishTrap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;

public class BlockFishTrap extends GrcBlockContainer
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	private final float chance = GrowthCraftFishTrap.getConfig().fishTrapCatchRate;
	private Random rand = new Random();

	public BlockFishTrap()
	{
		super(Material.wood);
		setTickRandomly(true);
		setHardness(0.4F);
		setStepSound(soundTypeWood);
		setBlockName("grc.fishTrap");
		setTileEntityType(TileEntityFishTrap.class);
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	protected boolean openGui(EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = getTileEntity(world, x, y, z);
		if (te instanceof IInteractionObject)
		{
			player.openGui(GrowthCraftFishTrap.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (super.onBlockActivated(world, x, y, z, player, meta, par7, par8, par9)) return true;
		return !player.isSneaking() && openGui(player, world, x, y, z);
	}

	private boolean isWater(Block block)
	{
		return BlockCheck.isWater(block);
	}

	private float applyBiomeCatchModifier(World world, int x, int y, int z, float f)
	{
		boolean isInWaterBiome;
		if (GrowthCraftFishTrap.getConfig().useBiomeDict)
		{
			final BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
			isInWaterBiome = BiomeDictionary.isBiomeOfType(biome, Type.WATER);
		}
		else
		{
			isInWaterBiome = Utils.isIDInList(world.getBiomeGenForCoords(x, z).biomeID, GrowthCraftFishTrap.getConfig().biomesList);
		}

		if (isInWaterBiome)
		{
			f *= 1.75;
		}
		return f;
	}

	private float getCatchRate(World world, int x, int y, int z)
	{
		final TileEntityFishTrap te = getTileEntity(world, x, y, z);
		if (te == null) return 0.0f;
		final int checkSize = 3;
		final int i = x - ((checkSize - 1) / 2);
		final int j = y - ((checkSize - 1) / 2);
		final int k = z - ((checkSize - 1) / 2);
		float f = 1.0F;

		for (int loopy = 0; loopy <= checkSize; loopy++)
		{
			for (int loopx = 0; loopx <= checkSize; loopx++)
			{
				for (int loopz = 0; loopz <= checkSize; loopz++)
				{
					final Block water = world.getBlock(i + loopx, j + loopy, k + loopz);
					float f1 = 0.0F;
					//1.038461538461538;

					if (water != null && isWater(water))
					{
						//f1 = 1.04F;
						f1 = 3.0F;
						//f1 = 17.48F;
					}

					f1 /= 4.0F;

					f += f1;
				}
			}
		}
		f = applyBiomeCatchModifier(world, x, y, z, f);
		return te.applyBaitModifier(f);
	}

	protected ItemStack pickCatch(World world, int x, int y, int z)
	{
		final String catchGroup = FishTrapRegistry.instance().getRandomCatchGroup(world.rand);
		GrowthCraftFishTrap.getLogger().info("Picking Catch from group=%s x=%d y=%d z=%d dimension=%d", catchGroup, x, y, z, world.provider.dimensionId);
		return FishTrapRegistry.instance().getRandomCatchFromGroup(world.rand, catchGroup);
	}

	protected void doCatch(World world, int x, int y, int z, TileEntityFishTrap te)
	{
		final ItemStack item = pickCatch(world, x, y, z);
		if (item != null)
		{
			GrowthCraftFishTrap.getLogger().info("Attempting to add item to inventory x=%d y=%d z=%d dimension=%d item=%s", x, y, z, world.provider.dimensionId, item);
			if (te.addStack(item))
			{
				GrowthCraftFishTrap.getLogger().info("Added item to inventory x=%d y=%d z=%d dimension=%d item=%s", x, y, z, world.provider.dimensionId, item);
				te.consumeBait();
			}
		}
	}

	protected void attemptCatch(World world, int x, int y, int z, Random random, TileEntityFishTrap te, boolean debugFlag)
	{
		final float f = this.getCatchRate(world, x, y, z);
		GrowthCraftFishTrap.getLogger().info("Attempting Catch rate=%f", f);
		if (random.nextInt((int)(this.chance / f) + 1) == 0 || debugFlag)
		{
			doCatch(world, x, y, z, te);
		}
	}

	private boolean canCatch(World world, int x, int y, int z)
	{
		return isWater(world.getBlock(x, y, z - 1)) ||
			isWater(world.getBlock(x, y, z + 1)) ||
			isWater(world.getBlock(x - 1, y, z)) ||
			isWater(world.getBlock(x + 1, y, z));
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);
		final TileEntityFishTrap te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			GrowthCraftFishTrap.getLogger().info("Checking if fishtrap can catch x=%d y=%d z=%d dimension=%d", x, y, z, world.provider.dimensionId);
			if (canCatch(world, x, y, z))
			{
				attemptCatch(world, x, y, z, random, te, false);
			}
		}
		else
		{
			GrowthCraftFishTrap.getLogger().warn("Missing TileEntityFishTrap at x=%d y=%d z=%d dimension=%d", x, y, z, world.provider.dimensionId);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[1];

		icons[0] = reg.registerIcon("grcfishtrap:fishtrap");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return icons[0];
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 0;
	}

	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int par5)
	{
		final TileEntityFishTrap te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			return Container.calcRedstoneFromInventory(te);
		}
		return 0;
	}
}
