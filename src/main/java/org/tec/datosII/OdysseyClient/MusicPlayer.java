package org.tec.datosII.OdysseyClient;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Reproductor de audio en streaming
 */
public class MusicPlayer{
    /**
     * Instancia unica del reproductor
     */
    private static MusicPlayer instance;
    
    /**
     * Hilo unico del reproductor
     */
    AudioThread audioThread;

    int currentChunk;

    Metadata currentSong;

    Slider slider;

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
        if (audioThread != null && audioThread.isAlive()) {
            pause();
        }
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "5");

        root.addElement("name").addText(song.name);
        root.addElement("artist").addText(song.artist);
        root.addElement("year").addText(song.year);
        root.addElement("album").addText(song.album);
        root.addElement("genre").addText(song.genre);
    
        audioThread = new AudioThread(document, chunk);
        audioThread.currentPercent.addListener(new ChangeListener<Number>() {
            @Override
            public void changed (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                slider.adjustValue(newValue.doubleValue());
            }
        });
        audioThread.start();
    }

    public void pause(){
        if (audioThread != null) {
            currentChunk = audioThread.pause();
        }
    }

    public void unpause(){
        play(currentSong, currentChunk);
    }



    public boolean isPlaying() {
        if (audioThread != null) {
            return audioThread.isAlive();
        } else {
            return false;
        }
    }

    public boolean isPaused(){
        return currentSong != null;
    }

    public void setSlider(Slider slider){
        this.slider = slider;
    }

    public void forward(int slider){
        int chunk = audioThread.getTotalChunks() * slider / 100;
        play(currentSong, chunk);
    }
    
    public double getAmplitude () {
        if (this.audioThread != null) {
            return audioThread.getAmplitude();
        } else {
            return - 1;
        }
    }
}
