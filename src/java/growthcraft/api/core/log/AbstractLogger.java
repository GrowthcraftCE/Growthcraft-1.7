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
package growthcraft.api.core.log;

import org.apache.logging.log4j.Level;

/**
 * Abstract Logger class
 */
public abstract class AbstractLogger implements ILogger
{
	protected boolean enabled = true;

	@Override
	public void enable()
	{
		this.enabled = true;
	}

	@Override
	public void disable()
	{
		this.enabled = false;
	}

	protected abstract void doLog(Level lv, String str, Object... objs);

	@Override
	public void log(Level lv, String str, Object... objs)
	{
		if (enabled) doLog(lv, str, objs);
	}

	@Override
	public void info(String str, Object... objs)
	{
		log(Level.INFO, str, objs);
	}

	@Override
	public void debug(String str, Object... objs)
	{
		log(Level.DEBUG, str, objs);
	}

	@Override
	public void warn(String str, Object... objs)
	{
		log(Level.WARN, str, objs);
	}

	@Override
	public void error(String str, Object... objs)
	{
		log(Level.ERROR, str, objs);
	}

	@Override
	public void fatal(String str, Object... objs)
	{
		log(Level.FATAL, str, objs);
	}
}
