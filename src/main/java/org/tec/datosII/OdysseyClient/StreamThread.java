package org.tec.datosII.OdysseyClient;

import com.Ostermiller.util.CircularByteBuffer;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Base64;

public class StreamThread extends Thread {
    OutputStream stream;
    Document request;
    Element chunkNumber;
    int chunk;
    int totalChunks;

    public StreamThread(OutputStream stream, Document request, Element chunkNumber, int initialChunk, int totalChunks){
        this.stream = stream;
        this.request = request;
        this.chunkNumber = chunkNumber;
        this.chunk = initialChunk;
        this.totalChunks = totalChunks;
    }

    @Override
    public void run(){
        while(this.chunk < this.totalChunks) {
            this.chunkNumber.setText(String.valueOf(chunk));

            String request = this.request.asXML();

            System.out.println(request);

            NioClient client = NioClient.getInstance();
            ResponseHandler handler = client.send(request.getBytes());

            try {
                System.out.println(handler.getStrResponse());
                Document response = handler.getXmlResponse();


                String audio = response.getRootElement().elementIterator("content").next().getText();
                stream.write(Base64.getDecoder().decode(audio));
            }catch (Exception ex){
                ex.printStackTrace();
            }

            chunk++;
        }
    }
}
