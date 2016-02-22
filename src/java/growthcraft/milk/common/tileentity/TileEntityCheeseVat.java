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
import java.util.Locale;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.api.core.item.ItemTest;
import growthcraft.api.core.nbt.NBTStringTagList;
import growthcraft.api.milk.cheesevat.ICheeseVatRecipe;
import growthcraft.api.milk.MilkFluidTags;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.api.milk.util.MilkTest;
import growthcraft.cellar.common.tileentity.component.HeatBlockComponent;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.tileentity.device.DeviceFluidSlot;
import growthcraft.core.common.tileentity.GrcTileEntityDeviceBase;
import growthcraft.core.common.tileentity.IItemHandler;
import growthcraft.core.common.tileentity.ITileHeatedDevice;
import growthcraft.core.common.tileentity.ITileNamedFluidTanks;
import growthcraft.core.util.ItemUtils;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityCheeseVat extends GrcTileEntityDeviceBase implements IItemHandler, ITileHeatedDevice, ITileNamedFluidTanks
{
	public static enum FluidTankType
	{
		PRIMARY,
		RENNET,
		WASTE,
		RECIPE;

		public static final FluidTankType[] VALUES = new FluidTankType[] { PRIMARY, RENNET, WASTE, RECIPE };
		public final int id;
		public final String name;

		private FluidTankType()
		{
			this.id = ordinal();
			this.name = name().toLowerCase(Locale.ENGLISH);
		}

		public String getUnlocalizedName()
		{
			return "grcmilk.cheese_vat.fluid_tank." + name;
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
	private HeatBlockComponent heatComponent = new HeatBlockComponent(this);
	private DeviceFluidSlot primaryFluidSlot = new DeviceFluidSlot(this, FluidTankType.PRIMARY.id);
	private DeviceFluidSlot wasteFluidSlot = new DeviceFluidSlot(this, FluidTankType.WASTE.id);

	@Override
	public boolean isHeated()
	{
		return heatComponent.getHeatMultiplier() > 0;
	}

	@Override
	public float getHeatMultiplier()
	{
		return heatComponent.getHeatMultiplier();
	}

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
			getFluidTank(FluidTankType.WASTE.id).getCapacity() +
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

	private void commitMilkCurdRecipe()
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

	private void commitRecipe()
	{
		GrowthCraftMilk.getLogger().info("Commiting Recipe");

		final FluidStack stack = primaryFluidSlot.get();
		if (FluidTest.hasTags(stack, MilkFluidTags.MILK_CURDS))
		{
			commitMilkCurdRecipe();
		}
	}

	@Override
	protected void updateDevice()
	{
		if (recheckRecipe)
		{
			this.recheckRecipe = false;
			if (isHeated()) commitRecipe();
		}
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, int amount, boolean doDrain)
	{
		return wasteFluidSlot.consume(amount, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean doDrain)
	{
		if (!FluidTest.areStacksEqual(wasteFluidSlot.get(), stack)) return null;
		return doDrain(dir, stack.amount, doDrain);
	}

	private boolean primaryTankHasMilk()
	{
		return FluidTest.hasTags(primaryFluidSlot.get(), MilkFluidTags.MILK);
	}

	private boolean primaryTankHasCurds()
	{
		return FluidTest.hasTags(primaryFluidSlot.get(), MilkFluidTags.MILK_CURDS);
	}

	@Override
	protected int doFill(ForgeDirection dir, FluidStack stack, boolean doFill)
	{
		int result = 0;

		if (MilkTest.isMilk(stack) || FluidTest.hasTags(stack, MilkFluidTags.WHEY))
		{
			result = primaryFluidSlot.fill(stack, doFill);
		}
		else if (FluidTest.isValidAndExpected(GrowthCraftMilk.fluids.rennet.getFluid(), stack))
		{
			if (primaryTankHasMilk())
			{
				result = fillFluidTank(FluidTankType.RENNET.id, stack, doFill);
			}
		}
		else if (MilkRegistry.instance().cheeseVat().isFluidIngredient(stack))
		{
			if (primaryTankHasCurds())
			{
				result = fillFluidTank(FluidTankType.RECIPE.id, stack, doFill);
			}
		}
		return result;
	}

	private boolean doSwordActivation(EntityPlayer player, ItemStack stack)
	{
		if (!isHeated()) return false;
		final FluidStack milkStack = primaryFluidSlot.get();
		if (FluidTest.hasTags(milkStack, MilkFluidTags.MILK))
		{
			if (!FluidTest.hasTags(milkStack, MilkFluidTags.MILK)) return false;
			if (!primaryFluidSlot.isFull()) return false;

			final FluidStack rennetStack = getFluidStack(FluidTankType.RENNET.id);
			if (!FluidTest.hasTags(rennetStack, MilkFluidTags.RENNET)) return false;
			if (rennetStack.amount < 333) return false;

			primaryFluidSlot.set(FluidUtils.exchangeFluid(milkStack, GrowthCraftMilk.fluids.curds.getFluid()));
			clearTank(FluidTankType.RENNET.id);
			wasteFluidSlot.fill(GrowthCraftMilk.fluids.whey.fluid.asFluidStack(1000), true);
			return true;
		}
		else if (FluidTest.hasTags(milkStack, MilkFluidTags.WHEY))
		{
			if (primaryFluidSlot.isFull())
			{
				final Fluid fluid = GrowthCraftMilk.fluids.cheeses.get(EnumCheeseType.RICOTTA).getFluid();
				primaryFluidSlot.set(FluidUtils.exchangeFluid(primaryFluidSlot.get(), fluid));
				wasteFluidSlot.fill(GrowthCraftMilk.fluids.whey.fluid.asFluidStack(1000), true);
			}
		}
		return false;
	}

	private boolean collectCurdInCheeseCloth(EntityPlayer player, ItemStack stack)
	{
		final FluidStack fluidStack = primaryFluidSlot.get();
		if (FluidTest.hasTags(fluidStack, MilkFluidTags.CHEESE))
		{
			final Fluid fluid = fluidStack.getFluid();
			final EnumCheeseType type = GrowthCraftMilk.fluids.fluidToCheeseType.get(fluid);

			if (type != null)
			{
				primaryFluidSlot.clear();
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

	@Override
	public void writeFluidTankNamesToTag(NBTTagCompound tag)
	{
		final NBTStringTagList tagList = new NBTStringTagList();
		for (FluidTankType type : FluidTankType.VALUES)
		{
			tagList.add(type.getUnlocalizedName());
		}
		tag.setTag("tank_names", tagList.getTag());
	}
}
