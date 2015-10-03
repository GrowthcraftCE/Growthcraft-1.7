package growthcraft.core;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * Extend this class when you need config for another module, see the other
 * modules for usage.
 */
public abstract class ConfigBase
{
	protected Configuration config;

	/**
	 * @param configDir - the minecraft configuration directory
	 * @param filename - config filename
	 */
	public ConfigBase(File configDir, String filename)
	{
		load(configDir, filename);
	}

	/**
	 * Overwrite this method in your extended class and read the config,
	 * define variables as needed, in short: "I don't care how you do it, just
	 * make sure its done in this method."
	 */
	abstract protected void loadConfig();

	/**
	 * Creates a Configuration instance and loads it, this will then pass
	 * control to loadConfig; where the config reading should take place.
	 *
	 * @param configDir - root config directory
	 * @param filename - config filename
	 */
	private void load(File configDir, String filename)
	{
		config = new Configuration(new File(configDir, filename));
		try
		{
			config.load();
			loadConfig();
		}
		finally
		{
			if (config.hasChanged()) { config.save(); }
		}
	}
}
