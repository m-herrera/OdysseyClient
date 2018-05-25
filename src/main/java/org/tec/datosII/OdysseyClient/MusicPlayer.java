package org.tec.datosII.OdysseyClient;

import javafx.scene.media.MediaPlayer;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Base64;

public class MusicPlayer {
    private static MusicPlayer instance;

    private MusicPlayer(){}

    public static MusicPlayer getInstance(){
        if(instance == null){
            instance = new MusicPlayer();
        }
        return instance;
    }

    public void play(Metadata song, int chunk){
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "5");

        root.addElement("name").addText(song.name);
        root.addElement("artist").addText(song.artist);
        root.addElement("year").addText(song.year);
        root.addElement("album").addText(song.album);
        root.addElement("genre").addText(song.genre);
        root.addElement("chunk").addText(String.valueOf(chunk));

        String request = document.asXML();

        NioClient client = NioClient.getInstance();
        ResponseHandler handler = client.send(request.getBytes());
        try {
            System.out.println(handler.getStrResponse());
            Document response = handler.getXmlResponse();
            String audio = response.getRootElement().elementIterator().next().getText();
            InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(audio));


            Clip clip = AudioSystem.getClip();
            AudioInputStream buffer = AudioSystem.getAudioInputStream(stream);
            clip.open(buffer);
            clip.start();


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
