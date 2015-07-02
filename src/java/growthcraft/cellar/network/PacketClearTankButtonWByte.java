package growthcraft.cellar.network;

import growthcraft.cellar.tileentity.TileEntityBrewKettle;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketClearTankButtonWByte extends AbstractPacket 
{
	int x, y, z;
	byte b;

	public PacketClearTankButtonWByte(){}

	public PacketClearTankButtonWByte(int x, int y, int z, byte b)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.b = b;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeByte(b);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		b = buffer.readByte();

	}

	@Override
	public void handleClientSide(EntityPlayer player) 
	{

	}

	@Override
	public void handleServerSide(EntityPlayer player) 
	{
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityBrewKettle)
		{
			if (b == 0)
			{
				((TileEntityBrewKettle) te).clearTank(0);
			}
			else if (b == 1)
			{
				((TileEntityBrewKettle) te).clearTank(1);
			}
		}
	}
}
