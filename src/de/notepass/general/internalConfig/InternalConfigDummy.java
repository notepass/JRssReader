package de.notepass.general.internalConfig;

import de.notepass.general.objects.gui.GroupBox;
import de.notepass.general.objects.gui.StatusBarItem;
import de.notepass.general.objects.gui.TitleBar;
import de.notepass.general.util.Util;
import javafx.geometry.Insets;

/**
 * <p>This class contains the default Internal Configuration.
 * If you can't change something in the config-file you can hopefully here</p>
 */
public class InternalConfigDummy {
    /**
     * <p>Timeout before two mouse clicks in a row wont be seen as a double-click anymore
     * Will be used, when I will start making my own total UI</p>
     */
    public static double doubleClickTimeout=1000;


    //Folder Configuration
    /**
     * Root-Folder in which all data will be written
     */
    public static String rootFolder = "data";
    /**
     * Folder for configuration scripts. Will inherit from {@link de.notepass.general.internalConfig.InternalConfigDummy#rootFolder}.
     */
    public static String configRoot = rootFolder+"/conf";
    /**
     * Root-Folder for language files.
     */
    public static String langRoot=rootFolder+"/language";



    //Config for the logger
    /**
     * Folder for configuration of the logger. Will inherit from {@link de.notepass.general.internalConfig.InternalConfigDummy#rootFolder}.
     */
    public static String logConfPath=rootFolder+"/conf/log_conf.xml";
    /**
     * Root XPath node for the log-Configuration.
     */
    public static String logXMLlog_config="/log_config";
    /**
     * XPath for the Localisation of the Log-File.
     */
    public static String logXMLpath="/path";
    /**
     * XPath for telling wether "Debug"-level messages should be logged or not.
     */
    public static String logXMLlogDebug="/logDebug";
    /**
     * XPath for telling wether "Info"-level messages should be logged or not.
     */
    public static String logXMLlogInfo="/logInfo";
    /**
     * XPath for telling wether "Warning"-level messages should be logged or not.
     */
    public static String logXMLlogWarn="/logWarn";
    /**
     * XPath for telling wether "Error"-level messages should be logged or not.
     */
    public static String logXMLlogError="/logError";
    /**
     * XPath for the prefix of "Debug"-level messages.
     */
    public static String logXMLlogDebugText="/logDebugText";
    /**
     * XPath for the prefix of "Info"-level messages.
     */
    public static String logXMLlogInfoText="/logInfoText";
    /**
     * XPath for the prefix of "Warning"-level messages.
     */
    public static String logXMLlogWarnText="/logWarnText";
    /**
     * XPath for the prefix of "Error"-level messages.
     */
    public static String logXMLlogErrorText="/logErrorText";
    /**
     * XPath for the date formatting.
     */
    public static String logXMLdateTimeFormat="/dateTimeFormat";
    /**
     * XPath for the prefix of the date.
     */
    public static String logXMLdateTimePrefix="/dateTimePrefix";
    /**
     * XPath for the suffix of the date.
     */
    public static String logXMLdateTimeSuffix="/dateTimeSuffix";



    //GUI Configuration
    /**
     * CSS-Files needed to be loaded for the new JavaFX-Elements
     */
    public static String[] cssFiles = {GroupBox.cssFile, StatusBarItem.cssFile, Util.createLoadString("style/General.css"), TitleBar.cssFile};
    /**
     * Default-Padding value for the FX-GUI
     */
    public static Insets guiDefaultPadding = new Insets(10,10,10,10);
    /**
     * Default VGap for the GridPanes
     */
    public static double guiDefaultVGap = 10;
    /**
     * Default HGap for the GridPanes
     */
    public static double guiDefaultHGap = 10;
    /**
     * Default Spacing for the GridPanes
     */
    public static double guiDefaultSpacing = 5;



    //Config for GroupBoxes
    /**
     * Default VGap for the GridPane of the groupBox-Element
     */
    public static double groupBoxDefaultVGap = 5;
    /**
     * Default HGap for the GridPane of the groupBox-Element
     */
    public static double groupBoxDefaultHGap = 5;
    /**
     * Default padding for the GridPane of the groupBox-Element
     */
    public static Insets groupBoxDefaultPadding = new Insets(5,5,5,5);
}
