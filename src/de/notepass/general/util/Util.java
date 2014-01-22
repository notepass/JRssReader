package de.notepass.general.util;

import de.notepass.general.logger.Log;
import de.notepass.general.objects.gui.GroupBox;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.notepass.general.internalConfig.InternalConfigDummy;

//The Util-Class of this project. You will find random stuff here...
public class Util implements Serializable {

    /**
     * <p>This method returns the line-separator
     * in a Java-6 compatible way
     * On Java7 please use System.lineSeperator();</p>
     * @return System line separator
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * <p>This method can read a specified config-node</p>
     * @param node
     * @return Config-node
     */
    //This function can read the Config-File of the program
    public static String readConfig(String node) {
        try {
            return Util.nodeListToString(Util.executeXPath(InternalConfigDummy.configRoot+"/conf.xml","/config/"+node+"/text()"));
        } catch (Exception e) {
            Log.logError(e);
            Log.logError("Couldn't read the main Config file... Stopping...");
            System.exit(1);
        }
        return "";
    }

    /**
     * <p>This method generates the node list
     * matching to a XPath-Expression.
     * Hint: Use the nodeListToString-method to get the String value</p>
     * @param filepath
     * @param xpath_exp
     * @return All matches nodes
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    //This function executes a XPath-Statement (From a file)
    public static NodeList executeXPath(String filepath, String xpath_exp) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        //This Function will execute an XPath Expression
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        //This Loads the File...
        Document doc = builder.parse(filepath);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        //... and this will execute the XPath
        XPathExpression expr = xpath.compile(xpath_exp);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        //The Output is a NoteList
        return (NodeList) result;
    }

    /**
     * <p>This function executes an XPath on multible files
     * WARNING: This function doesn't work right</p>
     * @param filepath
     * @param xpath_exp
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    public static String [] executeXPath(String [] filepath, String xpath_exp) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        @Deprecated
        String [] worker = new String[filepath.length];
        for (int i=0;i<filepath.length;i++) {
            worker[i]=nodeListToString(executeXPath(filepath[i], xpath_exp));
        }
        return worker;
    }

    /**
     * <p>This function grabs the nodeList of a previous XPath execute
     * and executes another XPath on it</p>
     * @param nl
     * @param xpath_exp
     * @return XPath expression nodeList
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    //This function executes a XPath-Statement (From a NodeList)
    public static NodeList executeXPath(NodeList nl, String xpath_exp) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        //... and this will execute the XPath
        XPathExpression expr = xpath.compile(xpath_exp);
        Object result = expr.evaluate(nl, XPathConstants.NODESET);

        //The Output is a NoteList
        return (NodeList) result;
    }

    /**
     * <p>This converts a XPath to a String</p>
     * @param nodeList
     * @return String equivalent of nodeList
     */
    //This function converts an NodeList to a String
    @Deprecated
    public static String nodeListToString(NodeList nodeList) {
        String result = new String();
        //Read every Node and put it into the String
        for (int i = 0; i < nodeList.getLength(); i++) {
            result = result + nodeList.item(i).getNodeValue();
        }
        return result;
    }

    /**
     * <p>This is the new way to convert a nodelist to a string. It will create a array entry for every node</p>
     * @param nodeList Source node list
     * @return String[] - One array-slot per node
     */
    public static String[] nodeListToStringArray(NodeList nodeList) {
        ArrayList<String> nodes = new ArrayList<String>();
        //Read every Node and put it into the String
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i).getNodeValue());
        }
        return nodes.toArray(new String[nodes.size()]);
    }

    /**
     * <p>Checks if a file exists.
     * You can also use new File(String file).exists</p>
     * @param file
     * @return boolean
     */
    //Checks if a file exists
    public static boolean exist(String file) {
        File f = new File(file);
        return f.exists();
    }

    /**
     * <p>This function lists all FILES in a folder
     * No folders are listed (Difference to the list function of File)</p>
     * @param path
     * @return List of Files
     */
    //List all Files in a given folder
    public static String[] listFiles(String path)
    {
        String files;
        File dir = new File(path);
        File[] files1 = dir.listFiles();

        String [] filesList = new String[files1.length];

        for (int i = 0; i < files1.length; i++) {
            if (files1[i].isFile()) {
                filesList[i] = files1[i].getName();
            }
        }

        return filesList;
    }

    /**
     * <p>This method will delete the extension from a file name</p>
     * @param str
     * @return File name without extension
     */
    //Deletes the File-Extension of a Filename
    public static String deleteFileExtension(String str) {
        int lastIndex = str.toLowerCase().lastIndexOf(".");
        char [] charArray = new char[str.length()];
        charArray = str.toCharArray();
        String ret = "";
        for (int i=0;i<lastIndex;i++) {
            ret = ret + charArray[i];
        }
        return ret;
    }

    /**
     * <p>This method translates a text.
     * It needs the ID of the text to translate.
     * Normally there are many more of these methods, specialised for the class</p>
     * @param id
     * @return Translated text
     */
    //The raw translate routine. This is more an "abstract" routine. There are specialised methods in some classes (Mostly for replacing local variables)
    //The installer uses an other translation routine! Its in the Installer class!
    public static String translate(String id) {
        String langFile = readConfig("langFile");
        try {
            //Reads the Translation
            String worker = Util.nodeListToString(Util.executeXPath(InternalConfigDummy.langRoot+"/"+langFile,"/lang/"+id+"/text()"));
            worker = worker.replaceAll(Pattern.quote("\\r"),"\r");
            worker = worker.replaceAll(Pattern.quote("\\n"),"\n");
            return worker;
        } catch (Exception e) {
            Log.logError(e);
            Log.logWarning("Couldn't translate text with ID "+id+"... This is normaly an error in the language-file...");
        }
        return "Couldn't translate Text. See debug-console or log-file for details";
    }

    /**
     * <p>Saves an Object to a file (can only be used on serializable Objects)</p>
     * @param saveObject
     * @param path
     * @throws IOException
     */
    //Saves an Object to a file (Should just be used in Cases were you can't save a File as XML [Reason for Deprecated status])
    public static void saveObject(Object saveObject, String path) throws IOException {
        @Deprecated
  	    ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(path,false));
        o.writeObject(saveObject);
        o.close();
    }

    /**
     * <p>Loads an Object from a file (You need to know the type yourself)</p>
     * @param path
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    //Loads an Object from a file (Should just be used in Cases were you can't save a File as XML [Reason for Deprecated status])
    public static Object loadObject(String path) throws IOException, ClassNotFoundException {
        @Deprecated
        ObjectInputStream o = new ObjectInputStream(new FileInputStream(path));
        Object result = o.readObject();
        o.close();
        return result;
    }

    /**
     * <p>Executes a RegEx statement on a string</p>
     * @param source
     * @param regEx
     * @return RegEx manipulated String
     */
    //Function to execute RegEx, gives back the first String that matches the RegEx
    public static String execRegEx(String source, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);

        if (m.find()) {
            return source.substring(m.start(), m.end());
        } else {
            return null;
        }
    }

    /**
     * <p>Reads the content of a Text-File to a String</p>
     * @param pathname
     * @return Content of textfile
     * @throws IOException
     */
    //Reads a Text-File and converts it to a String
    public static String textFileToString(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    /**
     * <p>Executes a command as a Shell-Command</p>
     * @param command
     * @throws IOException
     * @throws InterruptedException
     */
    //Executes a Shell-Command
    public static void shellExec(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    /**
     * <p>Saves the content of a String as Textfile</p>
     * @param file
     * @param string
     * @throws FileNotFoundException
     */
    //Saves a String as Textfile
    public static void stringToTextfile(String file, String string) throws FileNotFoundException {
        PrintStream out = null;
        out = new PrintStream(new FileOutputStream(file));
        out.print(string);
        out.close();
    }

    /**
     * <p>Opens a file with it's registerted application</p>
     * @param file
     * @throws IOException
     */
    //opens a file with it's associated application
    public static void openWithStandartApp(String file) throws IOException {
        Desktop.getDesktop().open(new File(file));
    }

    /**
     * <p>Shows a standard error-dialog</p>
     * @param text
     * @param title
     */
    //Shows an Error Message
    public static void showError(String text, String title) {
        JOptionPane.showMessageDialog(new Frame(),translate("stdErrorPrefix")+text+translate("stdErrorSuffix"), title,JOptionPane.ERROR_MESSAGE);
    }

    /**
     * <p>Shows a standard error-dialog</p>
     * @param text
     */
    //Shows an Error Message
    public static void showError(String text) {
        showError(text, translate("stdErrorTitle"));
    }

    /**
     * <p>Shows a standard error-dialog</p>
     * @param e
     * @param title
     */
    public static void showError(Exception e, String title) {
        showError(exceptionToString(e),title);
    }

    /**
     * <p>Shows a standard error-dialog</p>
     * @param e
     */
    public static void showError(Exception e) {
        showError(e,translate("stdErrorTitle"));
    }

    /**
     * <p>Shows a plain error-dialog (No external Stuff will be read, just for emergency purpose)</p>
     * @param text
     * @param title
     */
    //This method shows only the given message (Only used when there is an initialisation error)
    public static void showPureError(String text, String title) {
        JOptionPane.showMessageDialog(new Frame(),text, title,JOptionPane.ERROR_MESSAGE);
    }

    /**
     * <p>Shows a plain error-dialog (No external Stuff will be read, just for emergency purpose)</p>
     * @param text
     */
    public static void showPureError(String text) {
        showPureError(text,"Fatal Error");
    }

    /**
     * <p>Translates a error to a String</p>
     * @param e
     * @return Error-String (Stacktrace)
     */
    public static String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * <p>Shows a standard warning-dialog</p>
     * @param text
     * @param title
     */
    public static void showWarning(String text, String title) {
        JOptionPane.showMessageDialog(new Frame(),translate("stdWarnPrefix")+text+translate("stdWarnSuffix"), title,JOptionPane.WARNING_MESSAGE);
    }

    /**
     * <p>Shows a standard info-dialog</p>
     * @param text
     * @param title
     */
    public static void showInfo(String text, String title) {
        JOptionPane.showMessageDialog(new Frame(),translate("stdInfoPrefix")+text+translate("stdInfoSuffix"), title,JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * <p>Shows a info-dialog without prefix and duffix</p>
     * @param text
     * @param title
     */
    public static void showPureInfo(String text, String title) {
        JOptionPane.showMessageDialog(new Frame(),text, title,JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * <p>Executes a method from a external jar</p>
     * @param AjarFile
     * @param sourceClass
     * @param method
     * @param parameterTypes
     * @param args
     * @return Return of method
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    //Executes an Method form an external Java-Application
    public static Object execMethod(File AjarFile, String sourceClass, String method, Class<?> [] parameterTypes, Object [] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //Load .jar Files
        String jarFilePath=AjarFile.getAbsolutePath();

        JarFile jarFile = new JarFile( jarFilePath/*path*/);
        Enumeration e = jarFile.entries();

        URL[] urls = {new URL("jar:file:"+jarFilePath+"!/")};
        URLClassLoader c1 = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace("/",".");
            Class<?> endClass = c1.loadClass(className);
            if (className.equals(sourceClass)) {
                Method m1 = endClass.getDeclaredMethod(method,parameterTypes);
                return m1.invoke(endClass.newInstance(),args);
            }
        }
        return null;
    }

    /**
     * <p>Older version of textfileToString</p>
     * @param pathname
     * @return
     * @throws IOException
     */
    public static String readTextFile(String pathname) throws IOException {
        @Deprecated
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder();
        Scanner scanner = new Scanner(file);
        String lineSeparator = Util.getLineSeparator();

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    /**
     * <p>An older version of stringToTextfile</p>
     * @param content
     * @param target
     * @param append
     * @throws IOException
     */
    public static void writeTextFile(String content, File target, boolean append) throws IOException {
        @Deprecated
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter(target.getAbsolutePath(),append));
            writer.write(content);
        }
        finally
        {
            if ( writer != null)
                writer.close( );
        }
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * <p>Resolves an XPath value to a File (eg.: Value: EUR, XPath: /currency/ISO4217, Search Folder: data/currnecy) would resolve to euro.xml</p>
     * @param xpath
     * @param value
     * @param searchFolder
     * @return Matching File
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    //Resolves an XPath value to a File (eg.: Value: EUR, XPath: /currency/ISO4217, Search Folder: data/currnecy) would resolve to euro.xml
    public static String xmlFileToXpathExpression(String xpath, String value, String searchFolder) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        String [] searchFiles = Util.listFiles(searchFolder);
        for (int i=0; i<searchFiles.length; i++) {
            if (Util.nodeListToString(Util.executeXPath(searchFolder+"/"+searchFiles[i],xpath)).equals(value)) {
                return searchFiles[i];
            }
        }
        return "";
    }

    /**
     * <p>Formats a load String so it is valid</p>
     * @param path
     * @return
     */
    //Formats a load String so it is valid
    public static String createLoadString(String path) {
        URL url = GroupBox.class.getResource(path);
        return url.toExternalForm();
    }

    /**
     * <p>Returns the Screen size of the Computer (without side/menu-bars)</p>
     * @return Screen size
     */
    //Get the REAL screen Size
    public static Dimension getScreenSize() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = ge.getMaximumWindowBounds();
        return bounds.getSize();
    }

    /**
     * <p>Copys a file</p>
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copy(File source, File destination) throws IOException {
        //File in = new File(source);
        //File out = new File(destination);
        FileChannel inChannel = new FileInputStream(source).getChannel();
        FileChannel outChannel = new FileOutputStream(destination).getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
    }

    /**
     * <p>Chekcs if an XML-File is valid</p>
     * @param xml XML-File to check
     * @return boolean - isValidXml
     */
    public static boolean isValidXml(File xml) {
        //This Function will execute an XPath Expression

        if (!xml.exists()) {
            return false;
        }

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return false;
        }
        //This Loads the File...
        try {
            Document doc = builder.parse(xml.getAbsolutePath());
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void download(URI source, File target) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(source.toURL().openStream());
        FileOutputStream fos = new FileOutputStream(target,false);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

}
