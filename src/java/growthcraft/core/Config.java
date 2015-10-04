package growthcraft.core;

import java.io.File;
import growthcraft.core.ConfigBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigBase
{
	public Config(File dirname, String filename)
	{
		super(dirname, filename);
	}

	protected void loadConfig()
	{
		// Nobody here but us chickens, quack?
		// in anticipation for the loglevel variables, this will remain here.
	}
}
