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
package growthcraft.api.core.util;

public class EasingTemplate
{
	public static interface EasingFunction
	{
		double call(double k);
	}

	public static class Linear implements EasingFunction
	{
		public double call(double k)
		{
			return k;
		}
	}

	public static class QuadraticIn implements EasingFunction
	{
		public double call(double k)
		{
			return k * k;
		}
	}

	public static class QuadraticOut implements EasingFunction
	{
		public double call(double k)
		{
			return k * (2 - k);
		}
	}

	public static class QuadraticInOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if ((k *= 2) < 1)
				return 0.5 * k * k;
			else
				return -0.5 * ((k-=1) * (k - 2) - 1);
		}
	}

	public static class CubicIn implements EasingFunction
	{
		public double call(double k)
		{
			return k * k * k;
		}
	}

	public static class CubicOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			return (k -= 1) * k * k + 1;
		}
	}

	public static class CubicInOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if ((k *= 2) < 1)
				return 0.5 * k * k * k;
			else
				return 0.5 * ((k -= 2) * k * k + 2);
		}
	}

	public static class QuarticIn implements EasingFunction
	{
		public double call(double k)
		{
			return k * k * k * k;
		}
	}

	public static class QuarticOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			return 1 - ((k-=1) * k * k * k);
		}
	}

	public static class QuarticInOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if ((k *= 2) < 1)
				return 0.5 * k * k * k * k;
			else
				return -0.5 * ((k -= 2) * k * k * k - 2);
		}
	}

	public static class QuinticIn implements EasingFunction
	{
		public double call(double k)
		{
			return k * k * k * k * k;
		}
	}

	public static class QuinticOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			return (k-=1) * k * k * k * k + 1;
		}
	}

	public static class QuinticInOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if ((k *= 2) < 1)
				return 0.5 * k * k * k * k * k;
			else
				return 0.5 * ((k -= 2) * k * k * k * k + 2);
		}
	}

	public static class SinusoidalIn implements EasingFunction
	{
		public double call(double k)
		{
			return 1 - Math.cos(k * Math.PI / 2);
		}
	}

	public static class SinusoidalOut implements EasingFunction
	{
		public double call(double k)
		{
			return Math.sin(k * Math.PI / 2);
		}
	}

	public static class SinusoidalInOut implements EasingFunction
	{
		public double call(double k)
		{
			return 0.5 * (1 - Math.cos(Math.PI * k));
		}
	}

	public static class ExponentialIn implements EasingFunction
	{
		public double call(double k)
		{
			return k == 0 ? 0 : Math.pow(1024D, k - 1);
		}
	}

	public static class ExponentialOut implements EasingFunction
	{
		public double call(double k)
		{
			return k == 1 ? 1 : 1 - Math.pow(2D, -10 * k);
		}
	}

	public static class ExponentialInOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if (k == 0D)
				return 0;
			else if (k == 1D)
				return 1;
			else if ((k *= 2D) < 1D)
				return 0.5 * Math.pow(1024D, k - 1);
			else
				return 0.5 * (-(Math.pow(2D, -10 * (k - 1))) + 2);
		}
	}

	public static class CircularIn implements EasingFunction
	{
		public double call(double k)
		{
			return 1 - Math.sqrt(1 - k * k);
		}
	}

	public static class CircularOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			return Math.sqrt(1 - ((k-=1) * k));
		}
	}

	public static class CircularInOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if ((k *= 2) < 1)
				return -0.5 * (Math.sqrt(1 - k * k) - 1);
			else
				return 0.5 * (Math.sqrt(1 - (k -= 2) * k) + 1);
		}
	}

	public static class ElasticIn implements EasingFunction
	{
		private final double p = 0.4D;

		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			double s = 0.0D;
			double a = 0.1D;

			if (k == 0D)
				return 0;
			else if (k == 1D)
				return 1;

			if (s == 0D || a < 1D)
			{
				a = 1;
				s = p / 4;
			}
			else
			{
				s = p * Math.asin(1 / a) / (2 * Math.PI);
			}

			return -(a * (Math.pow(2D, 10 * (k -= 1))) * Math.sin((k - s) * (2 * Math.PI) / p));
		}
	}

	public static class ElasticOut implements EasingFunction
	{
		private final double p = 0.4;

		public double call(double k)
		{
			double s = 0.0;
			double a = 0.1;

			if (k == 0D)
				return 0;
			else if (k == 1D)
				return 1;

			if (a == 0D || a < 1D)
			{
				a = 1;
				s = p / 4;
			}
			else
			{
				s = p * Math.asin(1 / a) / (2 * Math.PI);
			}

			return a * Math.pow(2D, -10 * k) * Math.sin((k - s) * (2 * Math.PI) / p) + 1;
		}
	}

	public static class ElasticInOut implements EasingFunction
	{
		private final double p = 0.4;

		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			double s = 0.0;
			double a = 0.1;

			if (k == 0D)
				return 0;
			else if (k == 1D)
				return 1;

			if (a == 0D || a < 1D)
			{
				a = 1;
				s = p / 4;
			}
			else
			{
				s = p * Math.asin(1 / a) / (2 * Math.PI);
			}

			if ((k *= 2) < 1)
				return -0.5 * (a * Math.pow(2, 10 * (k -= 1)) * Math.sin((k - s) * (2 * Math.PI) / p));
			else
				return a * Math.pow(2, -10 * (k -= 1)) * Math.sin((k - s) * (2 * Math.PI) / p) * 0.5 + 1;
		}
	}

	public static class BackIn implements EasingFunction
	{
		private final double s = 1.70158;
		public double call(double k)
		{
			return k * k * ((s + 1) * k - s);
		}
	}

	public static class BackOut implements EasingFunction
	{
		private final double s = 1.70158;

		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			return (k-=1) * k * ((s + 1) * k + s) + 1;
		}
	}

	public static class BackInOut implements EasingFunction
	{
		private final double s = 1.70158 * 1.525;

		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if ((k *= 2D) < 1D)
				return 0.5 * (k * k * ((s + 1) * k - s));
			else
				return 0.5 * ((k -= 2) * k * ((s + 1) * k + s) + 2);
		}
	}

	public static class BounceOut implements EasingFunction
	{
		@SuppressWarnings("checkstyle:innerassignment")
		public double call(double k)
		{
			if (k < (1 / 2.75))
				return 7.5625 * k * k;
			else if (k < (2 / 2.75))
				return 7.5625 * (k -= 1.5 / 2.75) * k + 0.75;
			else if (k < (2.5 / 2.75))
				return 7.5625 * (k -= 2.25 / 2.75) * k + 0.9375;
			else
				return 7.5625 * (k -= 2.625 / 2.75) * k + 0.984375;
		}
	}

	public static class BounceIn implements EasingFunction
	{
		private final BounceOut bo = new BounceOut();

		public double call(double k)
		{
			return 1 - bo.call(1 - k);
		}
	}

	public static class BounceInOut implements EasingFunction
	{
		private final BounceIn bi = new BounceIn();
		private final BounceOut bo = new BounceOut();

		public double call(double k)
		{
			if (k < 0.5D)
				return bi.call(k * 2) * 0.5D;
			else
				return bo.call(k * 2 - 1) * 0.5D + 0.5D;
		}
	}

	public final BackIn backIn = new BackIn();
	public final BackInOut backInOut = new BackInOut();
	public final BackOut backOut = new BackOut();
	public final BounceIn bounceIn = new BounceIn();
	public final BounceInOut bounceInOut = new BounceInOut();
	public final BounceOut bounceOut = new BounceOut();
	public final CircularIn circIn = new CircularIn();
	public final CircularInOut circInOut = new CircularInOut();
	public final CircularOut circOut = new CircularOut();
	public final CubicIn cubicIn = new CubicIn();
	public final CubicInOut cubicInOut = new CubicInOut();
	public final CubicOut cubicOut = new CubicOut();
	public final ElasticIn elasticIn = new ElasticIn();
	public final ElasticInOut elasticInOut = new ElasticInOut();
	public final ElasticOut elasticOut = new ElasticOut();
	public final ExponentialIn expoIn = new ExponentialIn();
	public final ExponentialInOut expoInOut = new ExponentialInOut();
	public final ExponentialOut expoOut = new ExponentialOut();
	public final Linear linear = new Linear();
	public final QuadraticIn quadIn = new QuadraticIn();
	public final QuadraticInOut quadInOut = new QuadraticInOut();
	public final QuadraticOut quadOut = new QuadraticOut();
	public final QuarticIn quartIn = new QuarticIn();
	public final QuarticInOut quartInOut = new QuarticInOut();
	public final QuarticOut quartOut = new QuarticOut();
	public final QuinticIn quintIn = new QuinticIn();
	public final QuinticInOut quintInOut = new QuinticInOut();
	public final QuinticOut quintOut = new QuinticOut();
	public final SinusoidalIn sineIn = new SinusoidalIn();
	public final SinusoidalInOut sineInOut = new SinusoidalInOut();
	public final SinusoidalOut sineOut = new SinusoidalOut();
}
