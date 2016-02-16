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

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.core.util.FluidTest;
import growthcraft.api.core.util.FluidUtils;
import growthcraft.api.core.util.ItemTest;
import growthcraft.api.milk.cheesevat.ICheeseVatRecipe;
import growthcraft.api.milk.MilkFluidTags;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.api.milk.util.MilkTest;
import growthcraft.core.util.ItemUtils;
import growthcraft.core.common.tileentity.GrcTileEntityDeviceBase;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.tileentity.IItemHandler;
import growthcraft.milk.GrowthCraftMilk;
import growthcraft.milk.common.item.EnumCheeseType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityCheeseVat extends GrcTileEntityDeviceBase implements IItemHandler
{
	public static enum FluidTankType
	{
		PRIMARY,
		RENNET,
		WHEY,
		RECIPE;

		public final int id;

		private FluidTankType()
		{
			this.id = ordinal();
		}
	}

	private static FluidTankType[] recipeTanks = { FluidTankType.PRIMARY, FluidTankType.RECIPE };
	private static int[][] accessibleSlots = {
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 },
		{ 0 }
	};

	private boolean recheckRecipe;

	public void markForRecipeCheck()
	{
		this.recheckRecipe = true;
	}

	@Override
	public void onInventoryChanged(IInventory inv, int index)
	{
		super.onInventoryChanged(inv, index);
		markForRecipeCheck();
	}

	@Override
	protected FluidTank[] createTanks()
	{
		return new FluidTank[] {
			// milk
			new FluidTank(5000),
			// rennet
			new FluidTank(333),
			// whey
			new FluidTank(1000),
			// recipe fluid
			new FluidTank(1000)
		};
	}

	public int getVatFluidCapacity()
	{
		return getFluidTank(FluidTankType.PRIMARY.id).getCapacity() +
			getFluidTank(FluidTankType.WHEY.id).getCapacity() +
			getFluidTank(FluidTankType.RECIPE.id).getCapacity();
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 3, 1);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grcmilk.CheeseVat";
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

	private void commitRecipe()
	{
		GrowthCraftMilk.getLogger().info("Commiting Recipe");
		if (FluidTest.hasTags(getFluidStack(0), MilkFluidTags.MILK_CURDS))
		{
			GrowthCraftMilk.getLogger().info("Fluid appears to be curds");
			final List<FluidStack> fluids = new ArrayList<FluidStack>();
			final List<ItemStack> items = new ArrayList<ItemStack>();
			for (FluidTankType t : recipeTanks)
			{
				final FluidStack stack = getFluidStack(t.id);
				if (FluidTest.isValid(stack)) fluids.add(stack);
			}

			for (int i = 0; i < getSizeInventory(); ++i)
			{
				final ItemStack stack = getStackInSlot(i);
				if (stack == null) break;
				items.add(stack);
			}

			final ICheeseVatRecipe recipe = MilkRegistry.instance().cheeseVat().findRecipe(fluids, items);
			if (recipe != null)
			{
				GrowthCraftMilk.getLogger().info("Recipe %s was found", recipe);
				final List<ItemStack> inputItems = recipe.getInputItemStacks();
				final List<FluidStack> inputFluids = recipe.getInputFluidStacks();
				final int[] invSlots = InventoryProcessor.instance().findItemSlots(this, inputItems);
				if (InventoryProcessor.instance().checkSlots(this, inputItems, invSlots))
				{
					GrowthCraftMilk.getLogger().info("Inventory Slots are valid");
					if (FluidTest.hasEnoughAndExpected(inputFluids, fluids))
					{
						GrowthCraftMilk.getLogger().info("Fluids are valid");
						InventoryProcessor.instance().consumeItemsInSlots(this, inputItems, invSlots);
						for (int fluidIndex = 0; fluidIndex < fluids.size(); ++fluidIndex)
						{
							final FluidStack fluidStack = fluids.get(fluidIndex);
							final FluidTankType t = recipeTanks[fluidIndex];
							if (fluidStack != null)
							{
								drainFluidTank(t.id, fluidStack.amount, true);
							}
						}
						GrowthCraftMilk.getLogger().info("Spawning items on tile");
						for (ItemStack stack : recipe.getOutputItemStacks())
						{
							if (stack != null)
							{
								ItemUtils.spawnItemStackAtTile(stack.copy(), this, worldObj.rand);
							}
						}
						int tankIndex = 0;
						for (FluidStack stack : recipe.getOutputFluidStacks())
						{
							if (stack != null)
							{
								fillFluidTank(tankIndex, stack.copy(), true);
							}
							// Currently the cheese vat does not support more than 1 fluid output.
							tankIndex++;
							break;
						}
						markForBlockUpdate();
					}
				}
			}
			else
			{
				GrowthCraftMilk.getLogger().info("No recipe found.");
			}
		}
	}

	@Override
	protected void updateDevice()
	{
		if (recheckRecipe)
		{
			this.recheckRecipe = false;
			commitRecipe();
		}
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, int amount, boolean doDrain)
	{
		return drainFluidTank(FluidTankType.WHEY.id, amount, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean doDrain)
	{
		if (!FluidTest.areStacksEqual(getFluidStack(FluidTankType.WHEY.id), stack)) return null;
		return doDrain(dir, stack.amount, doDrain);
	}

	@Override
	protected int doFill(ForgeDirection dir, FluidStack stack, boolean doFill)
	{
		int result = 0;

		if (MilkTest.isMilk(stack))
		{
			result = fillFluidTank(FluidTankType.PRIMARY.id, stack, doFill);
		}
		else if (FluidTest.isValidAndExpected(GrowthCraftMilk.fluids.rennet.getFluid(), stack))
		{
			result = fillFluidTank(FluidTankType.RENNET.id, stack, doFill);
		}
		else if (MilkRegistry.instance().cheeseVat().isFluidIngredient(stack))
		{
			result = fillFluidTank(FluidTankType.RECIPE.id, stack, doFill);
		}
		return result;
	}

	private boolean doSwordActivation(EntityPlayer player, ItemStack stack)
	{
		final FluidStack milkStack = getFluidStack(FluidTankType.PRIMARY.id);
		if (!FluidTest.hasTags(milkStack, MilkFluidTags.MILK)) return false;
		if (milkStack.amount < 5000) return false;

		final FluidStack rennetStack = getFluidStack(FluidTankType.RENNET.id);
		if (!FluidTest.hasTags(rennetStack, MilkFluidTags.RENNET)) return false;
		if (rennetStack.amount < 333) return false;

		setFluidStack(FluidTankType.PRIMARY.id, FluidUtils.exchangeFluid(milkStack, GrowthCraftMilk.fluids.curds.getFluid()));
		clearTank(FluidTankType.RENNET.id);
		fillFluidTank(FluidTankType.WHEY.id, GrowthCraftMilk.fluids.whey.fluid.asFluidStack(1000), true);
		return true;
	}

	private boolean collectCurdInCheeseCloth(EntityPlayer player, ItemStack stack)
	{
		final FluidStack fluidStack = getFluidStack(FluidTankType.PRIMARY.id);
		if (FluidTest.hasTags(fluidStack, MilkFluidTags.CHEESE))
		{
			final Fluid fluid = fluidStack.getFluid();
			final EnumCheeseType type = GrowthCraftMilk.fluids.fluidToCheeseType.get(fluid);

			if (type != null)
			{
				clearTank(FluidTankType.PRIMARY.id);
				ItemUtils.decrPlayerCurrentInventorySlot(player, 1);
				final ItemStack curdItemStack = type.asCurdItemStack();
				ItemUtils.addStackToPlayer(curdItemStack, player, false);
				return true;
			}
		}
		return false;
	}

	private boolean addItemIngredient(EntityPlayer player, ItemStack stack)
	{
		final int slot = InventoryProcessor.instance().findNextEmpty(this);
		if (slot == -1) return false;
		final ItemStack result = ItemUtils.decrPlayerCurrentInventorySlot(player, 1);
		setInventorySlotContents(slot, result);
		return true;
	}

	@Override
	public boolean tryPlaceItem(EntityPlayer player, ItemStack stack)
	{
		if (!ItemTest.isValid(stack)) return false;
		if (stack.getItem() instanceof ItemSword)
		{
			return doSwordActivation(player, stack);
		}
		else if (GrowthCraftMilk.items.cheeseCloth.equals(stack.getItem()))
		{
			return collectCurdInCheeseCloth(player, stack);
		}
		else if (MilkRegistry.instance().cheeseVat().isItemIngredient(stack))
		{
			return addItemIngredient(player, stack);
		}
		return false;
	}

	@Override
	public boolean tryTakeItem(EntityPlayer player, ItemStack onHand)
	{
		if (onHand == null)
		{
			final int slot = InventoryProcessor.instance().findNextPresentFromEnd(this);
			if (slot == -1) return false;
			final ItemStack stack = InventoryProcessor.instance().yankSlot(this, slot);
			//ItemUtils.addStackToPlayer(stack, player, false);
			ItemUtils.spawnItemStackAtEntity(stack, player, worldObj.rand);
			return true;
		}
		return false;
	}

	@Override
	protected void markForFluidUpdate()
	{
		markForBlockUpdate();
		markForRecipeCheck();
	}
}
