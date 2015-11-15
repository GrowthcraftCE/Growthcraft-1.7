package growthcraft.cellar.common.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.core.util.ItemUtils;

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
	protected List<ItemStack> tempItemList = new ArrayList<ItemStack>();
	protected Random random = new Random();
	protected IInventory inventory;
	protected World world;
	protected TileEntityCellarDevice parent;

	public YeastGenerator(TileEntityCellarDevice te)
	{
		this.parent = te;
	}

	protected void readFromNBT(NBTTagCompound data)
	{
		data.setInteger("time", time);
	}

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

	protected void writeToNBT(NBTTagCompound data)
	{
		this.time = data.getInteger("time");
	}

	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		writeToNBT(target);
		data.setTag(name, target);
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
		if (parent.isFluidTankEmpty(0)) return false;

		return CellarRegistry.instance().booze().hasTags(parent.getFluid(0), "young");
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
			getInventory().setInventorySlotContents(1, result);
		}
	}

	public void produceYeast()
	{
		if (getInventory().getStackInSlot(1) == null)
		{
			initProduceYeast();
		}
		else
		{
			final ItemStack contents = getInventory().getStackInSlot(1);
			if (CellarRegistry.instance().fermenting().canYeastFormInBiome(contents, getCurrentBiome()))
			{
				getInventory().setInventorySlotContents(1, ItemUtils.increaseStack(contents));
			}
		}
	}

	public void update()
	{
		if (canProduceYeast())
		{
			this.time++;
			if (time >= 1200)
			{
				this.time = 0;
				produceYeast();
				markForInventoryUpdate();
			}
		}
	}
}
