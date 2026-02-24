package growthcraft.cellar.network;

import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketSwitchTankButton extends AbstractPacketButton
{
	public PacketSwitchTankButton() {}

	public PacketSwitchTankButton(int x, int y, int z)
	{
		super(x, y, z);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{

	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		final World world = player.worldObj;
		final TileEntity te = world.getTileEntity(xCoord, yCoord, zCoord);

		if (te instanceof TileEntityBrewKettle)
		{
			((TileEntityBrewKettle)te).switchTanks();
		}
	}
}
