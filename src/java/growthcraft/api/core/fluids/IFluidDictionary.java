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
package growthcraft.api.core.fluids;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILoggable;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Growthcraft uses so many fluids, the only thing we can do is tag them to
 * identify them -.-;
 */
public interface IFluidDictionary extends ILoggable
{
	/**
	 * Associates the fluid with the given tags.
	 *
	 * @param fluid - fluid to tag
	 * @param tags - fluid tags to associate this fluid with
	 */
	void addFluidTags(@Nonnull Fluid fluid, @Nonnull FluidTag... tags);

	/**
	 * Gets all tags associated with the fluid.
	 *
	 * @param fluid - fluid to get tags for
	 * @return tags
	 */
	@Nullable Collection<FluidTag> getFluidTags(@Nullable Fluid fluid);

	/**
	 * Gets all tags associated with the fluid.
	 *
	 * @param fluid - fluid stack to get tags for
	 * @return tags
	 */
	@Nullable Collection<FluidTag> getFluidTags(@Nullable FluidStack fluid);

	/**
	 * Does the fluid have the given tags?
	 *
	 * @param fluid - fluid to check
	 * @param tags - tags to look for
	 * @return true, the fluid has ALL the tags specified, false otherwise
	 */
	boolean hasFluidTags(@Nonnull Fluid fluid, @Nonnull FluidTag... tags);

	/**
	 * Retrieves all fluids with the specified tags.
	 *
	 * @param tags - tags that the fluids SHOULD have
	 * @return fluids, or null
	 */
	@Nonnull Collection<Fluid> getFluidsByTags(@Nonnull FluidTag... tags);

	/**
	 * Retrieves all fluids with the specified tags.
	 *
	 * @param tags - tags that the fluids SHOULD have
	 * @return fluids, or null
	 */
	@Nonnull Collection<Fluid> getFluidsByTags(@Nonnull List<FluidTag> tags);
}
