package de.notepass.general.objects.gui;

import de.notepass.general.internalConfig.InternalConfigDummy;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <p>Not-working try to build a dialog in JavaFX</p>
 */
@Deprecated
public class YesNoPrompt extends Application {
    //Titel of the Prompt
    public String title = "Titel";
    public boolean hasYesButton=true;
    public boolean hasNoButton=true;
    public boolean hasCancelButton=true;
    public String messageText = "Unkown";
    public Button yesButton=new Button("Yes");
    public Button noButton=new Button("No");
    public Button cancelButton=new Button("Cancel");
    public Stage initOwnerStage;
    public void start(final Stage primaryStage) {
        //Setting up Elements, Style and Dependency's
        primaryStage.initOwner(initOwnerStage);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);
        final BorderPane layoutPane = new BorderPane();
        final HBox promptText = new HBox();
        promptText.setAlignment(Pos.TOP_CENTER);
        promptText.setPadding(InternalConfigDummy.GUI_DEFAULT_PADDING);
        final HBox promptButtons = new HBox();
        promptButtons.setSpacing(InternalConfigDummy.GUI_DEFAULT_SPACING);
        promptButtons.setAlignment(Pos.BASELINE_RIGHT);
        promptButtons.setStyle("-fx-background-color: -fx-box-border,-fx-background;");
        promptButtons.setPadding(InternalConfigDummy.GUI_DEFAULT_PADDING);
        //Setting up Text and Buttons
        layoutPane.setCenter(promptText);
        layoutPane.setBottom(promptButtons);
        Label testlabel = new Label(messageText);
        promptText.getChildren().addAll(testlabel);
        //Choosing the Buttons to add
        if (hasYesButton) {
            promptButtons.getChildren().add(yesButton);
            yesButton.requestFocus();
        }
        if (hasNoButton) {
            promptButtons.getChildren().add(noButton);
            noButton.requestFocus();
        }
        if (hasCancelButton) {
            promptButtons.getChildren().add(cancelButton);
            cancelButton.requestFocus();
        }
        //Setting up the main Window
        Scene mainScene = new Scene(layoutPane,500,150);
        primaryStage.setScene(mainScene);
        //Show the Window
        primaryStage.centerOnScreen();
        primaryStage.showAndWait();
        primaryStage.toFront();

        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("a");
            }
        });
    }

    public static void main(String [] args) {
        launch(args);
    }
}
