package de.notepass.rssReader.rssApi;

import de.notepass.general.util.Util;
import de.notepass.rssReader.config.InternalConfig;
import de.notepass.rssReader.rssApi.objects.Rss;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * <p>This class makes it possible to read/write the RSS-Configuration.
 * It's also in charge for saving and loading the configuration at program-startup/shutdown.
 * Please note that you have to call the {@link #init()}-method to make this class ready after program startup</p>
 */
public class RssConfiguration {

    /**
     * <p>Initiates the Cache for the Configuration. HAS to be called before usage</p>
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws URISyntaxException
     */
    public static void init() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, URISyntaxException {
        checkFiles();
        loadConfigFromDisk();
    }

    /**
     * <p>Contains all aviable rss-Urls and their IDs<br/>
     * The Object-Array is build up like this:<br/>
     * [0]=URL<br/>
     * [1]=UUID</p>
     */
    private static ArrayList<Object[]> rssUrls = new ArrayList<Object[]>();

    /**
     * <p>Method to check the Files</p>
     */
    private static void checkFiles() throws IOException {
        if (!InternalConfig.RSS_URL_SAVE_FILE.exists()) {
            InternalConfig.RSS_URL_SAVE_FILE.getParentFile().mkdirs();
            InternalConfig.RSS_URL_SAVE_FILE.createNewFile();
        }

        if (!InternalConfig.RSS_ROOT.exists()) {
            InternalConfig.RSS_ROOT.mkdirs();
        }

        if (!InternalConfig.RSS_TEMP_ROOT.exists()) {
            InternalConfig.RSS_TEMP_ROOT.mkdirs();
        }
    }

    /**
     * <p>Returns the URLs to all saved RSS-Feeds</p>
     * @return URL[] - URLs to all aviable RSS-Files
     */
    public static URI[] getRssUris() {
        ArrayList<URI> rssURIs = new ArrayList<URI>();
        for (Object[] rssObject:rssUrls) {
            rssURIs.add((URI)rssObject[0]);
        }
        return rssURIs.toArray(new URI[rssURIs.size()]);
    }

    /**
     * <p>Returns the URL to a specified RSS-Feed</p>
     * @param id UUID of the RSS-Feed
     * @return URL - URL of the RSS-Feed
     */
    public static URI getRSSUri(UUID id) {
        for (Object[] rssElement:rssUrls) {
            if (((UUID)rssElement[1]).equals(id)) {
                return (URI)rssElement[0];
            }
        }
        return null;
    }

    /**
     * <p>Return the UUIDs of all aviable RSS-Feeds.
     * The output is sorted in the same way the output of {@link #getRssIds()} is sorted</p>
     * @return UUID[] - List of all RSS-UUIDs
     */
    public static UUID[] getRssIds() {
        ArrayList<UUID> rssURIs = new ArrayList<UUID>();
        for (Object[] rssObject:rssUrls) {
            rssURIs.add((UUID)rssObject[1]);
        }
        return rssURIs.toArray(new UUID[rssURIs.size()]);
    }

    /*
    /**
     * <p>Refreshes the saved RSS-Configuration (It's cached, so that it's faster aviable)
     * Will automatic be called if an RSS-Feed is added through the API</p>

    public static void readRssFileConfig() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, URISyntaxException {
        checkFiles();
        int i=0;
        String rssUUID = Util.nodeListToString(Util.executeXPath(InternalConfig.RSS_URL_SAVE_FILE.getAbsolutePath(),"/root/rssFile"+i+"/UUID/text()"));
        String rssURL = Util.nodeListToString(Util.executeXPath(InternalConfig.RSS_URL_SAVE_FILE.getAbsolutePath(), "/root/rssFile" + i + "/URL/text()"));
        while ( ((rssUUID != null) && (!rssUUID.equals(""))) && ( (rssURL != null) && (!rssUUID.equals(""))) ) {
            rssUrls.add(new Object[]{new URI(rssURL),UUID.fromString(rssUUID)});
            i++;
            rssUUID = Util.nodeListToString(Util.executeXPath(InternalConfig.RSS_URL_SAVE_FILE.getAbsolutePath(),"/root/rssFile"+i+"/UUID/text()"));
            rssURL = Util.nodeListToString(Util.executeXPath(InternalConfig.RSS_URL_SAVE_FILE.getAbsolutePath(), "/root/rssFile" + i + "/URL/text()"));
        }
    }
    */

    /**
     * <p>Saves the RSS-Feeds to a File</p>
     */
    private static void writeRssFileConfig(/*URI[] RssUrls, UUID[] RssUuids*/) throws IOException, ParserConfigurationException, TransformerException {
        checkFiles();
        /*
        //Create a documentBuilder instanz
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        //Create an Document
        Document doc = db.newDocument();

        //Create a root-Element
        Element XMLroot = doc.createElement("root");

        for (int i=0;i<rssUrls.size();i++) {
            //Add the main Information to the root-element
            Element XMLrssFile = doc.createElement("rssFile");

            Element XMLrssUrl = doc.createElement("URL");
            XMLrssUrl.appendChild(doc.createTextNode(((URI)rssUrls.get(i)[0]).toString()));

            Element XMLrssUuid = doc.createElement("UUID");
            XMLrssUuid.appendChild(doc.createTextNode(((UUID)rssUrls.get(i)[1]).toString()));

            XMLrssFile.appendChild(XMLrssUrl);
            XMLrssFile.appendChild(XMLrssUuid);
            XMLroot.appendChild(XMLrssFile);
        }

        //Add the root-element to the document
        doc.appendChild(XMLroot);

        //Save as XML-File
        //Transform from document to FileStream
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        DOMSource ds = new DOMSource(doc);
        StreamResult sr = new StreamResult(InternalConfig.RSS_URL_SAVE_FILE);

        //Save File to Disc
        trans.transform(ds,sr);
        */
        Properties properties = new Properties();
        FileOutputStream fos = new FileOutputStream(InternalConfig.RSS_URL_SAVE_FILE);
        String mergedUrls = "";
        String mergedUuids = "";
        for (Object[] rssData:rssUrls) {
            mergedUrls = mergedUrls + ((URI)rssData[0]).toString() + ";";
            mergedUuids = mergedUuids + ((UUID)rssData[1]).toString() + ";";
        }
        properties.setProperty("mergedUrls",mergedUrls);
        properties.setProperty("mergedUuids",mergedUuids);
        properties.store(fos,"Automatic generated file. Do NOT touch");

    }

    /**
     * <p>Returns all Configured RSS-Feeds as Rss-Objects</p>
     * @return Rss[] - All aviable RSS-Feeds
     */
    public static Rss[] getRssObjects() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        URI rssUris[] = getRssUris();
        UUID rssUuids[] = getRssIds();
        ArrayList<Rss> rssObjects = new ArrayList<Rss>();
        for (int i=0;i<rssUris.length;i++) {
            rssObjects.add(new Rss(rssUris[i],rssUuids[i]));
        }
        return rssObjects.toArray(new Rss[rssObjects.size()]);
    }

    /**
     * <p>Returns a Rss-Object for a specified RSS-Feed</p>
     * @param id - UUID of the RSS-Feed
     * @return Rss - Rss-Object of Rss-Feed with given Id
     */
    public static Rss getRssObject(UUID id) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        return new Rss(getRSSUri(id),id);
    }

    /**
     * <p>Adds an RSS-Feed to the list and gives it a UUID</p>
     * @param Url - Url to the RSS-Feed
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public static boolean addRssFeed(URI Url) throws IOException, TransformerException, ParserConfigurationException {
        checkFiles();
        downloadRss(Url,new File(InternalConfig.RSS_TEMP_ROOT.getAbsoluteFile()+"/temp.rss"));
        if (Util.isValidXml(new File(InternalConfig.RSS_TEMP_ROOT.getAbsoluteFile()+"/temp.rss"))) {
            rssUrls.add(new Object[]{Url,UUID.randomUUID()});
            writeRssFileConfig();
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Reloads the configuration into the cache</p>
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void loadConfigFromDisk() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, URISyntaxException {
        checkFiles();
        /*
        ArrayList<URI> rssUriLocal = new ArrayList<URI>();
        for (String Url:Util.nodeListToStringArray(Util.executeXPath(InternalConfig.RSS_URL_SAVE_FILE.getAbsolutePath(),"/root//rssFile/URL/text()"))) {
            rssUriLocal.add(new URI(Url));
        }

        ArrayList<UUID> rssUuidLocal = new ArrayList<UUID>();
        for (String Uuid:Util.nodeListToStringArray(Util.executeXPath(InternalConfig.RSS_URL_SAVE_FILE.getAbsolutePath(),"/root//rssFile/UUID/text()"))) {
            rssUuidLocal.add(UUID.fromString(Uuid));
        }

        rssUrls.clear();
        for (int i=0;i<rssUriLocal.size();i++) {
            rssUrls.add(new Object[]{rssUriLocal.get(i),rssUuidLocal.get(i)});
        }
        */
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(InternalConfig.RSS_URL_SAVE_FILE);
        properties.load(fis);
        if ((properties.getProperty("mergedUrls") != null) && (properties.getProperty("mergedUuids") != null)) {
            if ((!properties.getProperty("mergedUrls").equals("")) && (!properties.getProperty("mergedUuids").equals(""))) {
                rssUrls.clear();
                ArrayList<URI> tempUri = new ArrayList<>();
                for (String url:properties.getProperty("mergedUrls").split(Pattern.quote(";"))) {
                    if (!url.equals("")) {
                        tempUri.add(new URI(url));
                    }
                }

                ArrayList<UUID> tempUuid = new ArrayList<>();
                for (String uuid:properties.getProperty("mergedUuids").split(Pattern.quote(";"))) {
                    if (!uuid.equals("")) {
                        tempUuid.add(UUID.fromString(uuid));
                    }
                }


                for (int i=0;i<tempUuid.size();i++) {
                    rssUrls.add(new Object[]{tempUri.get(i),tempUuid.get(i)});
                }
            }
        }

    }

    /**
     * <p>Alias for {@link #loadConfigFromDisk()}</p>
     * @throws URISyntaxException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    public static void refreshCache() throws URISyntaxException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        loadConfigFromDisk();
    }

    /**
     * <p>Deletes ALL Rss-Feeds</p>
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public static void clear() throws ParserConfigurationException, TransformerException, IOException {
        rssUrls.clear();
        writeRssFileConfig();
    }

    /**
     * <p>Removes a specified RSS-Feed from the configuration</p>
     * @param id UUID of the RSS-Feed to delete
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public static void remove(UUID id) throws ParserConfigurationException, TransformerException, IOException {
        ArrayList<Object[]> rssUrlsCopy = new ArrayList<Object[]>();
        for (Object[] scopeItem:rssUrls) {
            rssUrlsCopy.add(scopeItem);
        }
        rssUrls.clear();
        for (Object[] scopeItem:rssUrlsCopy) {
            if (!((UUID)scopeItem[1]).equals(id)) {
                rssUrls.add(scopeItem);
            }
        }
        writeRssFileConfig();
    }

    public static boolean isValidRss(File xml) {
        return Util.isValidXml(xml);
    }

    private static void downloadRss(URI rssUri,File localFile) throws IOException {
        try {
            ReadableByteChannel rbc = Channels.newChannel(rssUri.toURL().openStream());
            FileOutputStream fos = new FileOutputStream(localFile,false);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {

        }
    }
}
