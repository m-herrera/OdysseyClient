package org.tec.datosII.OdysseyClient;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Reproductor de audio en streaming
 */
public class MusicPlayer{
    /**
     * Instancia unica del reproductor
     */
    private static MusicPlayer instance;

    int currentChunk;

    Metadata currentSong;

    Slider slider;

    private Document request;
    private int initialChunk;
    private boolean paused = true;
    private int pausedChunk;
    private int bufferSize = 983040;
    private int totalChunks;
    private double amplitude = 0;
    private boolean isPlaying = false;
    private MediaPlayer player;

    /**
     * Constructor privado del reproductor
     */
    private MusicPlayer(){}

    /**
     * Singleton para obtener la instancia del reproductor
     * @return la instancia del reproductor
     */
    public static MusicPlayer getInstance(){
        if(instance == null){
            instance = new MusicPlayer();
        }
        return instance;
    }

    /**
     * Reproduce una cancion
     * @param song Metadata de la cancion a reproducir
     * @param chunk Bloque desde el cual reproducir la cancion
     */
    public void play(Metadata song, int chunk){
        currentSong = song;

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "5");

        root.addElement("name").addText(song.name);
        root.addElement("artist").addText(song.artist);
        root.addElement("year").addText(song.year);
        root.addElement("album").addText(song.album);
        root.addElement("genre").addText(song.genre);

        paused = false;

        Element chunkNumber = root.addElement("chunk").addText(String.valueOf(initialChunk));

        String request = document.asXML();

        NioClient client = NioClient.getInstance();
        ResponseHandler handler = client.send(request.getBytes());

        try {
            Document response = handler.getXmlResponse();

            if(response.getRootElement().elementIterator("error").next().getText().equals("false")) {
                String audio = response.getRootElement().elementIterator("content").next().getText();

                File buffer = File.createTempFile("odyssey", "buffer");
                OutputStream stream = new FileOutputStream(buffer);

                stream.write(Base64.getDecoder().decode(audio));

                totalChunks = Integer.parseInt(response.getRootElement().elementIterator("chunks").next().getText());

                StreamThread streaming = new StreamThread(stream, document, chunkNumber, initialChunk + 1, totalChunks);
                streaming.start();

                player = new MediaPlayer(new Media(buffer.toURI().toString()));
                player.play();
                player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        slider.setValue(newValue.toSeconds() / (player.getTotalDuration().toSeconds() * totalChunks) * 100);
                        System.out.println(slider.getValue());
                    }
                });
            }else{
                System.out.println("Cancion no encontrada");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void pause(){
        if(player != null) {
            player.pause();
        }
    }

    public void unpause(){
        player.play();
    }

    public void stop(){
        player.stop();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused(){
        return paused;
    }

    public void setSlider(Slider slider){
        this.slider = slider;
    }

    public void forward(int slider){
        Duration duration = player.getTotalDuration();
        Duration time = duration.multiply(slider / 100);
        System.out.println(time.toSeconds());
        player.seek(time);
    }

    public int getAmplitude(){
        return 50;
    }
}
