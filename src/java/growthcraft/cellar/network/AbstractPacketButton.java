package growthcraft.cellar.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractPacketButton extends AbstractPacket
{
	int xCoord;
	int yCoord;
	int zCoord;

	public AbstractPacketButton() {}

	public AbstractPacketButton(int x, int y, int z)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(xCoord);
		buffer.writeInt(yCoord);
		buffer.writeInt(zCoord);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.xCoord = buffer.readInt();
		this.yCoord = buffer.readInt();
		this.zCoord = buffer.readInt();
	}
}
