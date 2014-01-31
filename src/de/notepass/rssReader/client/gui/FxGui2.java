package de.notepass.rssReader.client.gui;

import de.notepass.general.logger.Log;
import de.notepass.general.util.Util;
import de.notepass.rssReader.config.InternalConfig;
import de.notepass.rssReader.rssApi.RssConfiguration;
import de.notepass.rssReader.rssApi.objects.Rss;
import de.notepass.rssReader.rssApi.objects.RssContentAddedListener;
import de.notepass.rssReader.rssApi.objects.RssItem;
import de.notepass.rssReader.rssApi.objects.TimerRegister;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.net.URI;

/**
 * <p>This is an example-Gui to show you the basics of the API. An easier-to-understand cmd-line version is in make</p>
 */
public class FxGui2 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        //Initiate the Cache
        Log.logInfo("Initiating Cache");
        RssConfiguration.init();

        //Needed Variables
        final boolean[] reloadNeeded = {false};

        //Configure the main-layout
        final GridPane layout = new GridPane();
        final ColumnConstraints col_left = new ColumnConstraints();
        col_left.setPercentWidth(50);
        final ColumnConstraints col_right = new ColumnConstraints();
        col_right.setPercentWidth(50);
        layout.getColumnConstraints().addAll(col_left, col_right);
        layout.setHgap(InternalConfig.GUI_DEFAULT_HGAP);
        layout.setVgap(InternalConfig.GUI_DEFAULT_VGAP);
        layout.setPadding(InternalConfig.GROUPBOX_DEFAULT_PADDING);
        final Scene scene = new Scene(layout,500,500);
        stage.setScene(scene);
        final ObservableList<Rss> lv_rssFeedsItems = FXCollections.observableArrayList();
        final ListView<Rss> lv_rssFeeds = new ListView<>(lv_rssFeedsItems);
        lv_rssFeeds.setPrefWidth(10000);
        lv_rssFeeds.setPrefHeight(10000);
        final ObservableList<RssItem> lv_rssItemListItems = FXCollections.observableArrayList();
        final ListView<RssItem> lv_rssItemList = new ListView<>(lv_rssItemListItems);
        lv_rssItemList.setPrefWidth(10000);
        lv_rssItemList.setPrefHeight(10000);
        final WebView wv_rssItemVisualiser = new WebView();
        final WebEngine we_rssItemVisualiserController = wv_rssItemVisualiser.getEngine();
        layout.add(lv_rssFeeds, 0, 1);
        layout.add(lv_rssItemList,0,2);
        layout.add(wv_rssItemVisualiser,1,1,1,2);
        final HBox hb_buttons = new HBox();
        hb_buttons.setSpacing(InternalConfig.GUI_DEFAULT_SPACING);
        Button bt_addRssFeed = new Button("+");
        Button bt_remRssFeed = new Button("-");
        final Button bt_reload = new Button("R");
        final CheckBox cb_notify = new CheckBox("Game mode");
        cb_notify.setTooltip(new Tooltip("Doesn't show messages when a Rss-Feed is updated"));
        final CheckBox cb_reloadRss = new CheckBox("Travel mode");
        cb_reloadRss.setTooltip(new Tooltip("Doesn't download any Rss-Updates"));
        hb_buttons.getChildren().addAll(bt_addRssFeed, bt_remRssFeed, bt_reload, cb_notify, cb_reloadRss);
        hb_buttons.setAlignment(Pos.CENTER);
        layout.add(hb_buttons, 0, 0, 2, 1);

        //Get all Rss-Feeds from the Config
        Rss[] registeredRssFeeds = RssConfiguration.getRssObjects();
        //Add them to the ListView
        lv_rssFeedsItems.addAll(registeredRssFeeds);

        //If an Rss-Feed is selected in the List-View, load it's corresponding Items
        lv_rssFeeds.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rss>() {
            @Override
            public void changed(ObservableValue<? extends Rss> observableValue, Rss oldSelection, Rss newSelection) {
                if (newSelection != null) {
                    try {
                        lv_rssItemListItems.clear();
                        lv_rssItemListItems.addAll(newSelection.getRssItems());
                    } catch (Exception e) {
                        //If an error occurs, log it to a file (The Document-Factory can bug sometimes, so a error here hasn't to be a real issue)
                        Log.logError(e);
                    }
                }
            }
        });

        //If an Rss-Item is selected, show it in the WebView
        lv_rssItemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RssItem>() {
            @Override
            public void changed(ObservableValue<? extends RssItem> observableValue, RssItem oldSelection, RssItem newSelection) {
                if (newSelection != null) {
                    //HTML-Temlate for the browser
                    String template = "<html>\n" +
                            "\t<head>\n" +
                            "\t\n" +
                            "\t</head>\n" +
                            "\t<body style=\"margin: 0px; padding: 0px; background-color: #000000; /*background-image: url("+this.getClass().getResource("bg.png")+");*/ background-size: 100%;\">\n" +
                            "\t\t\t<div style=\"text-align: center; margin-top: 2.5%; margin-bottom: 4.5%; padding: 0.5%; background-color: #222; border-top: 1px solid #FFFFFF; border-bottom: 1px solid #FFFFFF; color: #FFFFFF;\">\n" +
                            "\t\t\t\t<h1>"+newSelection.getTitle()+"</h1>\n" +
                            "\t\t\t</div>\n" +
                            "\t\t\t<div style=\"padding: 2%; padding-top: 0.5%; padding-bottom:0.5%; background-color: #222; border-top: 1px solid #FFFFFF; border-bottom: 1px solid #FFFFFF; color: #FFFFFF;\">\n" +
                            "\t\t\t\t\t"+newSelection.getDescription()+"\n" +
                            "\t\t\t<hr><a style='color: #EEEEEE' href='"+newSelection.getLink()+"'>"+newSelection.getLink()+"</a></div>\n" +
                            "\t</body>\n" +
                            "</html>";
                    //Show the entry
                    we_rssItemVisualiserController.loadContent(template);
                }
            }
        });

        //Add a Rss-Feed
        bt_addRssFeed.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final Stage addStage = new Stage();
                addStage.initStyle(StageStyle.UTILITY);
                GridPane addLayout = new GridPane();
                addLayout.setHgap(InternalConfig.GUI_DEFAULT_HGAP);
                addLayout.setVgap(InternalConfig.GUI_DEFAULT_VGAP);
                addLayout.setPadding(new Insets(2));
                final TextField tf_rssUri = new TextField();
                tf_rssUri.setPromptText("Rss-Feed-Url");
                Button bt_checkAndAddFeed = new Button("+");
                bt_checkAndAddFeed.setMinWidth(30);
                bt_checkAndAddFeed.setMaxWidth(30);
                tf_rssUri.setPrefWidth(10000);
                addLayout.add(tf_rssUri, 0, 0);
                addLayout.add(bt_checkAndAddFeed,1,0);
                Scene addScene = new Scene(addLayout,300,36);
                addStage.setScene(addScene);
                addStage.show();
                bt_checkAndAddFeed.requestFocus();
                addStage.setMinHeight(addStage.getHeight());
                //addStage.setMaxHeight(addStage.getHeight());
                addStage.setMinWidth(addStage.getWidth());

                //If the Add-Button of the popup is clicked, check and add the Rss-Feed
                bt_checkAndAddFeed.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        try {
                            //Check if the Rss-File is a valid Rss-File
                            boolean isValid;
                            URI RssUrl = new URI(tf_rssUri.getText());
                            File target = new File(InternalConfig.RSS_TEMP_ROOT.getAbsolutePath()+"/check.rss");
                            Util.download(RssUrl,target);
                            isValid = RssConfiguration.isValidRss(target);
                            //Deletes the temporary file
                            if (!target.delete()) {
                                Log.logWarning("Couldn't delete temporary file "+target.getAbsolutePath());
                            }
                            bt_reload.fire();

                            if (isValid) {
                                //Add the Rss-File to the Configuration
                                RssConfiguration.addRssFeed(RssUrl);
                                addStage.close();
                            } else {
                                Util.showPureError("Invalid Rss-Feed!");
                            }

                        } catch(Exception e) {
                            Util.showPureError("There was an Internal Error:" + Util.getLineSeparator() + Util.exceptionToString(e));
                            Log.logError("There was an Internal Error while adding an Rss-Feed:" + Util.getLineSeparator() + Util.exceptionToString(e));
                        }
                    }
                });
            }
        });

        //If the remove-button is clicked, remove the selected feed
        bt_remRssFeed.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<Rss> selectedItems = lv_rssFeeds.getSelectionModel().getSelectedItems();
                if (selectedItems.size() > 0) {
                    for (Rss scopeRss:selectedItems) {
                        try {
                            //Remove the Rss-Feed from the list
                            RssConfiguration.remove(scopeRss.getUuid());
                            bt_reload.fire();
                        } catch (Exception e) {
                            Util.showPureError("There was an Internal Error:" + Util.getLineSeparator() + Util.exceptionToString(e));
                            Log.logError("There was an Internal Error while removing an Rss-Feed:" + Util.getLineSeparator() + Util.exceptionToString(e));
                        }
                    }
                }
            }
        });

        //Reload the list
        bt_reload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Delete all Items from the Lists
                lv_rssFeedsItems.clear();
                lv_rssItemListItems.clear();

                //Reload the Config File (To get external Changes as well, for internal changes there is no need to reload it)
                try {
                    RssConfiguration.refreshCache();
                } catch (Exception e) {
                    Util.showPureError("Error while reading the Config File:"+Util.getLineSeparator()+Util.exceptionToString(e));
                    Log.logError("Error while reading the Config File:" + Util.getLineSeparator() + Util.exceptionToString(e));
                }

                //Read the Rss-Feeds
                try {
                    lv_rssFeedsItems.addAll(RssConfiguration.getRssObjects());
                } catch (Exception e) {
                    Util.showError("There was an error while trying to read the Rss-Feeds:"+Util.getLineSeparator()+Util.exceptionToString(e));
                    Log.logError("There was an error while trying to read the Rss-Feeds:" + Util.getLineSeparator() + Util.exceptionToString(e));
                }
            }
        });

        //Add a change-listener to the Rss-Feeds (It is triggered, when new Content is added to the RSS-Feed)
        for (Rss scopeRss:lv_rssFeedsItems) {
            scopeRss.addChangeListener(new RssContentAddedListener() {
                @Override
                public void RssChanged(Rss changedRss) {
                    if (!stage.isFocused()) {
                        //If we need to reload the list, we need it to do like this, because JavaFx doesn't lie timer threads
                        reloadNeeded[0] = true;

                        //Notify the user, that there is new Stuff
                        Util.showPureInfo("You got new messages in your " + changedRss.getTitle() + " Feed!", "New stuff!");
                    }
                }
            });
        }
          
        //This loop checks every few seconds if a reload is needed (We could also watch for a change on the "reloadArrayList" or "reloadNeeded" Variable, but this would get too complex [Not sayin that this is easier...])
        final Timeline reloadChecker = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               if (reloadNeeded[0]) {
                   reloadNeeded[0] = false;
                   bt_reload.fire();
               }
            }
        }));
        reloadChecker.setCycleCount(Timeline.INDEFINITE);
        reloadChecker.play();

        //Disable/Enable the Event-System
        cb_notify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Rss scopeRss:lv_rssFeedsItems) {
                    scopeRss.setFireChangeEvent(!cb_notify.isSelected());
                }
            }
        });


        //Disable/Enable the Download
        cb_reloadRss.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Rss scopeRss:lv_rssFeedsItems) {
                    scopeRss.setAutoUpdate(!cb_reloadRss.isSelected());
                }
            }
        });

        stage.setTitle("RSS-Reader 2");

        //Toggle fullscreen mode with F11
        layout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().toString().toLowerCase().equals("f11")) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            }
        });



        //Show the Window
        stage.show();

        //Clean exit
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                TimerRegister.stopAllTimer();
                reloadChecker.stop();
            }
        });
    }
}
