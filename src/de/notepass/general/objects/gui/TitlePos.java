package de.notepass.general.objects.gui;

import javafx.geometry.Pos;

/**
 * <p>The title-pos triggers. Will be later replaced.</p>
 */
@Deprecated
public class TitlePos {
    private Pos mainPos;
    private String posString;
    public static TitlePos POS_LEFT = new TitlePos().makePosLeft();
    public static TitlePos POS_CENTER = new TitlePos().makePosCenter();
    public static TitlePos POS_RIGTH = new TitlePos().makePosRight();

    private TitlePos makePosLeft() {
        this.mainPos = Pos.TOP_LEFT;
        this.posString = "-fx-translate-x: 10px;";
        return this;
    }

    private TitlePos makePosCenter() {
        this.mainPos = Pos.TOP_CENTER;
        this.posString = "";
        return this;
    }

    private TitlePos makePosRight() {
        this.mainPos = Pos.TOP_RIGHT;
        this.posString = "-fx-translate-x: -10px;";
        return this;
    }

    public Pos getMainPos() {
        return mainPos;
    }

    public String getPosString() {
        return posString;
    }
}
