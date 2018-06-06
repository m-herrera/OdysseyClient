package org.tec.datosII.OdysseyClient;

import com.Ostermiller.util.CircularByteBuffer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;


public class VideoThread extends Thread {
    private Document request;
    private int initialChunk;
    private boolean paused = true;
    private int pausedChunk;
    private int bufferSize = 983040;
    private int totalChunks;
    private double amplitude = 0;


    public DoubleProperty currentPercent = new SimpleDoubleProperty(0);

    public VideoThread(Document request, int initialChunk) {
        this.request = request;
        this.initialChunk = initialChunk;
    }

    @Override
    public void run() {
        paused = false;

        Element root = request.getRootElement();

        Element chunkNumber = root.addElement("chunk").addText(String.valueOf(initialChunk));

        String request = this.request.asXML();

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

                StreamThread streaming = new StreamThread(buffer.getOutputStream(), this.request, chunkNumber, initialChunk + 1, totalChunks);
                streaming.start();

                /*

                Aqui se reproduce el video

                 */

                this.pausedChunk = streaming.pause();
            } else {
                System.out.println("Cancion no encontrada");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
