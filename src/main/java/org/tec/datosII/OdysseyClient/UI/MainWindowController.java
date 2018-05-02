package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class MainWindowController {

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private JFXSlider songSlider;

    @FXML
    private JFXListView<?> friendsList;

    @FXML
    private JFXTreeTableView<?> songList;

    @FXML
    void nextSong(ActionEvent event) {

    }

    @FXML
    void playPauseSong(ActionEvent event) {

    }

    @FXML
    void prevSong(ActionEvent event) {

    }

}

