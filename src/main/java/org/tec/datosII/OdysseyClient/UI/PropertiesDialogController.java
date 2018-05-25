package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.tec.datosII.OdysseyClient.Metadata;

public class PropertiesDialogController {

    @FXML
    private JFXTextField nameTextfield;

    @FXML
    private JFXTextField artistTextfield;

    @FXML
    private JFXTextField yearTextfield;

    @FXML
    private JFXTextField albumTextfield;

    @FXML
    private JFXTextField genreTextfield;

    @FXML
    private ImageView coverArt;

    @FXML
    void cancelBtn(ActionEvent event) {

    }

    @FXML
    void changeCoverArt(MouseEvent event) {

    }

    @FXML
    void saveBtn(ActionEvent event) {

    }

    public void load(Metadata metadata){
        nameTextfield.setText(metadata.name);
        artistTextfield.setText(metadata.artist);
        yearTextfield.setText(metadata.year);
        albumTextfield.setText(metadata.album);
        genreTextfield.setText(metadata.genre);
        coverArt.setImage(metadata.cover);
    }

    public Metadata retrieve() {
//        return new Metadata();
        return null;
    }

}
