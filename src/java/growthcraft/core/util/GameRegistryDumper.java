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
package growthcraft.core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * If you really, REALLY need to dump the block registry, then this is your thing.
 * Sub blocks are indented by 1 single tab character under their parent block.
 */
public class GameRegistryDumper
{
	private GameRegistryDumper() {}

	private static void writeItemStackToFile(ItemStack stack, FileWriter writer) throws IOException
	{
		if (stack != null && stack.getItem() != null)
		{
			final Item item = stack.getItem();
			final int damage = stack.getItemDamage();
			final String unlocName = stack.getUnlocalizedName();
			final String displayName = stack.getDisplayName();
			writer.write("\t" + item + "," + damage + "," + unlocName + "," + displayName + "\n");
		}
	}

	private static void writeItemSubtypes(Object obj, FileWriter writer) throws IOException
	{
		try
		{
			final List<ItemStack> sub = new ArrayList<ItemStack>();
			if (obj instanceof Item)
			{
				final Item item = (Item)obj;
				if (!item.getHasSubtypes()) return;
				item.getSubItems(item, item.getCreativeTab(), sub);
			}
			else if (obj instanceof Block)
			{
				final Block block = (Block)obj;
				final Item item = Item.getItemFromBlock(block);
				if (item == null) return;
				if (!item.getHasSubtypes()) return;
				block.getSubBlocks(item, block.getCreativeTabToDisplayOn(), sub);
			}

			if (sub.size() > 0)
			{
				for (ItemStack stack : sub)
				{
					if (stack != null && stack.getItem() != null)
					{
						writeItemStackToFile(stack, writer);
					}
				}
			}
		}
		catch (NullPointerException ex)
		{
			ex.printStackTrace();
			writer.write("\tnull,,,\n");
		}
	}

	@SuppressWarnings("rawtypes")
	public static void dumpBlocks()
	{
		final Iterator it = GameRegistry.Type.BLOCK.getRegistry().iterator();

		try (FileWriter writer = new FileWriter("dumps/GameRegistry_Blocks.txt"))
		{
			while (it.hasNext())
			{
				final Block obj = (Block)it.next();
				try
				{
					if (obj != null)
					{
						writer.write("" + Block.getIdFromBlock(obj) + "," + obj.getUnlocalizedName() + "," + obj.getLocalizedName() + "," + GameRegistry.findUniqueIdentifierFor(obj) + "\n");

						if (Platform.isClient())
						{
							writeItemSubtypes(obj, writer);
						}
						else
						{
							writeItemStackToFile(new ItemStack(obj), writer);
						}
					}
				}
				catch (NullPointerException ex) {}
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public static void dumpItems()
	{
		final Iterator it = GameRegistry.Type.ITEM.getRegistry().iterator();

		try (FileWriter writer = new FileWriter("dumps/GameRegistry_Items.txt"))
		{
			while (it.hasNext())
			{
				final Item obj = (Item)it.next();
				try
				{
					if (obj != null)
					{
						writer.write("" + Item.getIdFromItem(obj) + "," + obj.getUnlocalizedName() + "," + "?" + "," + GameRegistry.findUniqueIdentifierFor(obj) + "\n");

						if (Platform.isClient())
						{
							writeItemSubtypes(obj, writer);
						}
						else
						{
							writeItemStackToFile(new ItemStack(obj), writer);
						}
					}
				}
				catch (NullPointerException ex) {}
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public static void dumpFluids()
	{
		final Set<String> fluidKeys = FluidRegistry.getRegisteredFluids().keySet();
		try (FileWriter writer = new FileWriter("dumps/FluidRegistry_Fluids.txt"))
		{
			for (String key : fluidKeys)
			{
				writer.write(key + "\n");
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void run()
	{
		dumpBlocks();
		dumpItems();
		dumpFluids();
	}
}
