package growthcraft.core;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

/**
 * Extend this class when you need config for another module, see the other
 * modules for usage.
 */
public abstract class ConfigBase
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	protected static @interface ConfigOption
	{
		String catergory() default Configuration.CATEGORY_GENERAL;
		String name();
		String desc() default "";
	}

	private static final String DEFAULT_STR = "; Default : ";

	protected Configuration config;

	@SuppressWarnings("rawtypes")
	protected void autoloadConfig()
	{
		for (Field field : getClass().getDeclaredFields())
		{
			final ConfigOption opt = field.getAnnotation(ConfigOption.class);
			if (opt != null)
			{
				final Class typeClass = field.getType();
				try
				{
					if (Integer.TYPE.equals(typeClass))
					{
						final int val = field.getInt(this);
						field.setInt(this, config.get(opt.catergory(), opt.name(), val, opt.desc() + DEFAULT_STR + val).getInt());
					}
					else if (Float.TYPE.equals(typeClass))
					{
						final float val = field.getFloat(this);
						field.setFloat(this, (float)config.get(opt.catergory(), opt.name(), (double)val, opt.desc() + DEFAULT_STR + val).getDouble());
					}
					else if (Boolean.TYPE.equals(typeClass))
					{
						final boolean val = field.getBoolean(this);
						field.setBoolean(this, config.get(opt.catergory(), opt.name(), val, opt.desc() + DEFAULT_STR + val).getBoolean());
					}
					else if (Double.TYPE.equals(typeClass))
					{
						final double val = field.getDouble(this);
						field.setDouble(this, config.get(opt.catergory(), opt.name(), val, opt.desc() + DEFAULT_STR + val).getDouble());
					}
					else if (String.class.equals(typeClass))
					{
						final String val = (String)field.get(this);
						field.set(this, config.get(opt.catergory(), opt.name(), val, opt.desc() + DEFAULT_STR + val).getString());
					}
					else
					{
						FMLLog.log("Growthcraft", Level.ERROR, "Unhandled config option: type=" + typeClass + " option=" + opt.name());
					}
				}
				catch (IllegalAccessException ex)
				{
					FMLLog.log("Growthcraft", Level.ERROR, ex.toString());
				}
			}
		}
	}

	protected void loadConfig()
	{
		autoloadConfig();
	}

	protected void postLoadConfig()
	{

	}

	/**
	 * Creates a Configuration instance and loads it, this will then pass
	 * control to loadConfig; where the config reading should take place.
	 *
	 * @param configDir - root config directory
	 * @param filename - config filename
	 */
	public void load(File configDir, String filename)
	{
		config = new Configuration(new File(configDir, filename));
		try
		{
			config.load();
			loadConfig();
			postLoadConfig();
		}
		finally
		{
			if (config.hasChanged())
			{
				config.save();
				FMLLog.log("Growthcraft", Level.WARN, "Config file " + filename + " has changed, be sure to check it.");
			}
		}
	}
}
