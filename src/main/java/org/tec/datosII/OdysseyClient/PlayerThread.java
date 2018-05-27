package org.tec.datosII.OdysseyClient;

import com.Ostermiller.util.CircularByteBuffer;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.Base64;

public class PlayerThread extends Thread {
    Document request;
    int initialChunk;

    public PlayerThread(Document request, int initialChunk){
        this.request = request;
        this.initialChunk = initialChunk;
    }

    @Override
    public void run(){
        Element root = request.getRootElement();

        Element chunkNumber = root.addElement("chunk").addText(String.valueOf(initialChunk));

        String request = this.request.asXML();

        System.out.println(request);

        NioClient client = NioClient.getInstance();
        ResponseHandler handler = client.send(request.getBytes());

        try {
            Document response = handler.getXmlResponse();
            String audio = response.getRootElement().elementIterator("content").next().getText();

            CircularByteBuffer buffer = new CircularByteBuffer(1638400);
            buffer.getOutputStream().write(Base64.getDecoder().decode(audio));

            InputStream stream = buffer.getInputStream();

            int totalChunks = Integer.parseInt(response.getRootElement().elementIterator("chunks").next().getText());

            Thread streaming = new StreamThread(buffer.getOutputStream(), this.request, chunkNumber, initialChunk + 1, totalChunks);
            streaming.start();

            AudioInputStream in= AudioSystem.getAudioInputStream(stream);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            // Play now.
            rawplay(decodedFormat, din);
            in.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
    {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null)
        {
            // Start
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1 && !this.isInterrupted())
            {
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}
