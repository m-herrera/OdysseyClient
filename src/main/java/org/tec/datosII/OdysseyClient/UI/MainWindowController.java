package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javazoom.jl.converter.Converter;
import javazoom.jl.player.Player;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tec.datosII.OdysseyClient.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * Controlador de la ventana principal
 */
public class MainWindowController {

    /**
     * Paginas de canciones en memoria
     */
    TablePage[] tablePages = new TablePage[3];

    /**
     * Lista de canciones en memoria
     */
    ObservableList<Metadata> tableList = FXCollections.observableArrayList();

    /**
     * Boton de reproducir/pausar
     */
    @FXML
    private ImageView playPauseBtn;

    /**
     * Deslizador para la reproduccion
     */
    @FXML
    private JFXSlider songSlider;

    /**
     * Lista de amigos
     */
    @FXML
    private JFXListView<?> friendsList;

    /**
     * Tabla con la lista de canciones
     */
    @FXML
    private JFXTreeTableView<Metadata> songList;

    /**
     * Columna con el nombre
     */
    private JFXTreeTableColumn<Metadata, String> nameColumn = new JFXTreeTableColumn<>("Name");

    /**
     * Columna con el artista
     */
    private JFXTreeTableColumn<Metadata, String> artistColumn = new JFXTreeTableColumn<>("Artist");

    /**
     * Columna con el anno
     */
    private JFXTreeTableColumn<Metadata, String> yearColumn = new JFXTreeTableColumn<>("Year");


    /**
     * Columna con el album
     */
    private JFXTreeTableColumn<Metadata, String> albumColumn = new JFXTreeTableColumn<>("Album");


    /**
     * Columna con el genero
     */
    private JFXTreeTableColumn<Metadata, String> genreColumn = new JFXTreeTableColumn<>("Genre");

    int currentPage;

    ScrollBar scrollBar;
    /**
     * Configuracion inicial de la vista
     */
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

        updateSongs();
    }

    /**
     * Handler para reproducir la siguiente cancion
     * @param event
     */
    @FXML
    void nextSong(ActionEvent event) {

    }

    /**
     * Handler para reproducir/pausar la cancion
     * @param event
     */
    @FXML
    void playPauseSong(ActionEvent event) {

    }

    /**
     * Handler para reproducir la cancion desde la lista
     * @param event
     */
    @FXML
    void playSong(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            TreeItem<Metadata> treeItem = songList.getSelectionModel().getSelectedItem();
            if(treeItem == null){
                return;
            }

            MusicPlayer.getInstance().play(treeItem.getValue(), 0);
        }
    }

    /**
     * Handler para reproducir la cancion anterior
     * @param event
     */
    @FXML
    void prevSong(ActionEvent event) {

    }

    /**
     * Handler para cargar nuevas paginas a la lista
     * @param event
     */
    @FXML
    void scrollHandler(ScrollEvent event) {
        if(scrollBar == null){
            scrollBar = getVerticalScrollbar(songList);

            scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if(newValue.doubleValue() == scrollBar.getMin() && currentPage != 1){
                        currentPage--;
                        tableList.removeAll(tablePages[2].songs);
                        tablePages[2] = tablePages[1];
                        tablePages[1] = tablePages[0];
                        tablePages[0] = populateTable(currentPage - 1);
                        tableList.addAll(0, tablePages[0].songs); // No funciona, anade al final
                        songList.refresh();
                    }
                    if(newValue.doubleValue() == scrollBar.getMax() && tablePages[2].songs.size() == 10){
                        currentPage++;
                        tableList.removeAll(tablePages[0].songs);
                        tablePages[0] = tablePages[1];
                        tablePages[1] = tablePages[2];
                        tablePages[2] = populateTable(currentPage + 1);
                        tableList.addAll(tablePages[2].songs);
                        songList.refresh();
                    }
                }
            });
        }
    }

    /**
     * Handler para reordenar la canciones
     * @param event
     */
    @FXML
    void sort(ActionEvent event) {

    }

    /**
     * Handler para seleccionar las canciones a subir al servidor
     * @param event
     */
    @FXML
    void uploadSong(ActionEvent event){
        FileChooser browser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Audio files", "*.mp3");
        browser.getExtensionFilters().setAll(filter);
        browser.setTitle("Select songs to upload");
        List<File> files = browser.showOpenMultipleDialog(App.getRootStage());
        if(files != null) {
            for (File file : files) {
                uploadToServer(file);
                System.out.println(file.getName());
            }
        }
        updateSongs();
    }

    /**
     * Metodo para subir un archivo al servidor con toda su metadata asociada
     * @param file
     */
    void uploadToServer(File file){

        Metadata metadata = new Metadata(file.toPath().toString());

        metadata.addLyrics();

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "3");

        root.addElement("name").addText(metadata.name);
        root.addElement("artist").addText(metadata.artist);
        root.addElement("year").addText(metadata.year);
        root.addElement("album").addText(metadata.album);
        root.addElement("genre").addText(metadata.genre);
        root.addElement("lyrics").addText(metadata.lyrics);

        Element cover = root.addElement("cover");
        if(metadata.cover != null) {
            String imageString = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(metadata.cover, null), "png", bos);
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
            byte[] binaryFile = Files.readAllBytes(file.toPath());

            String encodedFile = Base64.getEncoder().encodeToString(binaryFile);
            content.addText(encodedFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        String request = document.asXML();

        NioClient client = NioClient.getInstance();
        ResponseHandler handler = client.send(request.getBytes());
        System.out.println(handler.getStrResponse());

    }

    /**
     * Handler para el menu de cada cancion
     * @param event
     */
    @FXML
    void contextMenu(ContextMenuEvent event){
        ContextMenu altMenu = new ContextMenu();
        MenuItem properties = new MenuItem("Properties");
        properties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                try {
                    Metadata selected = songList.getSelectionModel().getSelectedItem().getValue();
                    PropertiesDialog dialog = new PropertiesDialog();
                    dialog.showAndWait(selected);
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });

        altMenu.getItems().add(properties);
        altMenu.show(App.getRootStage(), event.getScreenX(), event.getScreenY());
    }

    /**
     * Handler para cargar una pagina de canciones a memoria
     * @param pageNumber Numero de la pagina a solicitar
     * @return TablePage con los datos de las canciones
     */
    private TablePage populateTable(int pageNumber){

        System.out.println("Pidiendo pagina " + pageNumber);

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "4");
        root.addElement("sortBy").addText("name");
        root.addElement("sortWith").addText("quickSort");
        root.addElement("page").addText(String.valueOf(pageNumber));

        NioClient client = NioClient.getInstance();
        String request = document.asXML();
        ResponseHandler handler = client.send(request.getBytes());

        System.out.println(handler.getStrResponse());

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

    /**
     * Handler para actualizar la lista de canciones
     */
    private void updateSongs(){
        tableList.clear();
        currentPage = 1;

        tablePages[0] = populateTable(0);
        tableList.addAll(tablePages[0].songs);

        tablePages[1] = populateTable(1);
        tableList.addAll(tablePages[1].songs);

        tablePages[2] = populateTable(2);
        tableList.addAll(tablePages[2].songs);
    }

    private ScrollBar getVerticalScrollbar(JFXTreeTableView<?> table) {
        ScrollBar result = null;
        for (Node n : table.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }
        return result;
    }

}

