package growthcraft.api.core.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class NumUtilsTest
{
	@Test
	public void test_between()
	{
		assertTrue(NumUtils.between(1, 1, 1));
		assertTrue(NumUtils.between(1, 0, 1));
		assertFalse(NumUtils.between(1, 0, 0));
		assertFalse(NumUtils.between(1, -1, 0));
		assertTrue(NumUtils.between(100, -100, 100));
		assertFalse(NumUtils.between(200, -100, 100));
	}

	@Test
	public void test_newIntRangeArray()
	{
		assertArrayEquals(new int[]{ 0, 1, 2, 3, 4, 5, 6, 7 }, NumUtils.newIntRangeArray(0, 8));
		assertArrayEquals(new int[]{ 8, 9, 10, 11, 12, 13, 14, 15 }, NumUtils.newIntRangeArray(8, 8));
		assertArrayEquals(new int[]{ 16, 17, 18, 19 }, NumUtils.newIntRangeArray(16, 4));
	}
}
