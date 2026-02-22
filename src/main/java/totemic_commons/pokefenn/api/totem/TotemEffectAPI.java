package totemic_commons.pokefenn.api.totem;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

/**
 * Provides access to functionality commonly used for Totem Effects.
 * Use <tt>TotemicAPI.get().totemEffect()</tt> to get an instance of this interface.
 */
public interface TotemEffectAPI
{
    /**
     * Calculates how long a potion effect from a Totem Pole should last by default.
     * The value is calculated based on the parameters to this method.
     * @param baseTime the base time
     * @param isPositive whether the effect is positive for the player
     * @param melodyAmount the amount of musical melody the totem pole where the effect originates has
     * @return the default time how long a potion effect from a Totem effect lasts
     */
    public int getDefaultPotionTime(int baseTime, boolean isPositive, Random rand, int melodyAmount, int totemWoodBonus, int repetitionBonus);

    /**
     * Calculates how strong a potion effect from a Totem Pole should be by default.
     * The value is calculated based on the parameters to this method.
     * @param baseStrength the base strength
     * @param isPositive whether the effect is positive for the player
     * @param melodyAmount the amount of musical melody the totem pole where the effect originates has
     * @return the default strength of a potion effect from a Totem effect
     */
    public int getDefaultPotionStrength(int baseStrength, boolean isPositive, Random rand, int melodyAmount, int totemWoodBonus, int repetitionBonus);

    /**
     * Adds a potion effect to the player, whose time and strength
     * are calculated from getDefaultPotionTime() and getDefaultPotionStrength()
     * @param baseTime the base time
     * @param baseStrength the base strength
     * @param isPositive whether the effect is positive for the player
     * @param melodyAmount the amount of musical melody the totem pole where the effect originates has
     */
    public void addPotionEffect(EntityPlayer player, Potion potion, boolean isPositive, int baseTime, int baseStrength, int melodyAmount, int totemWoodBonus, int repetitionBonus);
}
