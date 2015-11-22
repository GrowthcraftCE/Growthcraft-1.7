/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
package growthcraft.api.cellar.booze;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.util.MathHelper;

public class BoozeEffect
{
	private boolean hasTipsyEffect;
	private float tipsyChance;
	private int tipsyTime;
	private List<PotionEntry> potionEntries = new ArrayList<PotionEntry>();

	public BoozeEffect clearTipsy()
	{
		this.hasTipsyEffect = false;
		this.tipsyChance = 0.0f;
		this.tipsyTime = 0;
		return this;
	}

	public BoozeEffect setTipsy(float chance, int time)
	{
		this.hasTipsyEffect = true;
		this.tipsyChance = MathHelper.clamp_float(chance, 0.1F, 1.0F);
		this.tipsyTime = time;
		return this;
	}

	public BoozeEffect clearPotionEntries()
	{
		potionEntries.clear();
		return this;
	}

	public BoozeEffect addPotionEntry(int id, int time, int level)
	{
		potionEntries.add(new PotionEntry(id, time, level));
		return this;
	}

	public BoozeEffect addPotionEntries(List<PotionEntry> list)
	{
		potionEntries.addAll(list);
		return this;
	}

	public List<PotionEntry> getPotionEntries()
	{
		return potionEntries;
	}

	public boolean hasPotionEntries()
	{
		return getPotionEntries().size() > 0;
	}

	public boolean canCauseTipsy()
	{
		return hasTipsyEffect;
	}

	public float getTipsyChance()
	{
		return tipsyChance;
	}

	public int getTipsyTime()
	{
		return tipsyTime;
	}

	public boolean isValid()
	{
		return canCauseTipsy() || hasPotionEntries();
	}
}
