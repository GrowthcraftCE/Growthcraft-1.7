package totemic_commons.pokefenn.api.music;

/**
 * An interface for tile entities that accept music
 * Currently only really implemented by TotemBase, but you may be able to get it working with other stuff.
 */
public interface MusicAcceptor
{
    /**
     * Adds up to amount music of the given instrument
     * @return how much music was actually added
     */
    public int addMusic(MusicInstrument instr, int amount);
}
