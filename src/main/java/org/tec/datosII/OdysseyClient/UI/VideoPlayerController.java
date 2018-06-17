package org.tec.datosII.OdysseyClient.UI;

import com.Ostermiller.util.CircularByteBuffer;
import com.jfoenix.controls.JFXSlider;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Base64;

import javax.swing.*;

public class VideoPlayerController {
    Metadata video;

    private boolean paused = false;// true;
    private int pausedChunk;
    private int bufferSize = 31457280;
    private int totalChunks;
    private double amplitude = 0;
    private boolean isPlaying = false;

    ResizableJavaFXPlayerTest player;

    public DoubleProperty currentPercent = new SimpleDoubleProperty(0);

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private JFXSlider slider;

    @FXML
    private Pane videoView;

    @FXML
    void fullscreen(ActionEvent event) {

    }

    @FXML
    void playPause(ActionEvent event) {
        if(isPaused()){
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/play.png"));
            unpause();
        }else if(isPlaying()){
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/pause.png"));
            pause();
        }else{
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/pause.png"));
            play(0);
            isPlaying = true;
        }
    }

    @FXML
    void settings(ActionEvent event) {

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
     * Reproduce una video
     * @param chunk Bloque desde el cual reproducir la cancion
     */
    void play(int chunk) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "5");

        root.addElement("name").addText(video.name);
        root.addElement("artist").addText(video.artist);
        root.addElement("year").addText(video.year);
        root.addElement("album").addText(video.album);
        root.addElement("genre").addText(video.genre);

        currentPercent.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                slider.adjustValue(newValue.doubleValue());
            }
        });
        paused = false;

        Element chunkNumber = root.addElement("chunk").addText(String.valueOf(chunk));

        String request = document.asXML();

        System.out.println(request);

        NioClient client = NioClient.getInstance();
        ResponseHandler handler = client.send(request.getBytes());

        System.out.println(handler.getStrResponse());

        try {
            Document response = handler.getXmlResponse();

            if (response.getRootElement().elementIterator("error").next().getText().equals("false")) {
                String video = response.getRootElement().elementIterator("content").next().getText();

                CircularByteBuffer buffer = new CircularByteBuffer(bufferSize);

                buffer.getOutputStream().write(Base64.getDecoder().decode(video));

                InputStream stream = buffer.getInputStream();

                totalChunks = Integer.parseInt(response.getRootElement().elementIterator("chunks").next().getText());

                StreamThread streaming = new StreamThread(buffer.getOutputStream(), document, chunkNumber, chunk + 1, totalChunks);
                streaming.start();

                player = new ResizableJavaFXPlayerTest();
                try {
                    player.start(videoView, stream);
                } catch (Exception e) {
                    e.printStackTrace();
                    streaming.pause();
                }


            } else {
                System.out.println("Video no encontrada");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void pause(){
//        player.pause();
//        paused = true;
    }

    boolean isPlaying(){
        return isPlaying;
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

