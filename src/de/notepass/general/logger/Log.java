package de.notepass.general.logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import de.notepass.general.internalConfig.InternalConfigDummy;
import de.notepass.general.util.Util;
import de.notepass.rssReader.config.InternalConfig;

//This is the Log Class. It contains every log function

/**
 * <p>This class contains the Log-Functions for my programs</p>
 */
public class Log {

    //With this function the Configuration is read

    /**
     * <p>Read a specified node of the configuration</p>
     * @param part Node to read
     * @return String - Content of the node
     */
    private static String readConfig(String part) {
        try {
            FileInputStream fis = new FileInputStream(InternalConfigDummy.CONFIG_FILE);
            Properties logProperties = new Properties();
            logProperties.load(fis);
            return logProperties.getProperty(InternalConfigDummy.CONFIG_LOG_PREFIX+part);
        } catch (Exception e) {
            Util.showPureError("Couldn't read the configuration file. Exception is: "+e.getCause()+" Stacktrace is:"+Util.getLineSeparator()+Util.exceptionToString(e)); //TODO: Translate
            System.exit(1);
        }
        return null;
    }

    //This function gives out the Formatted log-Time-Format

    /**
     * <p>This functions formats the Date/Time according to the configuration</p>
     * @return String - Formatted Time/Date
     */
    private static String giveLogTimeFormatted() {
        Date logTime = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat();
        //The pattern for output is read from the config-File
        formatDate.applyPattern(readConfig(InternalConfigDummy.CONFIG_LOG_DATETIMEFORMAT));
        //And there it goes. As String of course
        return readConfig(InternalConfigDummy.CONFIG_LOG_DATETIMEPREFIX)+formatDate.format(logTime)+readConfig(InternalConfigDummy.CONFIG_LOG_DATETIMESUFFIX);
    }

    //This function will bring a logged text into an Textfile

    /**
     * <p>This method saves the logging text in the configured log-file</p>
     * @param text Text to save in the textfile
     */
    private static void logTextToFile(String text) {
        //Declare Variables
        FileOutputStream logFileStream = null;
        //reads the path for the output file from the Config
        String filePath = readConfig(InternalConfigDummy.CONFIG_LOG_FILEPATH);
        //Make folder/files
        new File(filePath).getParentFile().mkdirs();
        if (!(new File(filePath).exists())) {
            try {
                new File(filePath).createNewFile();
            } catch (IOException e) {
                Util.showPureError("Couldn't create the log file. Will now exit"+Util.getLineSeparator()+Util.exceptionToString(e));
                System.exit(1);
            }
        }
        //Add an break to the Text, so it breaks lines in the textfile
        text = text + "\r\n";
        try {
            //This is a standard output procedure...
            logFileStream = new FileOutputStream(filePath, true);
            for (int i=0; i<text.length(); i++) {
                logFileStream.write((byte) text.charAt(i));
            }
        } catch (FileNotFoundException e) {
            //If the file doesn't exist, we try to create it
            File file = new File(filePath);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                //And if it doesn't work, we give out an STacktrace and an understandable error Message
                ex.printStackTrace();
                System.out.println("Error: Couldn't open the log-File. Will now exit...");
                Util.showPureError("Error: Couldn't open the log-File. Will now exit..."+Util.getLineSeparator()+"Stacktrace:"+Util.getLineSeparator()+Util.getLineSeparator()+Util.exceptionToString(e));
                System.exit(1);
            }
        } catch (IOException e) {
            //If the file can't be open for another reason, we will give out a StackTrace and an understandable error message
            e.printStackTrace();
            System.out.println("Error: Couldn't open the log-File. Will now exit...");
            Util.showPureError("Error: Couldn't open the log-File. Will now exit..."+Util.getLineSeparator()+"Stacktrace:"+Util.getLineSeparator()+Util.getLineSeparator()+Util.exceptionToString(e));
            System.exit(1);
        } finally {
            try {
                //Whatever happen, close the Stream...
                logFileStream.close();
            } catch (IOException ex) {
                //Except when there is an error... (Jup, this totaly doesn't make any sense...)
                ex.printStackTrace();
                System.out.println("Error: Couldn't open the log-File. Will now exit...");
                Util.showPureError("Error: Couldn't open the log-File. Will now exit..."+Util.getLineSeparator()+"Stacktrace:"+Util.getLineSeparator()+Util.getLineSeparator()+Util.exceptionToString(ex));
                System.exit(1);
            }
        }
    }

    //The function to log an Debug-Message

    /**
     * <p>This method logs a "Debug"-level message</p>
     * @param logText - Text to log
     */
    public static void logDebug(String logText) {
        //Does the config tell us to save it?
        if (readConfig(InternalConfigDummy.CONFIG_LOG_LOGDEBUG).toLowerCase().equals("true")) {
            //If yes, we will output it in the Shell and the log-File
            System.out.println(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_DEBUGTEXT) + logText);
            logTextToFile(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_DEBUGTEXT) + logText);
        }
    }

    //The function to log an Info-Message
    /**
     * <p>This method logs a "Info"-level message</p>
     * @param logText - Text to log
     */
    public static void logInfo(String logText) {
        //Does the config tell us to save it?
        if (readConfig(InternalConfigDummy.CONFIG_LOG_LOGINFO).toLowerCase().equals("true")) {
            //If yes, we will output it in the Shell and the log-File
            System.out.println(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_INFOTEXT) + logText);
            logTextToFile(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_INFOTEXT) + logText);
        }
    }

    //This function logs a warning-message
    /**
     * <p>This method logs a "Warning"-level message</p>
     * @param logText - Text to log
     */
    public static void logWarning(String logText) {
        //Should we save it?
        if (readConfig(InternalConfigDummy.CONFIG_LOG_LOGWARN).toLowerCase().equals("true")) {
            //If yes, the output goes to the Shell and the File
            System.out.println(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_WARNTEXT) + logText);
            logTextToFile(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_WARNTEXT) + logText);
        }
    }

    /**
     * <p>This method logs a "Info"-level message</p>
     * @param logText - Text to log
     */
    public static void logInfo(String [] logText) {
        if (readConfig(InternalConfigDummy.CONFIG_LOG_LOGINFO).toLowerCase().equals("true")) {
            logInfo(logText[0]);
            for (int i=1; i<logText.length; i++) {
                System.out.println("\t" + readConfig(InternalConfigDummy.CONFIG_LOG_INFOTEXT) + logText);
                logTextToFile("\t" + readConfig(InternalConfigDummy.CONFIG_LOG_INFOTEXT) + logText);
            }
        }
    }

    /**
     * <p>This method logs a "Warning"-level message</p>
     * @param logText - Text to log
     */
    public static void logWarning(String [] logText) {
        if (readConfig(InternalConfigDummy.CONFIG_LOG_LOGWARN).toLowerCase().equals("true")) {
            logWarning(logText[0]);
            for (int i=1; i<logText.length; i++) {
                System.out.println("\t" + readConfig(InternalConfigDummy.CONFIG_LOG_WARNTEXT) + logText);
                logTextToFile("\t" + readConfig(InternalConfigDummy.CONFIG_LOG_WARNTEXT) + logText);
            }
        }
    }

    //This is the logger for the Error-Messages
    /**
     * <p>This method logs a "Error"-level message</p>
     * @param logText - Text to log
     */
    public static void logError(String logText) {
        //Should we save them?
        if (readConfig(InternalConfigDummy.CONFIG_LOG_LOGERROR).toLowerCase().equals("true")) {
            //If yes, put the Text in the Shell and the File
            System.out.println(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_ERRORTEXT) + logText);
            logTextToFile(giveLogTimeFormatted() + readConfig(InternalConfigDummy.CONFIG_LOG_ERRORTEXT) + logText);
        }
    }

    /**
     * <p>This method logs a "Error"-level message</p>
     * @param logText - Text to log
     */
    public static void logError(String [] logText) {
        if (readConfig(InternalConfigDummy.CONFIG_LOG_LOGERROR).toLowerCase().equals("true")) {
            logError(logText[0]);
            for (int i=1; i<logText.length; i++) {
                System.out.println("\t" + readConfig(InternalConfigDummy.CONFIG_LOG_ERRORTEXT) + logText);
                logTextToFile("\t" + readConfig(InternalConfigDummy.CONFIG_LOG_ERRORTEXT) + logText);
            }
        }
    }

    /**
     * <p>This method logs a "Error"-level message from an exception</p>
     * @param ex - Exception to log
     */
    public static void logError(Exception ex) {
        logError(Util.exceptionToString(ex));
    }

}
