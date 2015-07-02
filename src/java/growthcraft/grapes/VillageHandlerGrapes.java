package growthcraft.grapes;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class VillageHandlerGrapes implements IVillageTradeHandler, IVillageCreationHandler 
{
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		//		recipeList.add(new MerchantRecipe(new ItemStack(GrowthCraftGrapes.grapes, 18 + random.nextInt(3)), new ItemStack(Item.emerald, 1)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), new ItemStack(GrowthCraftGrapes.grapeWine, 1, 1)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2 + random.nextInt(2)), new ItemStack(GrowthCraftGrapes.grapeWine, 1, 2)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2 + random.nextInt(2)), new ItemStack(GrowthCraftGrapes.grapeWine, 1, 3)));
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) 
	{
		return new PieceWeight(ComponentVillageGrapeVineyard.class, 21, MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i));
	}

	@Override
	public Class<?> getComponentClass() 
	{
		return ComponentVillageGrapeVineyard.class;
	}

	@Override
	public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) 
	{
		return ComponentVillageGrapeVineyard.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}
}
