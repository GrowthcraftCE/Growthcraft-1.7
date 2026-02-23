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
package growthcraft.core.eventhandler;

import java.util.Random;

import growthcraft.core.common.item.ItemCrowbar;
import growthcraft.core.stats.CoreAchievement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class EventHandlerLivingDeathCore
{
	private Random rng = new Random();

	@SubscribeEvent
	public void onLivingEntityDeath(LivingDeathEvent event)
	{
		if (event.source instanceof EntityDamageSource)
		{
			final EntityDamageSource source = (EntityDamageSource)event.source;
			if (source.getEntity() instanceof EntityPlayer)
			{
				final EntityPlayer player = (EntityPlayer)source.getEntity();
				final ItemStack heldItem = player.getHeldItem();
				if (heldItem != null)
				{
					if (heldItem.getItem() instanceof ItemCrowbar)
					{
						if (event.entityLiving instanceof EntityZombie)
						{
							CoreAchievement.HALF_LIFE_CONFIRMED.unlock(player);
						}
					}
				}
			}
		}
	}
}
