package growthcraft.cellar.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class CellarTank extends FluidTank
{
	public CellarTank(int capacity, TileEntity tile) 
	{
		super(capacity);
		this.tile = tile;
	}
}
