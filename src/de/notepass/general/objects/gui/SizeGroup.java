package de.notepass.general.objects.gui;

import javafx.scene.control.Control;

import java.util.ArrayList;

//Resizes all the Elements in the Group to the largest element
//You must show the Window before using setElementDimension!

/**
 * <p>Please use ColumnConstrains instead</p>
 */
@Deprecated
public class SizeGroup {
    private boolean setHeight;
    private boolean setWidth;

    private ArrayList<Control> elements = new ArrayList<Control>();

    private double maxHeight;
    private double maxWidth;

    //Constructor
    public SizeGroup(boolean controlHeight, boolean controlWidth) {
        //Which Dimensions should be resized?
        setHeight = controlHeight;
        setWidth = controlWidth;

        //Setting some standard values
        maxHeight = 0;
        maxHeight = 0;
    }

    //Add a single element
    public void add(Control c) {
        elements.add(c);
    }

    //Add multible Elements
    public void addAll(Control ... c) {
        for (int i = 0; i<c.length;i++) {
            elements.add(c[i]);
        }
    }

    //Reset the List of Elements and Add a list of elements
    public void setAll(Control ... c) {
        elements = new ArrayList<Control>();
        for (int i=0;i<c.length;i++) {
            elements.add(c[i]);
        }
    }

    //Reset the List of Elements and add a single element
    public void set(Control c) {
        elements = new ArrayList<Control>();
        elements.add(c);
    }

    //Calculate the max values
    private void calcWidth() {
        double cache=0;
        for (int i=0; i<elements.size(); i++) {
            if (elements.get(i).getWidth()>cache) {
                cache=elements.get(i).getWidth();
            }
        }
        maxWidth=cache;
    }

    private void calcHeight() {
        double cache=0;
        for (int i=0; i<elements.size(); i++) {
            if (elements.get(i).getHeight()>cache) {
                cache=elements.get(i).getHeight();
            }
        }
        maxWidth=cache;
    }


    //Call for the Calculator (This will unset the manual Size Entry)
    public void calcDimensions() {
        if (setHeight) {
            calcHeight();
        }

        if (setWidth) {
            calcWidth();
        }
    }

    //Set the width/height manually
    public void setHeight(double height) {
        maxHeight = height;
    }

    public void setWidth(double width) {
        maxWidth = width;
    }

    //Set the Width/Height of the elements
    public void resizeElements() throws resizedSizeNullException{

        //Pre-Check
        if (setWidth && (maxWidth<=0)) {
            calcDimensions();
            if (maxWidth <= 0) {
                throw new resizedSizeNullException();
            }
        }

        if (setHeight && (maxHeight<=0)) {
            calcDimensions();
            if (maxHeight<=0) {
                throw new resizedSizeNullException();
            }
        }

        if (setHeight) {
            for (int i = 0;i<elements.size();i++) {
                elements.get(i).setMinHeight(maxHeight);
                elements.get(i).setMaxHeight(maxHeight);
            }
        }

        if (setWidth) {
            for (int i=0;i<elements.size();i++) {
                elements.get(i).setMinWidth(maxWidth);
                elements.get(i).setMaxWidth(maxWidth);
            }
        }
    }

    public double getHeight() {
        return maxHeight;
    }

    public double getWidth() {
        return maxWidth;
    }

}
