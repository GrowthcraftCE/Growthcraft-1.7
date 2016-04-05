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
package growthcraft.cellar.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EventWaterBag extends Event
{
	public final ItemStack itemStack;
	public final World worldObj;
	public final EntityPlayer player;

	public EventWaterBag(ItemStack stack, World world, EntityPlayer ply)
	{
		this.itemStack = stack;
		this.worldObj = world;
		this.player = ply;
	}

	public static class PreDrink extends EventWaterBag
	{
		public PreDrink(ItemStack stack, World world, EntityPlayer ply)
		{
			super(stack, world, ply);
		}

		@Override
		public boolean isCancelable()
		{
			return true;
		}
	}

	public static class PostDrink extends EventWaterBag
	{
		public PostDrink(ItemStack stack, World world, EntityPlayer ply)
		{
			super(stack, world, ply);
		}
	}
}
