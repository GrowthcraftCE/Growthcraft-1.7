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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemHandler
{
	/**
	 * Tries to place the item from the player into the target
	 *
	 * @param player - the player placing the item
	 * @param stack - the item stack being placed
	 * @return true, if something happened, eg. item was inserted, false otherwise
	 */
	boolean tryPlaceItem(@Nonnull EntityPlayer player, @Nullable ItemStack stack);

	/**
	 * Tries to remove an item from the target
	 * The stack provided is the player's current item stack, this can be safely
	 * ignored, OR changes the behaviour of the target.
	 *
	 * @param player - player taking the item
	 * @param stack - stack on player's hand currently
	 * @return true, an item was taken, false otherwise
	 */
	boolean tryTakeItem(@Nonnull EntityPlayer player, @Nullable ItemStack stack);
}
