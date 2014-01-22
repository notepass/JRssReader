package de.notepass.rssReader.config;

import de.notepass.general.internalConfig.InternalConfigDummy;

import java.io.File;

/**
 *<p>Specialised Internal Configuration</p>
 */
public class InternalConfig extends InternalConfigDummy {
    /**
     * <p>Root-Folder for all RSS-specific stuff</p>
     */
    public static File rssRoot = new File(rootFolder+"/rss");

    /**
     * <p>Folder for all RSS-Relevant configuration</p>
     */
    public static File rssConfigRoot = new File(new File(configRoot).getAbsolutePath());

    /**
     * <p>File to save the IDs/URLs of the RSS-Feed</p>
     */
    public static File rssUrlSaveFile = new File(rssConfigRoot.getAbsolutePath()+"/rss.xml");

    /**
     * <p>Temporary place to save downloaded RSS-Files</p>
     */
    public static File rssTmpRoot = new File(rssRoot.getAbsolutePath()+"/temp");

}
