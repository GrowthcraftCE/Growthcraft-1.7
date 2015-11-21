package growthcraft.core.common;

import javax.annotation.Nonnull;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;

public class GrcModuleBase implements IModule, ILoggable
{
	protected ILogger logger = NullLogger.INSTANCE;

	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void register() {}

	@Override
	public void postInit() {}
}
