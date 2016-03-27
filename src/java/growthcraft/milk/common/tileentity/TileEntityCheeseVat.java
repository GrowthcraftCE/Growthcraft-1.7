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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.netty.buffer.ByteBuf;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.api.core.item.ItemTest;
import growthcraft.api.core.nbt.NBTStringTagList;
import growthcraft.api.core.stream.StreamUtils;
import growthcraft.api.milk.cheesevat.ICheeseVatRecipe;
import growthcraft.api.milk.MilkFluidTags;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.cellar.common.tileentity.component.TileHeatingComponent;
import growthcraft.core.common.inventory.AccesibleSlots;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.tileentity.device.DeviceFluidSlot;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.GrcTileEntityDeviceBase;
import growthcraft.core.common.tileentity.IItemHandler;
import growthcraft.core.common.tileentity.ITileHeatedDevice;
import growthcraft.core.common.tileentity.ITileNamedFluidTanks;
import growthcraft.core.common.tileentity.ITileProgressiveDevice;
import growthcraft.core.util.ItemUtils;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.tileentity.cheesevat.CheeseVatState;
import growthcraft.milk.event.EventCheeseVat.EventCheeseVatMadeCheeseFluid;
import growthcraft.milk.event.EventCheeseVat.EventCheeseVatMadeCurds;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityCheeseVat extends GrcTileEntityDeviceBase implements IItemHandler, ITileHeatedDevice, ITileNamedFluidTanks, ITileProgressiveDevice
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
	private static AccesibleSlots accessibleSlots = new AccesibleSlots(new int[][]{
		{ 0, 1, 2 },
		{ 0, 1, 2 },
		{ 0, 1, 2 },
		{ 0, 1, 2 },
		{ 0, 1, 2 },
		{ 0, 1, 2 }
	});

	private DeviceFluidSlot primaryFluidSlot = new DeviceFluidSlot(this, FluidTankType.PRIMARY.id);
	private DeviceFluidSlot rennetFluidSlot = new DeviceFluidSlot(this, FluidTankType.RENNET.id);
	private DeviceFluidSlot wasteFluidSlot = new DeviceFluidSlot(this, FluidTankType.WASTE.id);
	private boolean recheckRecipe;
	private TileHeatingComponent heatComponent = new TileHeatingComponent(this, 0.5f);
	private CheeseVatState vatState = CheeseVatState.IDLE;
	private float progress;
	private int progressMax;

	public boolean isIdle()
	{
		return vatState == CheeseVatState.IDLE;
	}

	public boolean isWorking()
	{
		return !isIdle();
	}

	private void setVatState(CheeseVatState state)
	{
		this.vatState = state;
		markForBlockUpdate();
	}

	private void goIdle()
	{
		setVatState(CheeseVatState.IDLE);
	}

	private void setupProgress(int value)
	{
		this.progress = 0;
		this.progressMax = value;
	}

	private void resetProgress()
	{
		setupProgress(0);
	}

	@Override
	public float getDeviceProgress()
	{
		if (progressMax > 0)
		{
			return progress / (float)progressMax;
		}
		return 0.0f;
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		if (progressMax > 0)
		{
			return (int)(progress * scale / progressMax);
		}
		return 0;
	}

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

	@Override
	public int getHeatScaled(int scale)
	{
		return (int)(scale * MathHelper.clamp_float(getHeatMultiplier(), 0f, 1f));
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
			new FluidTank(GrowthCraftMilk.getConfig().cheeseVatPrimaryTankCapacity),
			// rennet
			new FluidTank(GrowthCraftMilk.getConfig().cheeseVatRennetTankCapacity),
			// waste
			new FluidTank(GrowthCraftMilk.getConfig().cheeseVatWasteTankCapacity),
			// recipe fluid
			new FluidTank(GrowthCraftMilk.getConfig().cheeseVatRecipeTankCapacity)
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
		return accessibleSlots.slotsAt(side);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return accessibleSlots.sideContains(side, index);
	}

	private boolean activateCurdTransition(boolean checkOnly)
	{
		final ItemStack starterCultureStack = GrowthCraftMilk.items.starterCulture.asStack();
		final int slot = InventoryProcessor.instance().findItemSlot(this, starterCultureStack);
		if (slot < 0)
		{
			GrowthCraftMilk.getLogger().info("No Starter Culture found!");
			return false;
		}

		final FluidStack milkStack = primaryFluidSlot.get();
		if (!FluidTest.hasTags(milkStack, MilkFluidTags.MILK))
		{
			GrowthCraftMilk.getLogger().info("Primary Fluid is NOT milk.");
			return false;
		}
		if (!primaryFluidSlot.isFull())
		{
			GrowthCraftMilk.getLogger().info("Primary Fluid Tank is NOT full.");
			return false;
		}

		final FluidStack rennetStack = rennetFluidSlot.get();
		if (!FluidTest.hasTags(rennetStack, MilkFluidTags.RENNET))
		{
			GrowthCraftMilk.getLogger().info("Rennet contains NON rennet fluid.");
			return false;
		}
		if (!rennetFluidSlot.isFull())
		{
			GrowthCraftMilk.getLogger().info("Rennet Fluid Tank is NOT full.");
			return false;
		}

		if (!checkOnly)
		{
			decrStackSize(slot, 1);
			primaryFluidSlot.set(FluidUtils.exchangeFluid(milkStack, GrowthCraftMilk.fluids.curds.getFluid()));
			rennetFluidSlot.clear();
			wasteFluidSlot.fill(GrowthCraftMilk.fluids.whey.fluid.asFluidStack(GrowthCraftMilk.getConfig().cheeseVatMilkToCurdsWheyAmount), true);
			GrowthCraftMilk.MILK_BUS.post(new EventCheeseVatMadeCurds(this));
		}
		return true;
	}

	private boolean activateWheyTransition(boolean checkOnly)
	{
		final FluidStack milkStack = primaryFluidSlot.get();
		if (FluidTest.hasTags(milkStack, MilkFluidTags.WHEY) && primaryFluidSlot.isFull())
		{
			if (!checkOnly)
			{
				final Fluid fluid = GrowthCraftMilk.fluids.cheeses.get(EnumCheeseType.RICOTTA).getFluid();
				primaryFluidSlot.set(FluidUtils.exchangeFluid(primaryFluidSlot.get(), fluid));
				wasteFluidSlot.fill(GrowthCraftMilk.fluids.whey.fluid.asFluidStack(GrowthCraftMilk.getConfig().cheeseVatWheyToRicottaWheyAmount), true);
				GrowthCraftMilk.MILK_BUS.post(new EventCheeseVatMadeCheeseFluid(this));
			}
			return true;
		}
		return false;
	}

	private boolean commitMilkCurdRecipe(boolean checkOnly)
	{
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
			final List<ItemStack> inputItems = recipe.getInputItemStacks();
			final List<FluidStack> inputFluids = recipe.getInputFluidStacks();
			final int[] invSlots = InventoryProcessor.instance().findItemSlots(this, inputItems);
			if (InventoryProcessor.instance().checkSlots(this, inputItems, invSlots))
			{
				if (FluidTest.hasEnoughAndExpected(inputFluids, fluids))
				{
					if (!checkOnly)
					{
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
							tankIndex++;
							// Currently the cheese vat does not support more than 1 fluid output.
							break;
						}
						markForBlockUpdate();
						GrowthCraftMilk.MILK_BUS.post(new EventCheeseVatMadeCheeseFluid(this));
					}
					return true;
				}
			}
		}
		return false;
	}

	private void commitRecipe()
	{
		final FluidStack stack = primaryFluidSlot.get();
		if (FluidTest.hasTags(stack, MilkFluidTags.MILK_CURDS))
		{
			if (commitMilkCurdRecipe(true))
			{
				setupProgress(GrowthCraftMilk.getConfig().cheeseVatCheeseTime);
				setVatState(CheeseVatState.PREPARING_CHEESE);
			}
		}
	}

	private void onFinishedProgress()
	{
		switch (vatState)
		{
			case PREPARING_RICOTTA:
				activateWheyTransition(false);
				break;
			case PREPARING_CURDS:
				activateCurdTransition(false);
				break;
			case PREPARING_CHEESE:
				commitMilkCurdRecipe(false);
				break;
			default:
		}
		resetProgress();
	}

	@Override
	protected void updateDevice()
	{
		heatComponent.update();
		if (!isIdle())
		{
			if (isHeated())
			{
				if (progress < progressMax)
				{
					progress += 1 * getHeatMultiplier();
				}
				else
				{
					onFinishedProgress();
					goIdle();
				}
			}
			else
			{
				if (progress > 0)
				{
					progress -= 1;
				}
				else
				{
					goIdle();
				}
			}
		}
		else
		{
			if (recheckRecipe)
			{
				this.recheckRecipe = false;
				if (isHeated()) commitRecipe();
			}
		}
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
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return FluidTest.hasTags(fluid, MilkFluidTags.MILK) ||
			FluidTest.hasTags(fluid, MilkFluidTags.WHEY) ||
			FluidTest.hasTags(fluid, MilkFluidTags.RENNET) ||
			MilkRegistry.instance().cheeseVat().isFluidIngredient(fluid);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, int amount, boolean doDrain)
	{
		if (!isIdle()) return null;
		return wasteFluidSlot.consume(amount, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean doDrain)
	{
		if (!FluidTest.areStacksEqual(wasteFluidSlot.get(), stack)) return null;
		return doDrain(dir, stack.amount, doDrain);
	}

	@Override
	protected int doFill(ForgeDirection dir, FluidStack stack, boolean doFill)
	{
		if (!isIdle()) return 0;
		int result = 0;

		if (FluidTest.hasTags(stack, MilkFluidTags.MILK) || FluidTest.hasTags(stack, MilkFluidTags.WHEY))
		{
			result = primaryFluidSlot.fill(stack, doFill);
		}
		else if (FluidTest.isValidAndExpected(GrowthCraftMilk.fluids.rennet.getFluid(), stack))
		{
			if (primaryTankHasMilk())
			{
				result = rennetFluidSlot.fill(stack, doFill);
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

	private void playSuccessfulSwordActivationFX()
	{
		worldObj.playSoundEffect((double)xCoord, (double)yCoord, (double)zCoord, "random.successful_hit", 0.3f, 0.5f);
	}

	private boolean doSwordActivation(EntityPlayer _player, ItemStack _stack)
	{
		if (!isHeated())
		{
			GrowthCraftMilk.getLogger().info("Vat is NOT heated.");
			return false;
		}
		GrowthCraftMilk.getLogger().info("Activating Using Sword.");
		final FluidStack milkStack = primaryFluidSlot.get();
		if (FluidTest.hasTags(milkStack, MilkFluidTags.MILK))
		{
			GrowthCraftMilk.getLogger().info("Activating Curd Transition.");
			if (activateCurdTransition(true))
			{
				setupProgress(GrowthCraftMilk.getConfig().cheeseVatCurdTime);
				setVatState(CheeseVatState.PREPARING_CURDS);
				playSuccessfulSwordActivationFX();
				return true;
			}
		}
		else if (FluidTest.hasTags(milkStack, MilkFluidTags.WHEY))
		{
			GrowthCraftMilk.getLogger().info("Activating Whey Transition.");
			if (activateWheyTransition(true))
			{
				setupProgress(GrowthCraftMilk.getConfig().cheeseVatWheyTime);
				setVatState(CheeseVatState.PREPARING_RICOTTA);
				playSuccessfulSwordActivationFX();
				return true;
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
		if (!isIdle()) return false;
		if (!ItemTest.isValid(stack)) return false;
		final Item item = stack.getItem();
		if (item instanceof ItemSword)
		{
			return doSwordActivation(player, stack);
		}
		else if (GrowthCraftMilk.items.starterCulture.equals(item))
		{
			return addItemIngredient(player, stack);
		}
		else if (GrowthCraftMilk.items.cheeseCloth.equals(item))
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
		if (!isIdle()) return false;
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

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (nbt.hasKey("progress_max"))
		{
			this.progressMax = nbt.getInteger("progress_max");
		}
		if (nbt.hasKey("progress"))
		{
			this.progress = nbt.getFloat("progress");
		}
		heatComponent.readFromNBT(nbt, "heat_component");
		this.vatState = CheeseVatState.getStateSafe(nbt.getString("vat_state"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("progress_max", progressMax);
		nbt.setFloat("progress", progress);
		heatComponent.writeToNBT(nbt, "heat_component");
		nbt.setString("vat_state", vatState.name);
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_CheeseVat(ByteBuf stream) throws IOException
	{
		this.progressMax = stream.readInt();
		this.progress = stream.readFloat();
		heatComponent.readFromStream(stream);
		String name = "idle";
		try
		{
			name = StreamUtils.readStringASCII(stream);
		}
		catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
		}
		this.vatState = CheeseVatState.getStateSafe(name);
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_CheeseVat(ByteBuf stream) throws IOException
	{
		stream.writeInt(progressMax);
		stream.writeFloat(progress);
		heatComponent.writeToStream(stream);
		try
		{
			StreamUtils.writeStringASCII(stream, vatState.name);
		}
		catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
}
