package growthcraft.api.cellar.brewing;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.HashKey;
import growthcraft.api.core.util.ItemKey;
import growthcraft.api.core.util.ItemTest;
import growthcraft.api.core.util.FluidTest;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BrewingRegistry implements IBrewingRegistry
{
	public static class BrewingKey extends HashKey
	{
		public Fluid fluid;
		public Item item;
		public int meta;

		public BrewingKey(@Nonnull Fluid flu, @Nonnull Item ite, int met)
		{
			super();
			this.fluid = flu;
			this.item = ite;
			this.meta = met;
			generateHashCode();
		}

		public BrewingKey(@Nonnull Fluid flu, @Nonnull ItemStack stack)
		{
			this(flu, stack.getItem(), stack.getItemDamage());
		}

		public void generateHashCode()
		{
			this.hash = fluid.hashCode();
			this.hash = 31 * hash + item.hashCode();
			this.hash = 31 * hash + meta;
		}
	}

	private HashMap<BrewingKey, BrewingRecipe> brewingList = new HashMap<BrewingKey, BrewingRecipe>();
	private List<ItemKey> brewingIngredients = new ArrayList<ItemKey>();
	private ILogger logger = NullLogger.INSTANCE;

	private Fluid boozeToKey(Fluid f)
	{
		return CellarRegistry.instance().booze().maybeAlternateBooze(f);
	}

	@Override
	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addBrewing(@Nonnull FluidStack sourceFluid, @Nonnull ItemStack raw, @Nonnull FluidStack resultFluid, int time, @Nonnull Residue residue)
	{
		this.brewingList.put(new BrewingKey(sourceFluid.getFluid(), raw), new BrewingRecipe(sourceFluid, raw, resultFluid, time, residue));
		this.brewingIngredients.add(new ItemKey(raw));
	}

	@Override
	public BrewingRecipe getBrewingRecipe(FluidStack fluidstack, ItemStack itemstack)
	{
		if (itemstack == null || fluidstack == null) return null;

		final Fluid f = boozeToKey(fluidstack.getFluid());
		BrewingRecipe ret = brewingList.get(new BrewingKey(f, itemstack.getItem(), itemstack.getItemDamage()));
		if (ret == null) ret = brewingList.get(new BrewingKey(f, itemstack.getItem(), ItemKey.WILDCARD_VALUE));

		if (ret != null)
		{
			if (!ItemTest.hasEnough(ret.getInputItemStack(), itemstack)) return null;
			if (!FluidTest.hasEnough(ret.getInputFluidStack(), fluidstack)) return null;
		}
		return ret;
	}

	@Override
	public boolean isBrewingRecipe(FluidStack fluidstack, ItemStack itemstack)
	{
		return getBrewingRecipe(fluidstack, itemstack) != null;
	}

	@Override
	public boolean isItemBrewingIngredient(ItemStack itemstack)
	{
		if (itemstack == null) return false;

		return brewingIngredients.contains(new ItemKey(itemstack)) ||
			brewingIngredients.contains(new ItemKey(itemstack.getItem(), ItemKey.WILDCARD_VALUE));
	}
}
