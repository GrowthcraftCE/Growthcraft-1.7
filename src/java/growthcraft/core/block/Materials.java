package growthcraft.core.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MapColor;

public class Materials
{
	static class MaterialFireproofLeaves extends Material
	{
		public MaterialFireproofLeaves(MapColor color)
		{
			super(color);
			setNoPushMobility();
		}

		@Override
		public boolean isOpaque()
		{
			return false;
		}
	}

	public static final Material fireproofWood = new Material(Material.wood.getMaterialMapColor());
	public static final Material fireproofLeaves = new MaterialFireproofLeaves(Material.leaves.getMaterialMapColor());

	private Materials() {}
}
