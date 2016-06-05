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
package growthcraft.core.common.tileentity;

public abstract class GrcTileEntityCommonBase extends GrcTileEntityBase
{
	protected boolean needInventoryUpdate;

	protected void updateDevice()
	{
	}

	// Call this when you modify a fluid tank outside of its usual methods
	protected void markForFluidUpdate()
	{
		//
	}

	// Call this when you have modified the inventory, or you're not sure what
	// kind of update you require
	public void markForInventoryUpdate()
	{
		needInventoryUpdate = true;
	}

	protected void checkUpdateFlags()
	{
		if (needInventoryUpdate)
		{
			needInventoryUpdate = false;
			markDirty();
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		checkUpdateFlags();

		if (!worldObj.isRemote)
		{
			updateDevice();
		}
	}
}
