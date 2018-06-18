package org.tec.datosII.OdysseyClient.UI;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GifCreator {
    
    public GifCreator (String path, Integer count) {
        
        FFmpeg ffmpeg = null;
        FFprobe ffprobe = null;
        try {
            ffmpeg = new FFmpeg("/usr/local/Cellar/ffmpeg/4.0.1/bin/ffmpeg");
            ffprobe = new FFprobe("/usr/local/Cellar/ffmpeg/4.0.1/bin/ffprobe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        final String output = "/Users/roger/Movies/jap.mp4";
        final String output2 = path;
        
        if (count == null) {
            count = 0;
        }
        
        FFmpegBuilder builder = new FFmpegBuilder()
                
                                        .setInput(output)     // Filename, or a FFmpegProbeResult
                                        .overrideOutputFiles(true) // Override the output if it exists
                
                                        .addOutput("temp/output" + count.toString() + ".gif")   // Filename for the destination
                                        .setFormat("gif")        // Format is inferred from filename, or can be set
                                        .setDuration(7, TimeUnit.SECONDS)
                                        .setVideoFilter("select='gte(n\\,10)',scale=1000:-1")
                                        .setVideoResolution(256, 256)
                
                                        .setStartOffset(30, TimeUnit.SECONDS)
                
                
                                        .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                                        .done();
        
        if (ffmpeg != null && ffprobe != null) {
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

// Run a one-pass encode
            executor.createJob(builder).run();
        }
        
        System.out.println("DONE");
        
    }
    
}
