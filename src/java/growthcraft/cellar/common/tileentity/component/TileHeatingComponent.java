/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.cellar.common.tileentity.component;

import javax.annotation.Nonnull;
import io.netty.buffer.ByteBuf;

import growthcraft.api.core.nbt.INBTSerializableContext;
import growthcraft.api.core.stream.IStreamable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileHeatingComponent implements INBTSerializableContext, IStreamable
{
	private TileEntity tileEntity;
	private HeatBlockComponent heatBlockComponent;
	private float heat;
	private float heatLoss = 0.01f;
	private float heatGain = 0.01f;

	public TileHeatingComponent(@Nonnull TileEntity te)
	{
		this.tileEntity = te;
		this.heatBlockComponent = new HeatBlockComponent(tileEntity);
	}

	public void update()
	{
		final float heatMul = heatBlockComponent.getHeatMultiplier();
		if (heatMul > 0)
		{
			if (this.heat < heatMul)
			{
				this.heat += heatGain * heatMul;
			}
			else
			{
				this.heat -= heatGain * heatMul;
			}
		}
		else
		{
			this.heat -= heatLoss;
		}
	}

	public float getHeatMultiplier()
	{
		return heat;
	}

	private void readFromNBT(@Nonnull NBTTagCompound tag)
	{
		this.heat = tag.getFloat("heat");
	}

	@Override
	public void readFromNBT(@Nonnull NBTTagCompound tag, @Nonnull String name)
	{
		if (tag.hasKey(name))
		{
			readFromNBT(tag.getCompoundTag(name));
		}
		else
		{
			// WARN
		}
	}

	private void writeToNBT(@Nonnull NBTTagCompound tag)
	{
		tag.setFloat("heat", heat);
	}

	@Override
	public void writeToNBT(@Nonnull NBTTagCompound tag, @Nonnull String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		writeToNBT(target);
		tag.setTag(name, target);
	}

	@Override
	public void readFromStream(ByteBuf stream)
	{
		this.heat = stream.readFloat();
	}

	@Override
	public void writeToStream(ByteBuf stream)
	{
		stream.writeFloat(heat);
	}
}
