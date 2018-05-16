package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.Metadata;
import org.tec.datosII.OdysseyClient.NioClient;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class MainWindowController {

    ObservableList<Metadata> songs = FXCollections.observableArrayList();

    @FXML
    private ImageView playPauseBtn;

    @FXML
    private JFXSlider songSlider;

    @FXML
    private JFXListView<?> friendsList;

    @FXML
    private JFXTreeTableView<Metadata> songList;

    private JFXTreeTableColumn<Metadata, String> nameColumn = new JFXTreeTableColumn<>("Name");

    private JFXTreeTableColumn<Metadata, String> artistColumn = new JFXTreeTableColumn<>("Artist");

    private JFXTreeTableColumn<Metadata, String> yearColumn = new JFXTreeTableColumn<>("Year");

    private JFXTreeTableColumn<Metadata, String> albumColumn = new JFXTreeTableColumn<>("Album");

    private JFXTreeTableColumn<Metadata, String> genreColumn = new JFXTreeTableColumn<>("Genre");

    private JFXTreeTableColumn<Metadata, String> lyricsColumn = new JFXTreeTableColumn<>("Lyrics");

    @FXML
    private void initialize(){
        songList.setShowRoot(false);
        songList.setEditable(false);
        songList.getColumns().setAll(nameColumn,artistColumn,albumColumn,yearColumn, genreColumn, lyricsColumn);

    }

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
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Audio files", "*.mp3");
        browser.getExtensionFilters().setAll(filter);
        browser.setTitle("Select songs to upload");
        List<File> files = browser.showOpenMultipleDialog(App.getRootStage());
        if(!files.isEmpty()) {
            for (File file : files) {
                uploadToServer(file);
                System.out.println(file.getName());
            }
        }
    }

    void uploadToServer(File file){

        Metadata metadata = new Metadata(file.toPath().toString());

        metadata.addLyrics();

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "3");

        Element name = root.addElement("name").addText(metadata.name);
        Element artist = root.addElement("artist").addText(metadata.artist);
        Element year = root.addElement("year").addText(metadata.year);
        Element album = root.addElement("album").addText(metadata.album);
        Element genre = root.addElement("genre").addText(metadata.genre);
        Element lyrics = root.addElement("lyrics").addText(metadata.lyrics);

        Element cover = root.addElement("cover");
        if(metadata.cover != null) {
            String imageString = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try {
                ImageIO.write(metadata.cover, "png", bos);
                byte[] imageBytes = bos.toByteArray();

                String encodedFile = Base64.getEncoder().encodeToString(imageBytes);

                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Element content = root.addElement("content");

        try {
            byte[] binaryFile = Files.readAllBytes(file.toPath());
            String encodedFile = Base64.getEncoder().encodeToString(binaryFile);
            content.addText(encodedFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        String request = document.asXML();

        NioClient client = NioClient.getInstance();
        client.send(request.getBytes());
    }

    @FXML
    void contextMenu(ContextMenuEvent event){
        ContextMenu altMenu = new ContextMenu();
        MenuItem properties = new MenuItem("Properties");
        properties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                try {
                    PropertiesDialog dialog = new PropertiesDialog();
//                    dialog.showAndWait(new Metadata());
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private ObservableList<Metadata> populateTable(){
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "4");
        NioClient.getInstance().send(document.asXML().getBytes());
        return FXCollections.observableArrayList();
    }
}

