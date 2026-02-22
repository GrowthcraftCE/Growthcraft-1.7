package totemic_commons.pokefenn.api.totem;

import java.util.List;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import totemic_commons.pokefenn.api.TotemicAPI;

/**
 * Default implementation of a Totem Effect that adds a potion effect to nearby players
 */
public class TotemEffectPotion extends TotemEffect
{
    public final Potion potion;
    public final boolean isPositive;
    public final int interval;
    public final int defaultTime;
    public final int amplifier;

    public TotemEffectPotion(String modid, String baseName, int horizontal, int vertical, int tier, Potion potion, boolean isPositive, int interval, int defaultTime, int amplifier)
    {
        super(modid, baseName, horizontal, vertical, tier);
        this.potion = Objects.requireNonNull(potion);
        this.isPositive = isPositive;
        this.interval = interval;
        this.defaultTime = defaultTime;
        this.amplifier = amplifier;
    }

    /**
     * Alternative constructor for positive potion effects
     */
    public TotemEffectPotion(String modid, String baseName, int horizontal, int vertical, int tier, Potion potion, int interval, int defaultTime, int amplifier)
    {
        this(modid, baseName, horizontal, vertical, tier, potion, true, interval, defaultTime, amplifier);
    }

    @Override
    public void effect(World world, int x, int y, int z, int poleSize, int horizontal, int vertical, int melodyAmount, int totemWoodBonus, int repetitionBonus)
    {
        if(world.isRemote)
            return;

        if(world.getTotalWorldTime() % interval == 0)
        {

            for(EntityPlayer entity : getPlayersInRange(world, x, y, z, horizontal, vertical))
            {
                TotemicAPI.get().totemEffect().addPotionEffect(entity, potion, isPositive, defaultTime, amplifier, melodyAmount, totemWoodBonus, repetitionBonus);
            }
        }
    }

    public static List<EntityPlayer> getPlayersInRange(World world, int x, int y, int z, int horizontal, int vertical)
    {
        return world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - 0.5F, y - 0.5f, z - 0.5f, x + 0.5f, y + 0.5f, z + 0.5f).expand(horizontal, vertical, horizontal));
    }
}
