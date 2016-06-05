package growthcraft.api.core.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagParserTest
{
	@Test
	public void test_csv_parseToArray()
	{
		assertArrayEquals(new String[]{"1","2","3","4"}, TagParser.csv.parseToArray("1,2,3,4"));
		assertArrayEquals(new String[]{"1","2","3","4"}, TagParser.csv.parseToArray("     1,   2 ,   3,  4"));
	}

	@Test
	public void test_csv_parse()
	{
		final TagParser.Tag[] tags = TagParser.csv.parse("1,2,+3,-4");
		assertEquals(4, tags.length);
		assertEquals(tags[0].value, "1");
		assertFalse(tags[0].exclude);
		assertFalse(tags[0].must);

		assertEquals(tags[1].value, "2");
		assertFalse(tags[1].exclude);
		assertFalse(tags[1].must);

		assertEquals(tags[2].value, "3");
		assertFalse(tags[2].exclude);
		assertTrue(tags[2].must);

		assertEquals(tags[3].value, "4");
		assertTrue(tags[3].exclude);
		assertFalse(tags[3].must);
	}
}
