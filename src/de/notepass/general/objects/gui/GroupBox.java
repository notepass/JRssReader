package de.notepass.general.objects.gui;

import de.notepass.general.util.Util;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

//This is a standard title GroupBox

/**
 * <p>This class contains a standard titled GroupBox</p>
 */

public class GroupBox extends StackPane {

    /**
     * Title position left trigger
     */
    public static final TitlePos TITLE_POS_LEFT = TitlePos.POS_LEFT;
    /**
     * Title position center trigger
     */
    public static final TitlePos TITLE_POS_CENTER = TitlePos.POS_CENTER;
    /**
     * Title position right trigger
     */
    public static final TitlePos TITLE_POS_RIGHT = TitlePos.POS_RIGTH;

    //The titePane will later just Contain the title and a basic markup
    /**
     * <p>The StackPane that contains the title and the content pane</p>
     */
    public StackPane titlePane = new StackPane();
    //The content Pane will contain all elements

    //Both of them are modifiable from the Outside... For Dynamical reasons
    /**
     * <p>The content pane is a GridPane that contains the content elements</p>
     */
    public GridPane contentPane = new GridPane();
    /**
     * <p>The label for the titel</p>
     */
    private Label title = new Label();
    /**
     * <p>Variable containing the needed CSS-Files</p>
     */
    public static String cssFile = Util.createLoadString("style/GroupBox.css");

    /**
     * <p>Setting up the GroupBox with its title and the title position</p>
     * @param titleString - Title of the GroupBox
     * @param tp - Position of the title ({@link de.notepass.general.objects.gui.GroupBox#TITLE_POS_LEFT}, {@link de.notepass.general.objects.gui.GroupBox#TITLE_POS_CENTER}, {@link de.notepass.general.objects.gui.GroupBox#TITLE_POS_RIGHT})
     */
    public GroupBox (String titleString, TitlePos tp) {

        title.setText(" "+titleString+" ");
        title.getStyleClass().add("GroupBoxTitle");

        StackPane.setAlignment(title, tp.getMainPos());
        title.setStyle(tp.getPosString());

        titlePane.getStyleClass().add("GroupBoxContent");
        titlePane.getChildren().add(contentPane);

        getStyleClass().add("GroupBoxBorder");
        getChildren().addAll(title, titlePane);
    }

    /**
     * <p>Faster version to access the add() method of the {@link #contentPane}</p>
     * @param content - The content that should be added
     * @param column
     * @param row
     */
    public void add(Node content, int column, int row) {
        this.contentPane.add(content, column, row);
    }

    /**
     *
     * @param content - The content that should be added
     * @param column
     * @param row
     * @param columnSpan
     * @param rowSpan
     */
    public void add(Node content, int column, int row, int columnSpan, int rowSpan) {
        this.contentPane.add(content,column,row,columnSpan,rowSpan);
    }
}
