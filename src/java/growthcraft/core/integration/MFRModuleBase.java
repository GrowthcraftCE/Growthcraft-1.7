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
package growthcraft.core.integration;

import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import powercrystals.minefactoryreloaded.api.IFactoryFruit;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import powercrystals.minefactoryreloaded.api.FertilizerType;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class MFRModuleBase extends ModIntegrationBase
{
	public static final String MOD_ID = "MineFactoryReloaded";

	public MFRModuleBase(String modid)
	{
		super(modid, MOD_ID);
	}

	@Optional.Method(modid=MOD_ID)
	protected void sendMessage(String target, Object value)
	{
		logger.info("Sending Message to '%s': '%s' %s", modID, target, value);
		FactoryRegistry.sendMessage(target, value);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerPlantableSapling(Item seed, Block sapling)
	{
		final UniqueIdentifier itemUUID = GameRegistry.findUniqueIdentifierFor(seed);
		final UniqueIdentifier blockUUID = GameRegistry.findUniqueIdentifierFor(sapling);
		if (blockUUID != null && itemUUID != null)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			tag.setString("sapling", blockUUID.toString());
			tag.setString("seed", itemUUID.toString());
			sendMessage("registerPlantable_Sapling", tag);
		}
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerPlantableSapling(Block sapling)
	{
		final UniqueIdentifier uuid = GameRegistry.findUniqueIdentifierFor(sapling);
		if (uuid != null)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			tag.setString("sapling", uuid.toString());
			sendMessage("registerPlantable_Sapling", tag);
		}
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerPickableFruit(IFactoryFruit fruit)
	{
		sendMessage("registerPickableFruit", fruit);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerHarvestable(IFactoryHarvestable harvester)
	{
		sendMessage("registerHarvestable", harvester);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerFertilizableByType(String type, Object obj)
	{
		sendMessage("registerFertilizable_" + type, obj);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerFertilizableCrop(NBTTagCompound tag)
	{
		registerFertilizableByType("Crop", tag);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerFertilizableCrop(Block block, int maxGrowth, FertilizerType fertilizerType)
	{
		final UniqueIdentifier uuid = GameRegistry.findUniqueIdentifierFor(block);
		if (uuid != null)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			tag.setString("plant", uuid.toString());
			tag.setInteger("meta", maxGrowth);
			tag.setInteger("type", fertilizerType.ordinal());
			registerFertilizableCrop(tag);
		}
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerFertilizableCrop(Block block, int maxGrowth)
	{
		registerFertilizableCrop(block, maxGrowth, FertilizerType.GrowPlant);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerPlantable(IFactoryPlantable planter)
	{
		sendMessage("registerPlantable", planter);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerHarvestableByType(String type, String str)
	{
		sendMessage("registerHarvestable_" + type, str);
	}

	@Optional.Method(modid=MOD_ID)
	protected void registerHarvestableLeaves(Block block)
	{
		final UniqueIdentifier uuid = GameRegistry.findUniqueIdentifierFor(block);
		if (uuid != null)
			registerHarvestableByType("Leaves", uuid.toString());
	}
}
