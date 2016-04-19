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
package growthcraft.core.common.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import buildcraft.api.tools.IToolWrench;

import growthcraft.api.core.item.EnumDye;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemCrowbar extends GrcItemBase implements IToolWrench
{
	private final Set<Class<? extends Block>> shiftRotations = new HashSet<Class<? extends Block>>();

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public ItemCrowbar()
	{
		super();
		setFull3D();
		setMaxStackSize(1);
		setHasSubtypes(true);
		shiftRotations.add(BlockLever.class);
		shiftRotations.add(BlockButton.class);
		shiftRotations.add(BlockChest.class);
		setHarvestLevel("wrench", 0);
		setUnlocalizedName("grccore.crowbar");
		setTextureName("grccore:crowbar");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	private boolean isShiftRotation(Class<? extends Block> cls)
	{
		for (Class<? extends Block> shift : shiftRotations)
		{
			if (shift.isAssignableFrom(cls)) return true;
		}
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		final Block block = world.getBlock(x, y, z);
		if (block == null) return false;
		if (player.isSneaking() != isShiftRotation(block.getClass())) return false;
		if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)))
		{
			player.swingItem();
			return !world.isRemote;
		}
		return false;
	}

	@Override
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z)
	{
		player.swingItem();
	}

	public EnumDye getDye(ItemStack stack)
	{
		return EnumDye.getByMeta(stack.getItemDamage());
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + getDye(stack).name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.icons = new IIcon[EnumDye.VALUES.length];
		for (EnumDye dye : EnumDye.VALUES)
		{
			this.icons[dye.meta] = reg.registerIcon(String.format("%s/%s", getIconString(), dye.name));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (EnumDye dye : EnumDye.VALUES)
		{
			list.add(new ItemStack(item, 1, dye.meta));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return icons[meta];
	}
}
