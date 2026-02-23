package growthcraft.cellar.network;

import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketClearTankButtonWByte extends AbstractPacketButton
{
	byte b;

	public PacketClearTankButtonWByte(){}

	public PacketClearTankButtonWByte(int x, int y, int z, byte byt)
	{
		super(x, y, z);
		this.b = byt;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		super.encodeInto(ctx, buffer);
		buffer.writeByte(b);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		super.decodeInto(ctx, buffer);
		this.b = buffer.readByte();
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

		if (te instanceof TileEntityCellarDevice)
		{
			if (b == 0)
			{
				((TileEntityCellarDevice)te).clearTank(0);
			}
			else if (b == 1)
			{
				((TileEntityCellarDevice)te).clearTank(1);
			}
		}
	}
}
