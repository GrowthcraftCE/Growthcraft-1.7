package growthcraft.api.core.fluids;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils
{
	private FluidUtils() {}

	public static FluidStack drainFluidBlock(World world, int x, int y, int z, boolean doDrain)
	{
		final Block block = world.getBlock(x, y, z);
		if (block instanceof BlockFluidBase)
		{
			final BlockFluidBase bfb = (BlockFluidBase)block;
			return bfb.drain(world, x, y, z, doDrain);
		}
		else if (block == Blocks.lava)
		{
			if (doDrain) world.setBlockToAir(x, y, z);
			return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
		}
		else if (block == Blocks.water)
		{
			if (doDrain) world.setBlockToAir(x, y, z);
			return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
		}
		return null;
	}

	public static List<Fluid> getFluidsByNames(List<String> names)
	{
		final List<Fluid> fluids = new ArrayList<Fluid>();
		for (String name : names)
		{
			fluids.add(FluidRegistry.getFluid(name));
		}
		return fluids;
	}

	public static FluidStack exchangeFluid(FluidStack stack, Fluid newFluid)
	{
		return new FluidStack(newFluid, stack.amount);
	}

	public static boolean doesFluidExist(String name)
	{
		return FluidRegistry.getFluid(name) != null && FluidRegistry.isFluidRegistered(name);
	}

	public static boolean doesFluidExist(Fluid fluid)
	{
		return fluid != null && FluidRegistry.isFluidRegistered(fluid);
	}

	public static boolean doesFluidsExist(Fluid[] fluid)
	{
		for (int i = 0; i < fluid.length; ++i)
		{
			if (!doesFluidExist(fluid[i]))
			{
				return false;
			}
		}
		return true;
	}

	public static FluidStack replaceFluidStack(int fluidId, FluidStack srcStack)
	{
		final Fluid fluid = FluidRegistry.getFluid(fluidId);
		if (fluid == null)
		{
			// An invalid fluid
			return null;
		}

		if (srcStack == null)
		{
			return new FluidStack(fluid, 0);
		}
		return new FluidStack(fluid, srcStack.amount);
	}

	public static FluidStack updateFluidStackAmount(FluidStack srcStack, int amount)
	{
		if (srcStack == null)
		{
			return new FluidStack(FluidRegistry.WATER, amount);
		}
		srcStack.amount = amount;
		return srcStack;
	}
}
