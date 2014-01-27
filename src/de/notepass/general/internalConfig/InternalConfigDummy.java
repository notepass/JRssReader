package de.notepass.general.internalConfig;

import de.notepass.general.objects.gui.GroupBox;
import de.notepass.general.objects.gui.StatusBarItem;
import de.notepass.general.objects.gui.TitleBar;
import de.notepass.general.util.Util;
import javafx.geometry.Insets;

import java.io.File;

/**
 * <p>This class contains the default Internal Configuration.
 * If you can't change something in the config-file you can hopefully here</p>
 */
public class InternalConfigDummy {
    /**
     * <p>Timeout before two mouse clicks in a row wont be seen as a double-click anymore
     * Will be used, when I will start making my own total UI</p>
     */
    final public static double DOUBLIC_CLICK_TIMEOUT =1000;


    //Folder Configuration
    /**
     * Root-Folder in which all data will be written
     */
    final public static String ROOT_FOLDER = "data";
    /**
     * Folder for configuration scripts. Will inherit from {@link de.notepass.general.internalConfig.InternalConfigDummy#ROOT_FOLDER}.
     */
    final public static String CONFIG_ROOT = ROOT_FOLDER +"/conf";
    /**
     * Root-Folder for language files.
     */
    final public static String LANG_ROOT = ROOT_FOLDER +"/language";



    //Config for the logger
    /**
     * Folder for configuration of the logger. Will inherit from {@link de.notepass.general.internalConfig.InternalConfigDummy#ROOT_FOLDER}.
     */
//    final public static String LOG_CONF_PATH = ROOT_FOLDER +"/conf/log_conf.xml";
    /**
     * Root XPath node for the log-Configuration.
     */
//    public static String LOG_XML_LOG_CONFIG ="/log_config";
    /**
     * XPath for the Localisation of the Log-File.
     */
//    public static String LOG_XML_PATH ="/path";
    /**
     * XPath for telling wether "Debug"-level messages should be logged or not.
     */
//    public static String logXMLlogDebug="/logDebug";
    /**
     * XPath for telling wether "Info"-level messages should be logged or not.
     */
//    public static String logXMLlogInfo="/logInfo";
    /**
     * XPath for telling wether "Warning"-level messages should be logged or not.
     */
//    public static String logXMLlogWarn="/logWarn";
    /**
     * XPath for telling wether "Error"-level messages should be logged or not.
     */
//    public static String logXMLlogError="/logError";
    /**
     * XPath for the prefix of "Debug"-level messages.
     */
//    public static String logXMLlogDebugText="/logDebugText";
    /**
     * XPath for the prefix of "Info"-level messages.
     */
//    public static String logXMLlogInfoText="/logInfoText";
    /**
     * XPath for the prefix of "Warning"-level messages.
     */
//    public static String logXMLlogWarnText="/logWarnText";
    /**
     * XPath for the prefix of "Error"-level messages.
     */
//    public static String logXMLlogErrorText="/logErrorText";
    /**
     * XPath for the date formatting.
     */
//    public static String logXMLdateTimeFormat="/dateTimeFormat";
    /**
     * XPath for the prefix of the date.
     */
//    public static String logXMLdateTimePrefix="/dateTimePrefix";
    /**
     * XPath for the suffix of the date.
     */
//    public static String logXMLdateTimeSuffix="/dateTimeSuffix";



    //Properties Configuration
    /**
     * <p>Configuration File</p>
     */
    final public static File CONFIG_FILE = new File(CONFIG_ROOT+"/configuration.properties");
    /**
     * <p>The prefix for the log-properties</p>
     */
    final public static String CONFIG_LOG_PREFIX = "log.";
    /**
     *<p>The prefix for the main-properties</p>
     */
    final public static String CONFIG_MAIN_PREFIX = "main.";
    /**
     * <p>Config-properties for path of the log-file</p>
     */
    final public static String CONFIG_LOG_FILEPATH = "path";
    /**
     * <p>Config-properties for boolean value to decide if debug-messages should be logged</p>
     */
    final public static String CONFIG_LOG_LOGDEBUG = "debug";
    /**
     * <p>Config-properties for boolean value to decide if warning-messages should be logged</p>
     */
    final public static String CONFIG_LOG_LOGWARN = "warn";
    /**
     * <p>Config-properties for boolean value to decide if info-messages should be logged</p>
     */
    final public static String CONFIG_LOG_LOGINFO = "info";
    /**
     * <p>Config-properties for boolean value to decide if error-messages should be logged</p>
     */
    final public static String CONFIG_LOG_LOGERROR = "error";
    /**
     * <p>Config-properties for the debug-log-prefix</p>
     */
    final public static String CONFIG_LOG_DEBUGTEXT = "debugText";
    /**
     * <p>Config-properties for the warning-log-prefix</p>
     */
    final public static String CONFIG_LOG_WARNTEXT = "warnText";
    /**
     * <p>Config-properties for the info-log-prefix</p>
     */
    final public static String CONFIG_LOG_INFOTEXT = "infoText";
    /**
     * <p>Config-properties for the error-log-prefix</p>
     */
    final public static String CONFIG_LOG_ERRORTEXT = "errorText";
    /**
     * <p>Config-properties for the date/time format</p>
     */
    final public static String CONFIG_LOG_DATETIMEFORMAT = "dateTimeFormat";
    /**
     * <p>Config-properties for the date/time prefix</p>
     */
    final public static String CONFIG_LOG_DATETIMEPREFIX = "dateTimePrefix";
    /**
     * <p>Config-properties for the date/time suffix</p>
     */
    final public static String CONFIG_LOG_DATETIMESUFFIX = "dateTimeSuffix";
    /**
     * <p>Config-properties for the path of the language file</p>
     */
    final public static String CONFIG_MAIN_LANGFILE = "langFile";



    //GUI Configuration
    /**
     * CSS-Files needed to be loaded for the new JavaFX-Elements
     */
    final public static String[] CSS_FILES = {GroupBox.cssFile, StatusBarItem.cssFile, Util.createLoadString("style/General.css"), TitleBar.cssFile};
    /**
     * Default-Padding value for the FX-GUI
     */
    final public static Insets GUI_DEFAULT_PADDING = new Insets(10,10,10,10);
    /**
     * Default VGap for the GridPanes
     */
    final public static double GUI_DEFAULT_VGAP = 10;
    /**
     * Default HGap for the GridPanes
     */
    final public static double GUI_DEFAULT_HGAP = 10;
    /**
     * Default Spacing for the GridPanes
     */
    final public static double GUI_DEFAULT_SPACING = 5;



    //Config for GroupBoxes
    /**
     * Default VGap for the GridPane of the groupBox-Element
     */
    final public static double GROUPBOX_DEFAULT_VGAP = 5;
    /**
     * Default HGap for the GridPane of the groupBox-Element
     */
    final public static double GROUPBOX_DEFAULT_HGAP = 5;
    /**
     * Default padding for the GridPane of the groupBox-Element
     */
    final public static Insets GROUPBOX_DEFAULT_PADDING = new Insets(5,5,5,5);
}
