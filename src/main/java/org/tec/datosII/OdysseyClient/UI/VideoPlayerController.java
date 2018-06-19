package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXSlider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

public class VideoPlayerController {
    private final String PAUSE_BTN = "org/tec/datosII/OdysseyClient/UI/icons/pause.png";
    private final String PLAY_BTN = "org/tec/datosII/OdysseyClient/UI/icons/play.png";
    public DoubleProperty currentPercent = new SimpleDoubleProperty(0);
    Metadata video;
    MediaPlayer player;
    int bands;
    Rectangle[] rects;
    private boolean paused = false;
    private long chunkSize;
    private int totalChunks;
    private boolean isPlaying = false;
    private File buffer;
    private Stage stage;
    private StreamThread streaming;
    @FXML
    private ImageView playPauseBtn;
    @FXML
    private JFXSlider slider;
    @FXML
    private MediaView videoView;
    @FXML
    private ImageView loadGIF;
    @FXML
    private StackPane stackPane;
    @FXML
    private HBox boxes;
    
    @FXML
    void fullscreen (ActionEvent event) {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        } else {
            stage.setFullScreen(true);
        }
    }
    
    @FXML
    void playPause (ActionEvent event) {
        if (isPaused()) {
            playPauseBtn.setImage(new Image(PAUSE_BTN));
            unpause();
        } else if (isPlaying()) {
            playPauseBtn.setImage(new Image(PLAY_BTN));
            pause();
        } else {
            playPauseBtn.setImage(new Image(PAUSE_BTN));
            play();
            isPlaying = true;
        }
    }
    
    
    @FXML
    void sliderChanged (MouseEvent event) {
        forward((int) slider.getValue());
        playPauseBtn.setImage(new Image("org/tec/datosII/OdysseyClient/UI/icons/pause.png"));
    }
    
    
    void load (Metadata video) {
        this.video = video;
        new Thread(() -> {
            buffer(0);
        }).start();
    }
    
    void stop () {
        if (isPlaying()) {
            player.stop();
        }
        streaming.pause();
    }
    
    /**
     * Descarga el video
     *
     * @param chunk Bloque desde el cual reproducir la cancion
     */
    void buffer (int chunk) {
        
        String gif2 = "org/tec/datosII/OdysseyClient/UI/gif2.gif";
        loadGIF.setImage(new Image(gif2));
        
        
        System.out.println("Buffering");
        
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "5");
        
        root.addElement("name").addText(video.name);
        root.addElement("artist").addText(video.artist);
        root.addElement("year").addText(video.year);
        root.addElement("album").addText(video.album);
        root.addElement("genre").addText(video.genre);
        
        currentPercent.addListener((observable, oldValue, newValue) -> slider.adjustValue(newValue.doubleValue()));
        paused = false;
        
        Element chunkNumber = root.addElement("chunk").addText(String.valueOf(chunk));
        
        String request = document.asXML();
        
        NioClient client = NioClient.getInstance();
        ResponseHandler handler = client.send(request.getBytes());
        
        try {
            Document response = handler.getXmlResponse();
            System.out.println("Got first chunk");
            
            if (response.getRootElement().elementIterator("error").next().getText().equals("false")) {
                String binaryVideo = response.getRootElement().elementIterator("content").next().getText();
                
                buffer = File.createTempFile("odyssey", "buffer");
                
                OutputStream outputStream = new FileOutputStream(buffer);
                
                outputStream.write(Base64.getDecoder().decode(binaryVideo));
                chunkSize = buffer.length();
                
                totalChunks = Integer.parseInt(response.getRootElement().elementIterator("chunks").next().getText());
                
                streaming = new StreamThread(outputStream, document, chunkNumber, chunk + 1, totalChunks);
                streaming.start();
                
            } else {
                System.out.println("Video no encontrada");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    void play () {
        
        stackPane.getChildren().remove(loadGIF);
//
        while (buffer == null || buffer.length() < 0.1 * chunkSize * totalChunks) {
            try {
                if (buffer != null) {
                    System.out.println("Buffering: Only have " + buffer.length() + " bytes loaded");
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Now playing " + buffer.length() + " bytes");
        
        player = new MediaPlayer(new Media(buffer.toURI().toString()));
        videoView.setMediaPlayer(player);
        
        bands = player.getAudioSpectrumNumBands();
        rects = new Rectangle[bands];
        
        sliderBoxes();
        
        player.play();
        
        player.setOnReady(() -> {
            
            double bandWidth = videoView.getBoundsInParent().getWidth() / rects.length;
            for (Rectangle r : rects) {
                r.setWidth(bandWidth);
                r.setHeight(2);
            }
        });
        
        player.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            for (int i = 0; i < rects.length; i++) {
                double h = magnitudes[i] + 60;
                if (h > 2) {
                    rects[i].setHeight(h);
                }
            }
        });
        
        player.currentTimeProperty().addListener((observable, oldValue, newValue) -> slider.setValue(newValue.toSeconds() / player.getTotalDuration().toSeconds() * 100));
        
    }
    
    
    void pause () {
        player.pause();
        paused = true;
    }
    
    boolean isPlaying () {
        return isPlaying;
    }
    
    boolean isPaused () {
        return paused;
    }
    
    void forward (double slider) {
        while (buffer.length() < (slider / 100) * totalChunks * chunkSize) {
            System.out.println("Buffering");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Duration duration = player.getTotalDuration();
        Duration time = duration.multiply(slider / 100);
        player.seek(time);
    }
    
    void unpause () {
        paused = false;
        player.play();
    }
    
    void setStage (Stage stage) {
        this.stage = stage;
    }
    
    
    void sliderBoxes () {
        
        for (int i = 0; i < rects.length; i++) {
            rects[i] = new Rectangle();
            rects[i].setFill(Color.rgb(245, 91, 59));
            boxes.getChildren().add(rects[i]);
        }
        
        
    }
    
    
}



