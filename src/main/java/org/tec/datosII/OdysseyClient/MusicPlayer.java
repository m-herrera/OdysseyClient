package org.tec.datosII.OdysseyClient;

import javazoom.jl.player.Player;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Reproductor de audio en streaming
 */
public class MusicPlayer{
    /**
     * Instancia unica del reproductor
     */
    private static MusicPlayer instance;

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
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "5");

        root.addElement("name").addText(song.name);
        root.addElement("artist").addText(song.artist);
        root.addElement("year").addText(song.year);
        root.addElement("album").addText(song.album);
        root.addElement("genre").addText(song.genre);

        Thread playerThread = new PlayerThread(document, chunk);
        playerThread.start();
    }

}
