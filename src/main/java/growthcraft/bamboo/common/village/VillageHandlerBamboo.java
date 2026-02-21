package growthcraft.bamboo.common.village;

import java.util.List;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import growthcraft.bamboo.GrowthCraftBamboo;

public class VillageHandlerBamboo implements IVillageCreationHandler {

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int i) {
        int num = MathHelper.getRandomIntegerInRange(random, i, 1 + i);
        if (!GrowthCraftBamboo.getConfig().generateBambooYard) num = 0;

        return new PieceWeight(ComponentVillageBambooYard.class, 21, num);
    }

    @Override
    public Class<?> getComponentClass() {
        return ComponentVillageBambooYard.class;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2,
        int p3, int p4, int p5) {
        return ComponentVillageBambooYard.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
    }
}
