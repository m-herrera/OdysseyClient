package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.NioClient;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class MainWindowController {

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private JFXSlider songSlider;

    @FXML
    private JFXListView<?> friendsList;

    @FXML
    private JFXTreeTableView<?> songList;

    @FXML
    void nextSong(ActionEvent event) {

    }

    @FXML
    void playPauseSong(ActionEvent event) {

    }

    @FXML
    void prevSong(ActionEvent event) {

    }

    @FXML
    void uploadSong(ActionEvent event){
        FileChooser browser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("MP3", "mp3");
        browser.setSelectedExtensionFilter(filter);
        browser.setTitle("Select songs to upload");
        List<File> files = browser.showOpenMultipleDialog(App.getRootStage());
        for (File file:files) {
            uploadToServer(file);
            System.out.println(file.getName());
        }
    }

    void uploadToServer(File file){
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "3");
        Element name = root.addElement("name").addText(file.getName());
        Element content = root.addElement("content");

        try {
            byte[] binaryFile = Files.readAllBytes(file.toPath());
            String encodedFile = Base64.getEncoder().encodeToString(binaryFile);
            System.out.println(encodedFile);
            content.addText(encodedFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        NioClient client = NioClient.getInstance();
        client.send(document.asXML().getBytes());
    }

}

