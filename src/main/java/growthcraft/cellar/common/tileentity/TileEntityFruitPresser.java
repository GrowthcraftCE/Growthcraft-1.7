package growthcraft.cellar.common.tileentity;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.tileentity.event.TileEventHandler;
import growthcraft.core.common.tileentity.GrcTileBase;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFruitPresser extends GrcTileBase
{
	public float trans;
	public float transPrev;

	private float transSpd = 0.21875F;
	private float transMin;
	private float transMax = 0.4375F;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (GrowthCraftCellar.blocks.fruitPresser.getBlock() != getBlockType())
		{
			invalidate();
		}

		final int meta = getBlockMetadata();
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

	@TileEventHandler(event=TileEventHandler.EventType.NBT_READ)
	public void readFromNBT_FruitPresser(NBTTagCompound nbt)
	{
		this.trans = nbt.getFloat("trans");
		this.transPrev = nbt.getFloat("transprev");
	}

	@TileEventHandler(event=TileEventHandler.EventType.NBT_WRITE)
	public void writeToNBT_FruitPresser(NBTTagCompound nbt)
	{
		nbt.setFloat("trans", trans);
		nbt.setFloat("transprev", transPrev);
	}

	@TileEventHandler(event=TileEventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_FruitPresser(ByteBuf stream) throws IOException
	{
		this.trans = stream.readFloat();
		this.transPrev = stream.readFloat();
		return true;
	}

	@TileEventHandler(event=TileEventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_FruitPresser(ByteBuf stream) throws IOException
	{
		stream.writeFloat(trans);
		stream.writeFloat(transPrev);
		return true;
	}
}
