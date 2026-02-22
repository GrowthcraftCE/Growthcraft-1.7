package totemic_commons.pokefenn.api;

import java.util.List;
import java.util.Map;

import totemic_commons.pokefenn.api.ceremony.Ceremony;
import totemic_commons.pokefenn.api.music.MusicInstrument;
import totemic_commons.pokefenn.api.totem.TotemEffect;
import vazkii.botania.totemic_custom.api.lexicon.LexiconCategory;
import vazkii.botania.totemic_custom.api.lexicon.LexiconEntry;

/**
 * Provides access to Totemic's registries.
 * Use <tt>TotemicAPI.get().registry()</tt> to get an instance of this interface.
 */
public interface TotemicRegistry
{
    /**
     * Adds a new totem effect
     * @return effect
     */
    public TotemEffect addTotem(TotemEffect effect);

    /**
     * Gets a totem effect by its unlocalized name
     * @param name the unlocalized name, including the mod ID
     */
    public TotemEffect getTotem(String name);

    /**
     * @return an immutable map of all registered totem effects
     */
    public Map<String, TotemEffect> getTotems();

    /**
     * Adds a new music instrument
     * @return instrument
     */
    public MusicInstrument addInstrument(MusicInstrument instrument);

    /**
     * Gets a music instrument by its unlocalized name
     * @param name the unlocalized name, including the mod ID
     */
    public MusicInstrument getInstrument(String name);

    /**
     * @return an immutable map of all registered instruments
     */
    public Map<String, MusicInstrument> getInstruments();

    /**
     * Adds a new ceremony
     * @return ceremony
     */
    public Ceremony addCeremony(Ceremony ceremony);

    /**
     * Gets a ceremony by its unlocalized name
     * @param name the unlocalized name, including the mod ID
     */
    public Ceremony getCeremony(String name);

    /**
     * @return an immutable map of all registered ceremonies
     */
    public Map<String, Ceremony> getCeremonies();

    /**
     * Adds a new category to the Totempedia
     * @return cat
     */
    public LexiconCategory addCategory(LexiconCategory cat);

    /**
     * @return an immutable list of all Totempedia categories
     */
    public List<LexiconCategory> getCategories();

    /**
     * Adds a new Totempedia entry to the given category
     */
    public void addLexiconEntry(LexiconCategory category, LexiconEntry entry);
}
