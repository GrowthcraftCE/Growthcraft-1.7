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
package growthcraft.milk.common.tileentity;

import java.io.IOException;

import growthcraft.api.core.item.ItemTest;
import growthcraft.api.milk.cheesepress.ICheesePressRecipe;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.device.DeviceInventorySlot;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.GrcTileEntityInventoryBase;
import growthcraft.core.common.tileentity.IItemHandler;
import growthcraft.core.common.tileentity.ITileProgressiveDevice;
import growthcraft.core.util.ItemUtils;
import growthcraft.milk.common.item.ItemBlockHangingCurds;
import growthcraft.milk.GrowthCraftMilk;

import io.netty.buffer.ByteBuf;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.inventory.IInventory;

public class TileEntityCheesePress extends GrcTileEntityInventoryBase implements IItemHandler, ITileProgressiveDevice
{
	private static int[][] accessibleSlots = {
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 }
	};

	@SideOnly(Side.CLIENT)
	public float animProgress;
	@SideOnly(Side.CLIENT)
	public int animDir;

	private int screwState;
	private DeviceInventorySlot invSlot = new DeviceInventorySlot(this, 0);
	private int time;
	private boolean needRecipeRecheck = true;
	private ICheesePressRecipe workingRecipe;

	public void markForRecipeCheck()
	{
		this.needRecipeRecheck = true;
	}

	private void setupWorkingRecipe()
	{
		final ICheesePressRecipe recipe = MilkRegistry.instance().cheesePress().findRecipe(invSlot.get());
		if (recipe != workingRecipe)
		{
			if (workingRecipe != null)
			{
				this.time = 0;
			}
			this.workingRecipe = recipe;
		}
	}

	protected ICheesePressRecipe getWorkingRecipe()
	{
		if (workingRecipe == null)
		{
			setupWorkingRecipe();
		}
		return workingRecipe;
	}

	@Override
	public float getDeviceProgress()
	{
		final ICheesePressRecipe recipe = getWorkingRecipe();
		if (recipe != null)
		{
			return (float)time / (float)recipe.getTimeMax();
		}
		return 0.0f;
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		final ICheesePressRecipe recipe = getWorkingRecipe();
		if (recipe != null)
		{
			return time * scale / recipe.getTimeMax();
		}
		return 0;
	}

	public boolean isPressed()
	{
		return screwState == 1;
	}

	public boolean isUnpressed()
	{
		return screwState == 0;
	}

	@Override
	public void onInventoryChanged(IInventory inv, int index)
	{
		markForRecipeCheck();
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 1, 1);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grcmilk.CheesePress";
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessibleSlots[side];
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	private void updateEntityClient()
	{
		final float step = 1.0f / 20.0f;
		if (isUnpressed())
		{
			this.animDir = -1;
		}
		else
		{
			this.animDir = 1;
		}

		if (animDir > 0 && animProgress < 1.0f || animDir < 0 && animProgress > 0)
		{
			this.animProgress = MathHelper.clamp_float(this.animProgress + step * animDir, 0.0f, 1.0f);
		}
	}

	private void commitRecipe()
	{
		final ICheesePressRecipe recipe = getWorkingRecipe();
		if (recipe != null)
		{
			invSlot.set(recipe.getOutputItemStack().copy());
			this.workingRecipe = null;
		}
	}

	private void updateEntityServer()
	{
		if (needRecipeRecheck)
		{
			needRecipeRecheck = false;
			setupWorkingRecipe();
		}

		final ICheesePressRecipe recipe = getWorkingRecipe();
		if (recipe != null)
		{
			// work can only progress if the cheese press is active.
			if (isPressed())
			{
				if (time < recipe.getTimeMax())
				{
					time++;
				}
				else
				{
					this.time = 0;
					commitRecipe();
				}
			}
			// otherwise the cheese press will lose its progress?
			else
			{
				if (time > 0) time--;
			}
		}
		else
		{
			if (time != 0)
			{
				this.time = 0;
				markForBlockUpdate();
			}
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (worldObj.isRemote)
		{
			updateEntityClient();
		}
		else
		{
			updateEntityServer();
		}
	}

	public void toggle()
	{
		if (screwState == 0)
		{
			this.screwState = 1;
		}
		else
		{
			this.screwState = 0;
		}
		markForBlockUpdate();
	}

	@Override
	public boolean tryPlaceItem(EntityPlayer player, ItemStack stack)
	{
		if (ItemTest.isValid(stack))
		{
			// Items cannot be added if the slot already has an item AND
			// the press is active
			if (invSlot.isEmpty() && isUnpressed())
			{
				if (GrowthCraftMilk.blocks.hangingCurds.getItem() == stack.getItem())
				{
					final ItemBlockHangingCurds item = (ItemBlockHangingCurds)stack.getItem();
					if (item.isDried(stack))
					{
						final ItemStack result = ItemUtils.decrPlayerCurrentInventorySlot(player, 1);
						invSlot.set(result);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean tryTakeItem(EntityPlayer player, ItemStack onHand)
	{
		if (!ItemTest.isValid(onHand))
		{
			// Items cannot be removed if the cheese press is active
			if (isUnpressed())
			{
				final ItemStack result = invSlot.yank();
				if (result != null)
				{
					ItemUtils.spawnItemStackAtTile(result, this, worldObj.rand);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.screwState = nbt.getInteger("screw_state");
		this.time = nbt.getInteger("time");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("screw_state", screwState);
		nbt.setInteger("time", time);
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_CheesePress(ByteBuf stream) throws IOException
	{
		this.screwState = stream.readInt();
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_CheesePress(ByteBuf stream) throws IOException
	{
		stream.writeInt(screwState);
		return false;
	}
}
