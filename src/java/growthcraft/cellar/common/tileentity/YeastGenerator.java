package growthcraft.cellar.common.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.core.util.ItemUtils;

import io.netty.buffer.ByteBuf;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;

public class YeastGenerator
{
	protected int time;
	protected int timeMax = 1200;
	protected int consumption = 1200 / 16;
	protected int fluidSlot;
	protected int invSlot;
	protected List<ItemStack> tempItemList = new ArrayList<ItemStack>();
	protected Random random = new Random();
	protected IInventory inventory;
	protected World world;
	protected TileEntityCellarDevice parent;

	/**
	 * @param te - parent tile entity
	 * @param fs - fluid slot id to use in parent
	 *             Fluid will be used from this slot
	 * @param is - inventory slot id to use in parent
	 *             Yeast will be generated into this slot
	 */
	public YeastGenerator(TileEntityCellarDevice te, int fs, int is)
	{
		this.parent = te;
		this.fluidSlot = fs;
		this.invSlot = is;
	}

	public int getProgressScaled(int scale)
	{
		if (timeMax <= 0) return 0;
		return this.time * scale / timeMax;
	}

	public int getTime()
	{
		return time;
	}

	public int getTimeMax()
	{
		return timeMax;
	}

	public YeastGenerator setTime(int t)
	{
		this.time = t;
		return this;
	}

	public YeastGenerator setConsumption(int t)
	{
		this.consumption = t;
		return this;
	}

	public YeastGenerator setTimeMax(int t)
	{
		this.timeMax = t;
		return this;
	}

	public World getWorld()
	{
		return parent.getWorldObj();
	}

	public IInventory getInventory()
	{
		return parent;
	}

	private void markForInventoryUpdate()
	{
		parent.markForInventoryUpdate();
	}

	public BiomeGenBase getCurrentBiome()
	{
		return getWorld().getBiomeGenForCoords(parent.xCoord, parent.zCoord);
	}

	public boolean canProduceYeast()
	{
		if (parent.getFluidAmount(0) < consumption) return false;

		return CellarRegistry.instance().booze().hasTags(parent.getFluid(fluidSlot), BoozeTag.YOUNG);
	}

	public void consumeFluid()
	{
		parent.getFluidTank(fluidSlot).drain(consumption, true);
		parent.markForBlockUpdate();
	}

	/**
	 * This is called to initialize the yeast slot, a random yeast type is
	 * chosen from the various biome types and set in the slot,
	 * any further yeast production will be of the same type.
	 */
	protected void initProduceYeast()
	{
		tempItemList.clear();
		final BiomeGenBase biome = getCurrentBiome();
		final FermentingRegistry reg = CellarRegistry.instance().fermenting();
		for (Type t : BiomeDictionary.getTypesForBiome(biome))
		{
			final List<ItemStack> yeastList = reg.getYeastListForBiomeType(t);
			if (yeastList != null)
			{
				tempItemList.addAll(yeastList);
			}
		}

		if (tempItemList.size() > 0)
		{
			final ItemStack result = tempItemList.get(random.nextInt(tempItemList.size())).copy();
			getInventory().setInventorySlotContents(invSlot, result);
			consumeFluid();
		}
	}

	public void produceYeast()
	{
		if (getInventory().getStackInSlot(invSlot) == null)
		{
			initProduceYeast();
		}
		else
		{
			final ItemStack contents = getInventory().getStackInSlot(invSlot);
			if (CellarRegistry.instance().fermenting().canYeastFormInBiome(contents, getCurrentBiome()))
			{
				getInventory().setInventorySlotContents(invSlot, ItemUtils.increaseStack(contents));
				consumeFluid();
			}
		}
	}

	public void update()
	{
		if (canProduceYeast())
		{
			this.time++;
			if (time >= timeMax)
			{
				this.time = 0;
				produceYeast();
				markForInventoryUpdate();
			}
		}
		else
		{
			if (this.time != 0)
			{
				this.time = 0;
				markForInventoryUpdate();
			}
		}
	}

	/**
	 * Serialization
	 */

	/**
	 * @param data - nbt data to read from
	 */
	protected void readFromNBT(NBTTagCompound data)
	{
		this.time = data.getInteger("time");
		//this.timeMax = data.getInteger("timeMax");
	}

	/**
	 * @param data - parent nbt data to read from
	 * @param name - sub tag to read
	 */
	public void readFromNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound list = data.getCompoundTag(name);
		if (list != null)
		{
			readFromNBT(list);
		}
		else
		{
			// LOG error
		}
	}

	/**
	 * @param buf - buffer to read from
	 */
	public void readFromStream(ByteBuf buf)
	{
		this.time = buf.readInt();
		//this.timeMax = buf.readInt();
	}

	/**
	 * @param data - nbt to write to
	 */
	protected void writeToNBT(NBTTagCompound data)
	{
		data.setInteger("time", time);
		//data.setInteger("timeMax", timeMax);
	}

	/**
	 * @param data - nbt to write to
	 * @param name - sub tag nbt to write to
	 */
	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		writeToNBT(target);
		data.setTag(name, target);
	}

	/**
	 * @param buf - buffer to write to
	 */
	public void writeToStream(ByteBuf buf)
	{
		buf.writeInt(time);
		//buf.writeInt(timeMax);
	}
}
