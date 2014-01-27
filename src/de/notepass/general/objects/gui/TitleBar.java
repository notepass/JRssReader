package de.notepass.general.objects.gui;

import de.notepass.general.internalConfig.InternalConfigDummy;
import de.notepass.general.util.Util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Deprecated
public class TitleBar extends StackPane {
    private double mouseDragOffsetX;
    private double mouseDragOffsetY;
    private double oldWidth=0;
    private double oldHeight=0;
    private double oldX=0;
    private double oldY=0;
    private double lastMouseClick=0;
    private Button titleLabel=new Button("Titel");
    private Stage parentStage;
    private Button bt_maximize;

    public static String cssFile = Util.createLoadString("style/TitleBar.css");
    public TitleBar(final Stage parentStage, final boolean has_bt_minimize, final boolean has_bt_maximize,final boolean has_bt_close) {
        this.parentStage = parentStage;
        //Elements
        final HBox buttons = new HBox();
        final HBox title = new HBox();
        final Button bt_close = new Button("X");
        bt_maximize = new Button("F");
        final Button bt_minimize = new Button("-");
        parentStage.initStyle(StageStyle.UNDECORATED);
        bt_close.getStyleClass().addAll("TitleBarButton");
        bt_minimize.getStyleClass().addAll("TitleBarButton");
        bt_maximize.getStyleClass().addAll("TitleBarButton");
        titleLabel.getStyleClass().addAll("TitleBarButton");
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.getChildren().addAll(bt_minimize, bt_maximize, bt_close);
        buttons.getStyleClass().addAll("TitleBarElementPane");
        title.setAlignment(Pos.BASELINE_LEFT);
        title.getChildren().addAll(titleLabel);
        title.getStyleClass().addAll("TitleBarElementPane");
        getChildren().addAll(title, buttons);
        getStyleClass().addAll("TitleBar");



        //Action Listener
        //Dragging
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseDragOffsetX = event.getSceneX();
                mouseDragOffsetY = event.getSceneY();
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                parentStage.setX(event.getScreenX() - mouseDragOffsetX);
                parentStage.setY(event.getScreenY() - mouseDragOffsetY);
            }
        });

        //Closing
        bt_close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                parentStage.close();
            }
        });

        //Minimizing
        bt_minimize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                parentStage.setIconified(!parentStage.isIconified());
            }
        });

        //Maximize
        bt_maximize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isMaximised()) {
                    parentStage.setWidth(oldWidth);
                    parentStage.setHeight(oldHeight);
                    parentStage.setX(oldX);
                    parentStage.setY(oldY);
                    bt_maximize.setText("F");
                } else {
                    oldWidth = parentStage.getWidth();
                    oldHeight = parentStage.getHeight();
                    oldX = parentStage.getX();
                    oldY = parentStage.getY();
                    parentStage.setHeight(Util.getScreenSize().height);
                    parentStage.setWidth(Util.getScreenSize().width);
                    parentStage.setX(0);
                    parentStage.setY(0);
                    bt_maximize.setText("S");
                }
            }
        });

        //Maxi/Minimize on Double Mouse Click
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                doubleCLickAction();
            }
        });

    }

    //Tells if the Window is maximized
    private boolean isMaximised() {
        if ( (Util.getScreenSize().width == this.parentStage.getWidth()) && (Util.getScreenSize().height == this.parentStage.getHeight())) {
            return true;
        } else {
            return false;
        }
    }

    public void doubleCLickAction() {
        double time=System.currentTimeMillis();
        if (lastMouseClick==0) {
            lastMouseClick=System.currentTimeMillis();
        } else {
            if ( (time - lastMouseClick) <= InternalConfigDummy.DOUBLIC_CLICK_TIMEOUT) {
                bt_maximize.fire();

                //So dat you don't un/maximise it when you click again in < 1sek
                lastMouseClick = 0;
            } else {
                lastMouseClick = time;
            }
        }
    }
}
