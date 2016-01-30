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
package growthcraft.cellar.eventhandler;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.event.BarrelDrainedEvent;
import growthcraft.cellar.stats.CellarAchievement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerBarrelDrainedCellar
{
	@SubscribeEvent
	public void handle(BarrelDrainedEvent event)
	{
		if (event.fluid != null && event.player != null)
		{
			if (CellarRegistry.instance().booze().isFluidBooze(event.fluid))
			{
				if (CellarRegistry.instance().booze().hasTags(event.fluid.getFluid(), BoozeTag.FERMENTED))
				{
					CellarAchievement.FERMENT_BOOZE.unlock(event.player);
				}
			}
		}
	}
}
