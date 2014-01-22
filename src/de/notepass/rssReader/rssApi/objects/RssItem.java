package de.notepass.rssReader.rssApi.objects;

public class RssItem {
    private String title = null;
    private String description = null;
    private String link = null;
    private String date = null;

    public RssItem(String title, String description, String link, String date) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription () {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String toString() {
        return getTitle();
    }
}
