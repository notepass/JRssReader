package de.notepass.general.Cache;


/**
 * <p>This class features the possibility to cache Objects and access them faster. Cached Objects are Globally aviable</p>
 */
public class GlobalCache {
    /**
     * <p>The used Cache-Object which is from now on globally aviable</p>
     */
    private static DynamicCache cache = new DynamicCache();

    /**
     * <p>Returns the global cache-object</p>
     * @return DynamicCache - The cache
     */
    public static DynamicCache getCache() {
        return cache;
    }
}
