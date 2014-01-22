package de.notepass.general.objects.gui;

import de.notepass.general.util.Util;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kim.hayo
 * Date: 26.11.13
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class StatusBarItem extends GridPane {
    private ArrayList<Node> content = new ArrayList<Node>();
    private double width = 0;
    public static String cssFile = Util.createLoadString("style/StatusBar.css");

    public StatusBarItem(double width) {
        setStyle("-fx-background-color: rgba(0,0,0,0);");
        setMaxWidth(width);
        setMinWidth(width);
        setMinHeight(200);
        getStyleClass().add("StatusBarItem");
    }

    public void add(Control c, int index) {
        add(c, index, 0);
    }

}
