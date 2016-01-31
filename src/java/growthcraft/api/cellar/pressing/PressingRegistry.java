package growthcraft.api.cellar.pressing;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.ItemKey;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class PressingRegistry implements IPressingRegistry
{
	private ILogger logger = NullLogger.INSTANCE;
	private Map<ItemKey, PressingRecipe> pressingList = new HashMap<ItemKey, PressingRecipe>();

	@Override
	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addPressingRecipe(@Nonnull ItemStack stack, @Nonnull FluidStack resultFluid, int time, @Nullable Residue residue)
	{
		final ItemKey key = new ItemKey(stack);
		final PressingRecipe result = new PressingRecipe(stack, resultFluid, time, residue);
		pressingList.put(key, result);
		logger.debug("Added new Pressing Recipe key={%s} result={%s}", key, result);
	}

	@Override
	public PressingRecipe getPressingRecipe(ItemStack itemstack)
	{
		if (itemstack == null) return null;

		final PressingRecipe ret = pressingList.get(new ItemKey(itemstack));
		if (ret != null) return ret;

		return pressingList.get(new ItemKey(itemstack.getItem(), ItemKey.WILDCARD_VALUE));
	}

	@Override
	public boolean hasPressingRecipe(ItemStack itemstack)
	{
		return this.getPressingRecipe(itemstack) != null;
	}
}
