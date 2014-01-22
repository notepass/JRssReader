package de.notepass.general.Cache;

import java.util.*;

/**
 * <p>This class features the possibility to cache Objects and access them faster</p>
 */
public class DynamicCache {
    /**
     * <p>Variable to save the given Objects</p>
     */
    private ArrayList<Object> content = new ArrayList<>();

    /**
     * <p>Variable to save the given Classes</p>
     */
    private ArrayList<Class> classType = new ArrayList<>();

    /**
     * <p>Variable to save the index information. The Object Array consists of two Variables:</br>
     * [0] = String-ID</br>
     * [1] = Numeric-ID (Used to read the content from the previous ArrayLists)</p>
     */
    private ArrayList<Object[]> indexList = new ArrayList<>();

    /**
     * <p>Adds an Item to the Cache with a randomly generated
     * Access-ID</p>
     * @param item - Object that should be cached
     * @param itemClass - Class of the item that is caches (eg String.class, Object.class)
     * @return String - The identifier to access the Data
     */
    public String add(Object item, Class itemClass) {
        String returnment = null;
        try {
            String indexString = UUID.randomUUID().toString();
            returnment = add(item,itemClass,indexString);
        } catch (DuplicateIndexException e) {

        }
        return returnment;
    }

    /**
     * <p>Adds an Item to the Cache with a specified Access-ID</p>
     * @param item - Object that should be cached
     * @param itemClass - Class of the item that is caches (eg String.class, Object.class)
     * @param indexString - The identifier to access the data
     * @return String - The identifier to Access the Data
     * @throws DuplicateIndexException - Will be thrown if the wanted identifier is alreade in use
     */
    public String add(Object item, Class itemClass, String indexString) throws DuplicateIndexException {
        checkTextIndex(indexString);
        int currentIndex = content.size();
        content.add(currentIndex,item);
        classType.add(currentIndex,itemClass);
        indexList.add(new Object[]{indexString,currentIndex});
        return indexString;
    }

    /**
     * <p>Reads a cached object by its ID. If you are often reading a specific object, please use the
     * method which is called with the numeric index</p>
     * @param id - The text ID (returned by the add-methods)
     * @return Object - The cached Object
     */
    public Object getItem(String id) {
        return getItem(getNumericIndex(id));
    }

    /**
     * <p>Reads a cached object by its numeric index. This method is faster than the String-Method</p>
     * @param index
     * @return Object - The cached Object
     */
    public Object getItem(int index) {
        if ( (index >= 0) && (index < content.size())) {
            return content.get(index);
        } else {
            return null;
        }
    }

    /**
     * <p>Checks if a text-ID is already taken</p>
     * @param index - The Text-ID index
     * @throws DuplicateIndexException
     */
    private void checkTextIndex(String index) throws DuplicateIndexException {
        for (int i=0;i<indexList.size();i++) {
            if ( (indexList.get(i)[0].toString().equals(index))) {
                throw new DuplicateIndexException();
            }
        }
    }

    /**
     * <p>Translates a String-ID to the numerical ID</p>
     * @param stringIndex - String-ID
     * @return Integer - Numerical ID
     */
    public int getNumericIndex(String stringIndex) {
        int targetIndex = -1;
        for (int i=0;i<indexList.size();i++) {
            if (indexList.get(i)[0].toString().equals(stringIndex)) {
                targetIndex = (int) indexList.get(i)[1];
            }
        }
        return targetIndex;
    }

    /**
     * <p>Deletes the Cache</p>
     */
    public void clear() {
        content.clear();
        classType.clear();
        indexList.clear();
    }

    /**
     * <p>Returns the number of elements saved in the Cache</p>
     * @return int - Element count
     */
    public int size() {
        return indexList.size();
    }

    /**
     * <p>Reads a cached object-class by its ID. If you are often reading a specific object, please use the
     * method which is called with the numeric index</p>
     * @param index - String ID of the Element
     * @return Class - Item class
     */
    public Class getItemClass(String index) {
        int numericIndex = getNumericIndex(index);
        return getItemClass(numericIndex);
    }

    /**
     * <p>Reads a cached object by its numeric index. This method is faster than the String-Method</p>
     * @param index - Numeric index of the Element
     * @return Class - Item class
     */
    public Class getItemClass(int index) {
        if ( (index >= 0) && (index < classType.size())) {
            return classType.get(index);
        } else {
            return null;
        }
    }
}
