/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.milk.common.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import growthcraft.api.core.util.BBox;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.milk.client.render.RenderCheeseBlock;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.EnumCheeseStage;
import growthcraft.milk.common.item.ItemBlockCheeseBlock;
import growthcraft.milk.common.tileentity.TileEntityCheeseBlock;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCheeseBlock extends GrcBlockContainer
{
	@SideOnly(Side.CLIENT)
	private Map<EnumCheeseType, Map<EnumCheeseStage, IIcon[]>> iconMap;

	public BlockCheeseBlock()
	{
		super(Material.cake);
		setHardness(0.5F);
		setStepSound(soundTypeCloth);
		setBlockName("grcmilk.CheeseBlock");
		setCreativeTab(GrowthCraftMilk.creativeTab);
		setTileEntityType(TileEntityCheeseBlock.class);
		final BBox bb = BBox.newCube(4f, 0f, 4f, 8f, 8f, 8f).scale(1f / 16f);
		setBlockBounds(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
	}

	@Override
	protected boolean shouldRestoreBlockState(World world, int x, int y, int z, ItemStack stack)
	{
		return true;
	}

	@Override
	protected boolean dropsTileStack(World world, int x, int y, int z, int metadata, int fortune)
	{
		return true;
	}

	@Override
	protected ItemStack createHarvestedBlockItemStack(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			return te.asItemStack();
		}
		return new ItemStack(this, 1, meta);
	}

	@Override
	protected void getTileItemStackDrops(List<ItemStack> ret, World world, int x, int y, int z, int metadata, int fortune)
	{
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			ret.add(te.asItemStack());
		}
		else
		{
			super.getTileItemStackDrops(ret, world, x, y, z, metadata, fortune);
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
	{
		final TileEntityCheeseBlock teCheeseBlock = getTileEntity(world, x, y, z);
		if (teCheeseBlock != null)
		{
			return teCheeseBlock.asItemStack();
		}
		return super.getPickBlock(target, world, x, y, z, player);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			te.populateDrops(ret);
		}
		return ret;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		if (item instanceof ItemBlockCheeseBlock)
		{
			final ItemBlockCheeseBlock ib = (ItemBlockCheeseBlock)item;
			for (EnumCheeseType cheese : EnumCheeseType.VALUES)
			{
				if (cheese.hasBlock())
				{
					final ItemStack stack = new ItemStack(item, 1, cheese.meta);
					ib.getTileTagCompound(stack);
					list.add(stack);
				}
			}
		}
	}

	@Override
	public int getRenderType()
	{
		return RenderCheeseBlock.RENDER_ID;
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
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.iconMap = new HashMap<EnumCheeseType, Map<EnumCheeseStage, IIcon[]>>();
		for (EnumCheeseType type : EnumCheeseType.VALUES)
		{
			if (!type.hasBlock()) continue;
			iconMap.put(type, new HashMap<EnumCheeseStage, IIcon[]>());
			final String prefix = "grcmilk:cheese/" + type.name;
			for (EnumCheeseStage stage : type.stages)
			{
				final IIcon[] icons = new IIcon[3];
				icons[0] = reg.registerIcon(String.format("%s_%s/bottom", prefix, stage.name));
				icons[1] = reg.registerIcon(String.format("%s_%s/top", prefix, stage.name));
				icons[2] = reg.registerIcon(String.format("%s_%s/side", prefix, stage.name));
				iconMap.get(type).put(stage, icons);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private IIcon getIconByTypeAndStage(int side, EnumCheeseType type, EnumCheeseStage stage)
	{
		final IIcon[] icons = iconMap.get(type).get(stage);
		if (side == 0)
		{
			return icons[0];
		}
		else if (side == 1)
		{
			return icons[1];
		}
		else
		{
			return icons[2];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		final int meta = world.getBlockMetadata(x, y, z);
		EnumCheeseType type = EnumCheeseType.getSafeById(meta);
		EnumCheeseStage stage = type.stages.get(0);
		if (te != null)
		{
			type = te.getCheese().getType();
			stage = te.getCheese().getStage();
		}
		return getIconByTypeAndStage(side, type, stage);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		final EnumCheeseType type = EnumCheeseType.getSafeById(meta);
		return getIconByTypeAndStage(side, type, type.stages.get(0));
	}
}
