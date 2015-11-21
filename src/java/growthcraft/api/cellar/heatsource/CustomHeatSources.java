package growthcraft.api.cellar.heatsource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.apache.logging.log4j.Level;

import growthcraft.api.core.util.ItemKey;
import growthcraft.api.cellar.CellarRegistry;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration.UnicodeInputStreamReader;

/**
 * Allows you to load Custom heat source defintions from a JSON file
 */
public class CustomHeatSources
{
	public static class HeatSourceEntry
	{
		public String mod_id;
		public String block_name;
		public Map<Integer, Float> states;

		public HeatSourceEntry() {}

		public HeatSourceEntry(String m, String b, Map<Integer, Float> s)
		{
			this.mod_id = m;
			this.block_name = b;
			this.states = s;
		}

		public static Map<Integer, Float> wildcardHeat(float h)
		{
			final Map<Integer, Float> map = new HashMap<Integer, Float>();
			map.put(ItemKey.WILDCARD_VALUE, h);
			return map;
		}
	}

	private static final HeatSourceEntry[] DEFAULT_ENTRIES = {
		new HeatSourceEntry("minecraft", "fire", HeatSourceEntry.wildcardHeat(1.0f)),
		new HeatSourceEntry("minecraft", "flowing_lava", HeatSourceEntry.wildcardHeat(0.7f)),
		new HeatSourceEntry("minecraft", "lava", HeatSourceEntry.wildcardHeat(0.7f))
	};

	private final String DEFAULT_ENCODING = "UTF-8";
	private final Gson gson = new Gson();
	private final String parentModID;
	private HeatSourceEntry[] entries;

	public CustomHeatSources(String parentMod)
	{
		this.parentModID = parentMod;
	}

	private void addHeatSource(HeatSourceEntry heatsource)
	{
		final Block block = GameRegistry.findBlock(heatsource.mod_id, heatsource.block_name);
		if (block != null)
		{
			if (heatsource.states == null || heatsource.states.size() == 0)
			{
				FMLLog.log(
					parentModID,
					Level.WARN,
					"Block contains invalid states, we will assume a wildcard, but you should probably set this. mod_id=%s block=%s", heatsource.mod_id, heatsource.block_name
				);
				CellarRegistry.instance().heatSource().addHeatSource(block, ItemKey.WILDCARD_VALUE);
			}
			else
			{
				for (Map.Entry<Integer, Float> entry : heatsource.states.entrySet())
				{
					int key = entry.getKey();
					if (key < 0) key = ItemKey.WILDCARD_VALUE;
					CellarRegistry.instance().heatSource().addHeatSource(block, key, entry.getValue());
				}
			}
		}
		else
		{
			FMLLog.log(
				parentModID,
				Level.ERROR,
				"Block could not be found, and will not be added as heat source. mod_id=%s block=%s", heatsource.mod_id, heatsource.block_name
			);
		}
	}

	private void loadFromBuffer(BufferedReader buff)
	{
		entries = gson.fromJson(buff, HeatSourceEntry[].class);
	}

	public void saveDefault(File file)
	{
		try
		{
			if (file.getParentFile() != null)
			{
				file.getParentFile().mkdirs();
			}

			if (!file.createNewFile())
				return;

			if (file.canWrite())
			{
				try (FileWriter writer = new FileWriter(file))
				{
					final String result = gson.toJson(DEFAULT_ENTRIES, HeatSourceEntry[].class);
					writer.write(result);
				}
			}
		}
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}

	public void load(File dir, String filename)
	{
		final File file = new File(dir, filename);
		BufferedReader buffer = null;
		UnicodeInputStreamReader input = null;
		try
		{
			if (!file.exists()) saveDefault(file);

			if (file.canRead())
			{
				input = new UnicodeInputStreamReader(new FileInputStream(file), DEFAULT_ENCODING);
				buffer = new BufferedReader(input);
				loadFromBuffer(buffer);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (buffer != null)
			{
				try
				{
					buffer.close();
				} catch (IOException e){}
			}
			if (input != null)
			{
				try
				{
					input.close();
				} catch (IOException e){}
			}
		}
	}

	public void register()
	{
		if (entries != null)
		{
			FMLLog.log(parentModID, Level.INFO, "Registering %d heat sources.", entries.length);
			for (HeatSourceEntry heatsource : entries) addHeatSource(heatsource);
		}
	}
}
