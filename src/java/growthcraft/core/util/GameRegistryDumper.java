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

	@SuppressWarnings("rawtypes")
	public static void dumpBlocks()
	{
		final Iterator it = GameRegistry.Type.BLOCK.getRegistry().iterator();

		try (FileWriter writer = new FileWriter("dumps/GameRegistry_Blocks.txt"))
		{
			while (it.hasNext())
			{
				final Block obj = (Block)it.next();
				if (obj != null)
				{
					writer.write("" + Block.getIdFromBlock(obj) + "," + obj.getUnlocalizedName() + "," + obj.getLocalizedName() + "," + GameRegistry.findUniqueIdentifierFor(obj) + "\n");

					final List<ItemStack> sub = new ArrayList<ItemStack>();
					obj.getSubBlocks(Item.getItemFromBlock(obj), obj.getCreativeTabToDisplayOn(), sub);
					if (sub.size() > 0)
					{
						for (ItemStack stack : sub)
						{
							if (stack != null && stack.getItem() != null)
							{
								writer.write("\t" + stack.getItem() + "," + stack.getItemDamage() + "," + stack.getUnlocalizedName() + "," + stack.getDisplayName() + "\n");
							}
						}
					}
				}
			}
		}
		catch (IOException ex)
		{
			System.err.println(ex);
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
				if (obj != null)
				{
					writer.write("" + Item.getIdFromItem(obj) + "," + obj.getUnlocalizedName() + "," + "?" + "," + GameRegistry.findUniqueIdentifierFor(obj) + "\n");

					final List<ItemStack> sub = new ArrayList<ItemStack>();
					obj.getSubItems(obj, obj.getCreativeTab(), sub);
					if (sub.size() > 0)
					{
						for (ItemStack stack : sub)
						{
							if (stack != null && stack.getItem() != null)
							{
								writer.write("\t" + stack.getItem() + "," + stack.getItemDamage() + "," + stack.getUnlocalizedName() + "," + stack.getDisplayName() + "\n");
							}
						}
					}
				}
			}
		}
		catch (IOException ex)
		{
			System.err.println(ex);
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
			System.err.println(ex);
		}
	}

	public static void run()
	{
		dumpBlocks();
		dumpItems();
		dumpFluids();
	}
}
