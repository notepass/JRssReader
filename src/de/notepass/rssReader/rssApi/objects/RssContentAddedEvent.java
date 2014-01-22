package de.notepass.rssReader.rssApi.objects;

import java.util.EventObject;

/**
 *<p>The event that is triggered when content in the RSS has been Changed or Content is added</p>
 */
public class RssContentAddedEvent extends EventObject {
    //private Rss rss;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RssContentAddedEvent(Object source, Rss rss) {
        super(source);
        //this.rss = rss;
    }

    /*
    public Rss rss() {
        return this.rss;
    }
    */
}
