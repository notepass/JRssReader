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
    final public static File RSS_ROOT = new File(ROOT_FOLDER +"/rss");

    /**
     * <p>Folder for all RSS-Relevant configuration</p>
     */
    final public static File RSS_CONFIG_ROOT = new File(new File(CONFIG_ROOT).getAbsolutePath());

    /**
     * <p>File to save the IDs/URLs of the RSS-Feed</p>
     */
    final public static File RSS_URL_SAVE_FILE = new File(RSS_CONFIG_ROOT.getAbsolutePath()+"/rss.properties");

    /**
     * <p>Temporary place to save downloaded RSS-Files</p>
     */
    final public static File RSS_TEMP_ROOT = new File(RSS_ROOT.getAbsolutePath()+"/temp");

}
