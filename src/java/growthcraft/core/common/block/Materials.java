package growthcraft.core.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
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

	static class MaterialBooze extends MaterialLiquid
	{
		public MaterialBooze(MapColor color)
		{
			super(color);
			setNoPushMobility();
		}

		public boolean isLiquid()
		{
			return true;
		}
	}

	public static final Material fireproofWood = new Material(Material.wood.getMaterialMapColor());
	public static final Material fireproofLeaves = new MaterialFireproofLeaves(Material.leaves.getMaterialMapColor());
	public static final Material booze = new MaterialBooze(Material.water.getMaterialMapColor());

	private Materials() {}
}
