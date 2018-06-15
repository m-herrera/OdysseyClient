package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXSlider;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeItem;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.*;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.swing.*;

public class VideoPlayerController {
    Metadata video;

    VideoThread thread;

    boolean paused;

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private JFXSlider slider;

    @FXML
    private Pane videoView;

    @FXML
    void next(ActionEvent event) {

    }

    @FXML
    void playPause(ActionEvent event) {
        if(isPlaying()){
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/play.png"));
        }else if(isPaused()){
            unpause();
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/pause.png"));
        }else{
//            play(0);

            /*
                En lugar de ejecutar esto desde aqui deberia hacerse desde el metodo play
             */
            ResizableJavaFXPlayerTest player = new ResizableJavaFXPlayerTest();
            try {
                player.start(videoView, new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 0;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void prev(ActionEvent event) {

    }

    @FXML
    void sliderChanged(MouseEvent event) {

    }

    void load(Metadata video){
        this.video = video;
    }

    void stop(){

    }

    /**
     * Reproduce una cancion
     * @param chunk Bloque desde el cual reproducir la cancion
     */
    void play(int chunk){
        if(thread != null && thread.isAlive()){
            pause();
        }
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "5");

        root.addElement("name").addText(video.name);
        root.addElement("artist").addText(video.artist);
        root.addElement("year").addText(video.year);
        root.addElement("album").addText(video.album);
        root.addElement("genre").addText(video.genre);

        thread = new VideoThread(document, chunk);
        thread.setVideoView(videoView);
        thread.currentPercent.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                slider.adjustValue(newValue.doubleValue());
            }
        });
        thread.start();
    }

    void pause(){
        paused = true;
    }

    boolean isPlaying(){
        if(thread != null){
            return thread.isAlive();
        }else{
            return false;
        }
    }

    boolean isPaused(){
        return paused;
    }

    void forward(int slider){
//        int chunk = thread.getTotalChunks() * slider / 100;
//        play(currentSong, chunk);
    }

    void unpause(){
    }


}

