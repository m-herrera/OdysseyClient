package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.tec.datosII.OdysseyClient.Metadata;

/**
 * Controlador de la ventana de propiedades
 */
public class PropertiesDialogController {
    /**
     * Textfield con el nombre de la cancion
     */

    @FXML
    private JFXTextField nameTextfield;
    /**
     * Textfield con el nombre del artista
     */

    @FXML
    private JFXTextField artistTextfield;

    /**
     * Textfield con el ano
     */

    @FXML
    private JFXTextField yearTextfield;

    /**
     * Textfield con el album
     */

    @FXML
    private JFXTextField albumTextfield;

    /**
     * Textfield con el genero
     */

    @FXML
    private JFXTextField genreTextfield;

    /**
     * Imagen de portada del album
     */
    @FXML
    private ImageView coverArt;

    /**
     * Handler de cancelar
     * @param event
     */
    @FXML
    void cancelBtn(ActionEvent event) {

    }

    /**
     * Handler para cambiar la imagen
     * @param event
     */
    @FXML
    void changeCoverArt(MouseEvent event) {

    }

    /**
     * Handler para guardar los datos
     * @param event
     */
    @FXML
    void saveBtn(ActionEvent event) {

    }

    /**
     * Carga los datos de la cancion a la interfaz
     * @param metadata Metadata de la cancion
     */
    public void load(Metadata metadata){
        nameTextfield.setText(metadata.name);
        artistTextfield.setText(metadata.artist);
        yearTextfield.setText(metadata.year);
        albumTextfield.setText(metadata.album);
        genreTextfield.setText(metadata.genre);
        coverArt.setImage(metadata.cover);
    }

    /**
     * Obtiene los datos de la cancion editados
     * @return Metadata de la cancion
     */
    public Metadata retrieve() {
//        return new Metadata();
        return null;
    }

}
