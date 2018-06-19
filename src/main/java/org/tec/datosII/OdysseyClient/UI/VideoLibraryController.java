package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.Metadata;
import org.tec.datosII.OdysseyClient.NioClient;
import org.tec.datosII.OdysseyClient.ResponseHandler;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class VideoLibraryController {
    
    
    @FXML
    private ImageView imgVid1;
    
    @FXML
    private Label labelVid1;
    
    @FXML
    private ImageView imgVid2;
    
    @FXML
    private Label labelVid2;
    
    @FXML
    private ImageView imgVid3;
    
    @FXML
    private Label labelVid3;
    
    @FXML
    private ImageView imgVid4;
    
    @FXML
    private Label labelVid4;
    
    @FXML
    private ImageView imgVid5;
    
    @FXML
    private Label labelVid5;
    
    @FXML
    private JFXButton prevPageBtn;
    
    @FXML
    private JFXButton nextPageBtn;
    
    @FXML
    void nextPageRequest (ActionEvent event) {
    
    }
    
    @FXML
    void openMl (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
            Parent page = loader.load();
            Scene mainScene = new Scene(page, App.width, App.height);
            App.getRootStage().setScene(mainScene);
            App.getRootStage().sizeToScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    void openSearchDialog (ActionEvent event) {
    
    }
    
    @FXML
    void prevPageRequest (ActionEvent event) {
    
    }
    
    @FXML
    void uploadSong (ActionEvent event) {
        FileChooser browser = new FileChooser();
        FileChooser.ExtensionFilter videoFilter = new FileChooser.ExtensionFilter("Video files", "*.mp4");
        browser.getExtensionFilters().add(videoFilter);
        browser.setTitle("Select files to upload");
        List<File> files = browser.showOpenMultipleDialog(App.getRootStage());
        if (files != null) {
            for (File file : files) {
                uploadToServer(file);
                System.out.println(file.getName());
            }
        }
    }
    
    private void uploadToServer (File file) {
        Metadata metadata = new Metadata(file.toPath().toString());
        
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "3");
        
        root.addElement("name").addText(metadata.name);
        root.addElement("type").addText(metadata.type);
        
        Element cover = root.addElement("cover");
        
        if (metadata.cover != null) {
            String imageString = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(metadata.cover, null), "gif", bos);
                byte[] imageBytes = bos.toByteArray();
                
                String encodedFile = Base64.getEncoder().encodeToString(imageBytes);
                cover.addText(encodedFile);
                
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        
        Element content = root.addElement("content");
        
        try {
            ByteArrayInputStream binaryFile = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
            
            int bufSize = 10485760;
            byte[] binaryChunk = new byte[bufSize];
            int readData;
            
            Element chunk = root.addElement("chunk");
            root.addElement("totalChunks").addText(String.valueOf(binaryFile.available() / bufSize));
            
            int currentChunk = 0;
            
            while (binaryFile.available() > 0) {
                readData = binaryFile.read(binaryChunk, 0, binaryChunk.length);
                
                String encodedFile = Base64.getEncoder().encodeToString(Arrays.copyOf(binaryChunk, readData));
                content.setText(encodedFile);
                chunk.setText(String.valueOf(currentChunk));
                String request = document.asXML();
                
                NioClient client = NioClient.getInstance();
                ResponseHandler handler = client.send(request.getBytes());
                handler.getStrResponse();
                
                currentChunk++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void fetchVideos () {
        
        ArrayList<Label> labels = new ArrayList<>();
        labels.add(labelVid1);
        labels.add(labelVid2);
        labels.add(labelVid3);
        labels.add(labelVid4);
        labels.add(labelVid5);
        
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(imgVid1);
        imageViews.add(imgVid2);
        imageViews.add(imgVid3);
        imageViews.add(imgVid4);
        imageViews.add(imgVid5);
        
        for (Label l : labels) {
            Metadata m = new Metadata();
            l.setText(m.name);
        }
        
        for (ImageView v : imageViews) {
            Metadata m = new Metadata();
            if (m.cover == null) {
                setNotFound(v);
            } else {
                v.setImage(m.cover);
            }
            
        }
        
    }
    
    void setNotFound (ImageView img) {
        
        Image lost = new Image("icons/vid?.png");
        img.setImage(lost);
        
    }
    
}
