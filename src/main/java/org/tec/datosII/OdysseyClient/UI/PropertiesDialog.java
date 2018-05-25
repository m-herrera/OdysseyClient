package org.tec.datosII.OdysseyClient.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.Metadata;

import java.io.IOException;

public class PropertiesDialog {

    private Stage dialog;
    private FXMLLoader loader;

    PropertiesDialog() throws IOException {
        loader = new FXMLLoader(getClass().getResource("properties.fxml"));

        dialog = new Stage();
        dialog.initOwner(App.getRootStage());
        dialog.initModality(Modality.WINDOW_MODAL);

        Parent window = loader.load();

        Scene scene = new Scene(window, 600, 400);
        dialog.setTitle("Properties");
        dialog.setScene(scene);
        dialog.setResizable(false);
    }

    public Metadata showAndWait(Metadata metadata){
        PropertiesDialogController controller = loader.getController();
        controller.load(metadata);
        dialog.showAndWait();
        return controller.retrieve();
    }
}
