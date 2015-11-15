package growthcraft.core.util;

// I'm surprised minecraft doesn't have a Rectangle class, these things are
// darn handy!
public class Rectangle
{
	public int x;
	public int y;
	public int w;
	public int h;

	public Rectangle(int ix, int iy, int iw, int ih)
	{
		this.x = ix;
		this.y = iy;
		this.w = iw;
		this.h = ih;
	}

	public Rectangle()
	{
		this(0, 0, 0, 0);
	}

	public boolean contains(int ix, int iy)
	{
		return ix >= x && iy >= y && ix < (x + w) && iy < (y + h);
	}
}
