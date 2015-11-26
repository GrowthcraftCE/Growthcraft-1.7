package growthcraft.api.core.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EffectChanceTest
{
	@Test
	public void test_apply()
	{
		final Random random = new Random();
		final EffectChance chance = new EffectChance();
		final TestEffect testEffect = new TestEffect();
		chance.setChance(1.0f);
		chance.setEffect(testEffect);
		chance.apply(null, null, random, null);
		assertEquals(testEffect.touched, 1);
	}

	@Test
	public void test_getDescription()
	{
		final EffectChance chance = new EffectChance();
		final TestEffect testEffect = new TestEffect();
		chance.setChance(1.0f);
		chance.setEffect(testEffect);
		final List<String> list = new ArrayList<String>();
		chance.getDescription(list);
		assertTrue(list.size() > 0);
	}
}
