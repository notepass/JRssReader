package de.notepass.rssReader.rssApi.objects;

import de.notepass.general.logger.Log;
import de.notepass.general.util.Util;
import de.notepass.rssReader.config.InternalConfig;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 *<p>This Object represents a single RSS-Feed</p>
 */
public class Rss {

    private UUID rssUuid = null;
    private URI rssUri = null;
    private File localFile = null;
    private boolean autoUpdate = true;
    private long autoUpdateTime = 5000;
    private Timer autoUpdateTimer = new Timer();
    private TimerTask autoUpdateTimerTask;
    private List listeners = new ArrayList();
    boolean fireChangeEvent = true;
    String title;
    String description;
    String date;
    String link;


    /**
     * <p>Function to check if all needed Directories exists and to create missing</p>
     */
    private void checkFolders() {
        if (!InternalConfig.RSS_TEMP_ROOT.exists()) {
            InternalConfig.RSS_TEMP_ROOT.mkdirs();
        }
    }

    /**
     * <p>Function to download RSS-Feeds from teh internetz</p>
     * @return File - Downloaded File
     */
    private File downloadRss() throws IOException {
        checkFolders();
        Util.download(this.getUri(),this.localFile);
        return localFile;
    }

    /**
     * <p>Constructor to load an existing RSS-Feed without an UUID</p>
     * @param sourceURI The URI where the RSS-Feed is located
     */
    public Rss(URI sourceURI, UUID rssUuid) throws IOException, ParserConfigurationException, XPathExpressionException, SAXException {
        this.rssUri = sourceURI;
        this.rssUuid = rssUuid;
        this.localFile = new File(InternalConfig.RSS_TEMP_ROOT +"/"+rssUuid.toString()+".rss");
        update();

        this.autoUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (autoUpdate) {

                    //Variables for the content of the Rss-Feed (will just be used if a change-event should be triggered)
                    String[] oldTitles = null;
                    String[] newTitles = null;
                    String[] oldDescription = null;
                    String[] newDescription = null;
                    Integer oldNodeCount = null;
                    boolean isValidXml = Util.isValidXml(localFile);
                    if (fireChangeEvent && isValidXml) {
                        //Check for content changes/additions in the Item-Section of the RSS-Feed
                        try {
                            oldTitles = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/title/text()"));
                        } catch (Exception e) {
                            Log.logError("Error while trying to read "+localFile.getAbsolutePath());
                        }

                        try {
                            oldDescription = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/description/text()"));
                        } catch (Exception e) {
                            Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                        }

                        try {
                            oldNodeCount = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/text()")).length;
                        } catch (Exception e) {
                            Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                        }
                    }

                    if (autoUpdate) {
                        try {
                            updateLocalFile();
                        } catch (IOException e) {
                            Log.logError("Couldn't download RSS-Feed from server. Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                        }
                    }

                    isValidXml = Util.isValidXml(localFile);
                    if (fireChangeEvent && isValidXml) {
                        try {
                            newTitles = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/title/text()"));
                        } catch (Exception e) {
                            Log.logError("Error while trying to read "+localFile.getAbsolutePath());
                        }

                        try {
                            newDescription = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/description/text()"));
                        } catch (Exception e) {
                            Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                        }

                        //Make sure there were no errors while reading the values
                        if ( (oldDescription != null) && (newDescription != null) && (oldTitles != null) && (newTitles != null) ) {
                            //In this ArrayList, every MATCHING node will write a true. So if in this array list there are not enought values for all node, there was a change
                            ArrayList<Boolean> isAsPrevious = new ArrayList<Boolean>();
                            for (String oldCurrentDescription:oldDescription) {
                                for (String newCurrentDescription:newDescription) {
                                    if (oldCurrentDescription.equals(newCurrentDescription)) {
                                        isAsPrevious.add(true);
                                    }
                                }
                            }

                            for (String oldCurrentTitle:oldTitles) {
                                for (String newCurrentTitle:newTitles) {
                                    if (oldCurrentTitle.equals(newCurrentTitle)) {
                                        isAsPrevious.add(true);
                                    }
                                }
                            }

                            try {
                                Integer newNodeCount = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/text()")).length;
                                if (newNodeCount > oldNodeCount) {
                                    fire();
                                }
                            } catch (Exception e) {
                                Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                            }

                            if ( (oldDescription.length + oldTitles.length) != isAsPrevious.size()) {
                                //Fire the event if needed
                                fire();
                            }

                        }

                    }

                }
            }
        };




        this.autoUpdateTimer.scheduleAtFixedRate(this.autoUpdateTimerTask,this.autoUpdateTime,this.autoUpdateTime);
        TimerRegister.add(this.autoUpdateTimer);

        /*
        //TODO: Add Time read
        //TODO: Add comments
        //TODO: Change name
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(autoUpdateTime), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //Variables for the content of the Rss-Feed (will just be used if a change-event should be triggered)
                String[] oldTitles = null;
                String[] newTitles = null;
                String[] oldDescription = null;
                String[] newDescription = null;
                Integer oldNodeCount = null;
                boolean isValidXml = Util.isValidXml(localFile);
                if (fireChangeEvent && isValidXml) {
                    //Check for content changes/additions in the Item-Section of the RSS-Feed
                    try {
                        oldTitles = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/title/text()"));
                    } catch (Exception e) {
                        Log.logError("Error while trying to read "+localFile.getAbsolutePath());
                    }

                    try {
                        oldDescription = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/description/text()"));
                    } catch (Exception e) {
                        Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                    }

                    try {
                        oldNodeCount = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/text()")).length;
                    } catch (Exception e) {
                        Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                    }
                }

                if (autoUpdate) {
                    try {
                        updateLocalFile();
                    } catch (IOException e) {
                        Log.logError("Couldn't download RSS-Feed from server. Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                    }
                }

                isValidXml = Util.isValidXml(localFile);
                if (fireChangeEvent && isValidXml) {
                    try {
                        newTitles = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/title/text()"));
                    } catch (Exception e) {
                        Log.logError("Error while trying to read "+localFile.getAbsolutePath());
                    }

                    try {
                        newDescription = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/description/text()"));
                    } catch (Exception e) {
                        Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                    }

                    //Make sure there were no errors while reading the values
                    if ( (oldDescription != null) && (newDescription != null) && (oldTitles != null) && (newTitles != null) ) {
                        //In this ArrayList, every MATCHING node will write a true. So if in this array list there are not enought values for all node, there was a change
                        ArrayList<Boolean> isAsPrevious = new ArrayList<Boolean>();
                        for (String oldCurrentDescription:oldDescription) {
                            for (String newCurrentDescription:newDescription) {
                                if (oldCurrentDescription.equals(newCurrentDescription)) {
                                    isAsPrevious.add(true);
                                }
                            }
                        }

                        for (String oldCurrentTitle:oldTitles) {
                            for (String newCurrentTitle:newTitles) {
                                if (oldCurrentTitle.equals(newCurrentTitle)) {
                                    isAsPrevious.add(true);
                                }
                            }
                        }

                        try {
                            Integer newNodeCount = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/text()")).length;
                            if (newNodeCount > oldNodeCount) {
                                fire();
                            }
                        } catch (Exception e) {
                            Log.logError("Error while trying to read "+localFile.getAbsolutePath()+" Stack trace:"+Util.getLineSeparator()+Util.exceptionToString(e));
                        }

                        if ( (oldDescription.length + oldTitles.length) != isAsPrevious.size()) {
                            //Fire the event if needed
                            fire();
                        }

                    }

                }
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
        */

        updateLocalFile();
    }

    /**
     * <p>Returns the downloaded RSS-Feed as a File</p>
     * @return File - File of the Downloaded RSS-Feed
     */
    public File getRawRssContent() {
        return this.localFile;
    }

    /**
     * <p>Returns the MD5-Hash of the local file</p>
     * @return String - MD5-Hash of local file
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public String getMd5() throws NoSuchAlgorithmException, IOException {
        if (localFile.exists()) {
            String rssContent = Util.readTextFile(this.localFile.getAbsolutePath());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(rssContent.getBytes(), 0, rssContent.length());
            String signature = new BigInteger(1,md5.digest()).toString(16);
        return signature;
        } else {
            return null;
        }
    }

    /**
     * <p>Downloads the RSS-File from web</p>
     * @throws IOException
     */
    public void updateLocalFile() throws IOException {
        downloadRss();
    }

    /**
     * <p>Set if Auto-Updates of the file should be enabled</p>
     * @param autoUpdate - Boolean trigger for Auto-Updater
     */
    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    /**
     * <p>Return if wether or not Auto-Updating is enabled</p>
     * @return boolean - Auto update value
     */
    public boolean isAutoUpdating() {
        return this.autoUpdate;
    }

    /**
     * <p>Time for the updater to wait till the next update (Minimum value ist 1000)</p>
     * @param timeMs - Time in milliseconds before updating the local file
     */
    public void setAutoUpdateTime(long timeMs) {
        if (timeMs >= 1000) {
            this.autoUpdateTime = timeMs;
        }
    }

    /**
     * <p>Get the time of the Auto-Updater (default value is 5000ms)</p>
     * @return long - Auto-Updater sheudle time
     */
    public long getAutoUpdateTime() {
        return this.autoUpdateTime;
    }

    /**
     * <p>Returns the UUID of the Rss (Internal Id)</p>
     * @return UUID - UUID of the Rss
     */
    public UUID getUuid() {
        return this.rssUuid;
    }

    /**
     * <p>Sets if wether or not the RssContentAddedEvent should be fired</p>
     * @param fireChangeEvent
     */
    public void setFireChangeEvent(boolean fireChangeEvent) {
        this.fireChangeEvent = fireChangeEvent;
    }

    /**
     * <p>Returns a boolean value, wether the RssContentAddedEvent will be fired or not</p>
     * @return
     */
    public boolean getFireChangeEvent() {
        return this.fireChangeEvent;
    }

    /**
     * <p>Add a listener, that will be triggered, if the Rss-File is changed (Content added)</p>
     * @param chgLs Your change-listener
     */
    public synchronized void addChangeListener(RssContentAddedListener chgLs) {
        this.listeners.add(chgLs);
    }

    /**
     * <p>Removes a change listener</p>
     * @param chgLs Change listener to remove
     */
    public synchronized void removeChangeListener(RssContentAddedListener chgLs) {
        this.listeners.remove(chgLs);
    }

    /**
     * <p>Triggers the Rss-File changed event</p>
     */
    private synchronized void fire() {
        Iterator listeners = this.listeners.iterator();
        while( listeners.hasNext() ) {
            ( (RssContentAddedListener) listeners.next() ).RssChanged(this);
        }
    }

    /**
     * <p>Returns all aviable items as a RssItem-Array</p>
     * @return RssItem[] - All aviable Rss-Item nodes
     */
    public RssItem[] getRssItems() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        ArrayList<RssItem> items = new ArrayList<RssItem>();
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> dates = new ArrayList<String>();
        if (localFile.exists()) {
            for (String node:Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/title/text()"))) {
                titles.add(node);
            }


            for (String node:Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/description/text()"))) {
                descriptions.add(node);
            }


            for (String node:Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/link/text()"))) {
                links.add(node);
            }


            for (String node:Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//item/pubDate/text()"))) {
                dates.add(node);
            }

            for (int i=0;i<titles.size();i++) {
                items.add(new RssItem(titles.get(i),descriptions.get(i),links.get(i),dates.get(i)));
            }
        }

        return items.toArray(new RssItem[items.size()]);
    }

    /**
     * <p>Returns the title of the "channel"-node of the RSS</p>
     * @return String - Title of the Rss-Feed
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * <p>Returns the description of the "channel"-node of the RSS</p>
     * @return String - Description of the Rss-Feed
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * <p>Returns the link of the "channel"-node of the RSS</p>
     * @return String - Link of the Rss-Feed
     */
    public String getLink() {
        return this.link;
    }

    /**
     * <p>Returns the date of the "channel"-node of the RSS</p>
     * @return String - Date of the Rss-Feed
     */
    public String getDate() {
        return this.date;
    }

    /**
     * <p>Returns the Url of the Rss-Fees</p>
     * @return URI - URI of the Rss-Feed
     */
    public URI getUri() {
        return this.rssUri;
    }

    public String toString() {
        return this.getTitle();
    }

    /**
     * <p>Return wether or not this is an valid RSS-Feed/XML-File</p>
     * @return boolean - isValidRss
     */
    public boolean isValidRss() {
        return Util.isValidXml(this.localFile);
    }

    /**
     * <p>Downloads the Rss-Feed from the Internet an reloads the Local information</p>
     * @throws IOException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void update() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        checkFolders();
        updateLocalFile();

        //Because the Verification can sometime bug, it has to be tried a maximum of 25 times. If it is still false, then the document is not valid
        boolean validRss = isValidRss();

        if (validRss) {
            String[] titles = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//title/text()"));
            if (titles.length > 0) {
                this.title = titles[0];
            } else {
                this.title = null;
            }

            String[] descriptions = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//description/text()"));
            if (descriptions.length > 0) {
                this.description = descriptions[0];
            } else {
                this.description = null;
            }

            String[] links = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//link/text()"));
            if (links.length > 0) {
                this.link = links[0];
            } else {
                this.link = null;
            }

            String[] dates = Util.nodeListToStringArray(Util.executeXPath(localFile.getAbsolutePath(),"/rss/channel//pubDate/text()"));
            if (dates.length > 0) {
                this.date = dates[0];
            } else {
                this.date = null;
            }
        } else {
            this.date = null;
            this.link = null;
            this.description = null;
            this.title = null;
        }





    }
}
