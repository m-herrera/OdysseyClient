package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoPlayerController {

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private JFXSlider songSlider;

    @FXML
    private MediaView videoView;

    @FXML
    void next(ActionEvent event) {

    }

    @FXML
    void playPause(ActionEvent event) {
        MediaPlayer player = new MediaPlayer(new Media("/Users/Jai/Desktop/test.mp4"));
        videoView.setMediaPlayer(player);
        player.play();
    }

    @FXML
    void prev(ActionEvent event) {

    }

    @FXML
    void sliderChanged(MouseEvent event) {

    }

}

