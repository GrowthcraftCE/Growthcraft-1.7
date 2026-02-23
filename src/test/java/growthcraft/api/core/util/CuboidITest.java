package growthcraft.api.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CuboidITest
{
	@Test
	public void test_constructor_default()
	{
		final CuboidI cuboid = new CuboidI();
		assertEquals(0, cuboid.x);
		assertEquals(0, cuboid.y);
		assertEquals(0, cuboid.z);
		assertEquals(0, cuboid.w);
		assertEquals(0, cuboid.h);
		assertEquals(0, cuboid.l);
	}

	@Test
	public void test_constructor_i6()
	{
		final CuboidI cuboid = new CuboidI(1, 2, 3, 4, 5, 6);
		assertEquals(1, cuboid.x);
		assertEquals(2, cuboid.y);
		assertEquals(3, cuboid.z);
		assertEquals(4, cuboid.w);
		assertEquals(5, cuboid.h);
		assertEquals(6, cuboid.l);
	}

	@Test
	public void test_constructor_cuboid()
	{
		final CuboidI other = new CuboidI(1, 2, 3, 4, 5, 6);
		final CuboidI cuboid = new CuboidI(other);
		assertEquals(1, cuboid.x);
		assertEquals(2, cuboid.y);
		assertEquals(3, cuboid.z);
		assertEquals(4, cuboid.w);
		assertEquals(5, cuboid.h);
		assertEquals(6, cuboid.l);
	}
}
