package growthcraft.cellar.network;

import growthcraft.cellar.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.tileentity.TileEntityFruitPress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketClearTankButton extends AbstractPacket 
{
	int x, y, z;

	public PacketClearTankButton(){}

	public PacketClearTankButton(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();

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

		if (te instanceof TileEntityFruitPress)
		{
			((TileEntityFruitPress) te).clearTank();
		}

		if (te instanceof TileEntityFermentBarrel)
		{
			((TileEntityFermentBarrel) te).clearTank();
		}
	}
}
