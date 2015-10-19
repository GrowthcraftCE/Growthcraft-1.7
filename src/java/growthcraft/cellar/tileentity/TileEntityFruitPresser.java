package growthcraft.cellar.tileentity;

import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFruitPresser extends TileEntity
{
	public float trans;
	public float transPrev;

	private float transSpd = 0.21875F;
	private float transMin;
	private float transMax = 0.4375F;

	/************
	 * UPDATE
	 ************/
	public void updateEntity()
	{
		super.updateEntity();

		if (GrowthCraftCellar.fruitPresser.getBlock() != worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord))
		{
			invalidate();
		}

		final int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		this.transPrev = this.trans;

		if ((meta == 0 || meta == 1) && this.trans > this.transMin)
		{
			this.trans -= this.transSpd;
		}
		else if ((meta == 2 || meta == 3) && this.trans < this.transMax)
		{
			this.trans += this.transSpd;
		}
	}

	public float getTranslation()
	{
		return this.trans;
	}

	/************
	 * NBT
	 ************/
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.trans = nbt.getFloat("trans");
		this.transPrev = nbt.getFloat("transprev");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("trans", trans);
		nbt.setFloat("transprev", transPrev);
	}

	/************
	 * PACKETS
	 ************/
	@Override
	public Packet getDescriptionPacket()
	{
		final NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.func_148857_g());
		this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
	}
}
