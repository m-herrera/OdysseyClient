package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXSlider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.*;
import java.io.*;
import java.util.Base64;

public class VideoPlayerController {
    Metadata video;

    private boolean paused = false;
    private int pausedChunk;
    private long chunkSize;
    private int totalChunks;
    private boolean isPlaying = false;
    private File buffer;
    private Stage stage;
    private StreamThread streaming;

    MediaPlayer player;

    public DoubleProperty currentPercent = new SimpleDoubleProperty(0);

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private JFXSlider slider;

    @FXML
    private MediaView videoView;

    @FXML
    void fullscreen(ActionEvent event) {
        if(stage.isFullScreen()){
            stage.setFullScreen(false);
        }else {
            stage.setFullScreen(true);
        }
    }

    @FXML
    void playPause(ActionEvent event) {
        if(isPaused()){
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/pause.png"));
            unpause();
        }else if(isPlaying()){
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/play.png"));
            pause();
        }else{
            playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/pause.png"));
            play();
            isPlaying = true;
        }
    }


    @FXML
    void sliderChanged(MouseEvent event) {
        forward((int) slider.getValue());
        playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/pause.png"));
    }


    void load(Metadata video){
        this.video = video;
        new Thread(()->{buffer(0);}).start();
    }

    void stop(){
        if(isPlaying()) {
            player.stop();
        }
        streaming.pause();
    }

    /**
     * Descarga el video
     * @param chunk Bloque desde el cual reproducir la cancion
     */
    void buffer(int chunk) {
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

        NioClient client = NioClient.getInstance();
        ResponseHandler handler = client.send(request.getBytes());

        try {
            Document response = handler.getXmlResponse();

            if (response.getRootElement().elementIterator("error").next().getText().equals("false")) {
                String video = response.getRootElement().elementIterator("content").next().getText();

                buffer = File.createTempFile("odyssey", "buffer");

                OutputStream outputStream = new FileOutputStream(buffer);

                outputStream.write(Base64.getDecoder().decode(video));
                chunkSize = buffer.length();

                totalChunks = Integer.parseInt(response.getRootElement().elementIterator("chunks").next().getText());

                streaming = new StreamThread(outputStream, document, chunkNumber, chunk + 1, totalChunks);
                streaming.start();

            } else {
                System.out.println("Video no encontrada");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void play(){
        while(buffer == null || buffer.length() < 0.1 * chunkSize * totalChunks){
            try {
                if(buffer != null) {
                    System.out.println("Buffering: Only have " + buffer.length() + " bytes loaded");
                }
                Thread.sleep(1000);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        System.out.println("Now playing " + buffer.length() + " bytes");

        player = new MediaPlayer(new Media(buffer.toURI().toString()));
        videoView.setMediaPlayer(player);
        player.play();
        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                slider.setValue(newValue.toSeconds() / player.getTotalDuration().toSeconds() * 100);
            }
        });

    }

    void pause(){
        player.pause();
        paused = true;
    }

    boolean isPlaying(){
        return isPlaying;
    }

    boolean isPaused(){
        return paused;
    }

    void forward(double slider){
        Duration duration = player.getTotalDuration();
        Duration time = duration.multiply(slider / 100);
        System.out.println(time.toSeconds());
        player.seek(time);
    }

    void unpause(){
        paused = false;
        player.play();
    }

    void setStage(Stage stage){
        this.stage = stage;
    }

}

