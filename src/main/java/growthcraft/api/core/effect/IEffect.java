/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.api.core.effect;

import java.util.Random;

import growthcraft.api.core.description.IDescribable;
import growthcraft.api.core.nbt.INBTSerializableContext;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;

/**
 * This is the main interface for Growthcraft's Effect system.
 * Its meant to solve the problem with constructing complex item effects,
 * where data along just won't cut it.
 */
public interface IEffect extends IDescribable, INBTSerializableContext
{
	/**
	 * This method is called when the effect needs to be applied to the
	 * given world and entity.
	 *
	 * @param world - world that the entity is currently present ing
	 * @param entity - entity to apply the effect to
	 * @param data - any extra data you want to pass along
	 */
	void apply(World world, Entity entity, Random random, Object data);
}
