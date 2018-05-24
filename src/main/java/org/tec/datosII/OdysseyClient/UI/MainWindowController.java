package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class MainWindowController {
    TablePage[] tablePages = new TablePage[3];

    ObservableList<Metadata> tableList = FXCollections.observableArrayList();

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

    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Metadata, String> param) -> {
            ObservableValue<String> property = new ReadOnlyObjectWrapper<String>(param.getValue().getValue().name);
            return property;
        });

        artistColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Metadata, String> param) -> {
            ObservableValue<String> property = new ReadOnlyObjectWrapper<String>(param.getValue().getValue().artist);
            return property;
        });

        albumColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Metadata, String> param) -> {
            ObservableValue<String> property = new ReadOnlyObjectWrapper<String>(param.getValue().getValue().album);
            return property;
        });

        yearColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Metadata, String> param) -> {
            ObservableValue<String> property = new ReadOnlyObjectWrapper<String>(param.getValue().getValue().year);
            return property;
        });

        genreColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Metadata, String> param) -> {
            ObservableValue<String> property = new ReadOnlyObjectWrapper<String>(param.getValue().getValue().genre);
            return property;
        });

        final TreeItem<Metadata> root = new RecursiveTreeItem<>(tableList, RecursiveTreeObject::getChildren);

        songList.setRoot(root);
        songList.setShowRoot(false);
        songList.setEditable(false);
        songList.getColumns().setAll(nameColumn,artistColumn,albumColumn,yearColumn, genreColumn);



        tablePages[0] = populateTable(0);
        tableList.addAll(tablePages[0].songs);

        tablePages[1] = populateTable(1);
        tableList.addAll(tablePages[1].songs);

        tablePages[2] = populateTable(2);
        tableList.addAll(tablePages[2].songs);

//        songList.refresh();
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
//                    dialog.showAndWait();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private TablePage populateTable(int pageNumber){
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "4");
        root.addElement("sortBy").addText("name");
        root.addElement("sortWith").addText("quickSort");
        root.addElement("page").addText(String.valueOf(pageNumber));

        NioClient client = NioClient.getInstance();
        String request = document.asXML();
        ResponseHandler handler = client.send(request.getBytes());

        TablePage page = null;
        try {
            page = new TablePage();
            Document response = handler.getXmlResponse();
            Element responseRoot = response.getRootElement();
            page.pageNumber = pageNumber;
            page.totalSongs = Integer.parseInt(responseRoot.elementIterator("numberOfSongs").next().getText());
            page.pages = Integer.parseInt(responseRoot.elementIterator("pages").next().getText());
            page.pageSize = Integer.parseInt(responseRoot.elementIterator("pageSize").next().getText());

            Element songs = responseRoot.elementIterator("songs").next();
            for (Element song : songs.elements()) {
                Metadata newSong = new Metadata();

                newSong.name = song.elementIterator("name").next().getText();
                newSong.album = song.elementIterator("album").next().getText();
                newSong.artist = song.elementIterator("artist").next().getText();
                page.songs.addAll(newSong);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }


        return page;
    }

    private void updateSongs(){

    }
}

