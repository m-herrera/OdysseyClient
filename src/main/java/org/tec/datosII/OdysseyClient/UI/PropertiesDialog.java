package org.tec.datosII.OdysseyClient.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.Metadata;

import java.io.IOException;

/**
 * Ventana de propiedades de una cancion
 */
public class PropertiesDialog {
    /**
     * Stage de la ventana
     */
    private Stage dialog;

    /**
     * Loader de la ventana
     */
    private FXMLLoader loader;

    /**
     * Constructor que carga la ventana
     * @throws IOException
     */
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

    /**
     * Abre la ventana y espera
     * @param metadata Metadata de la cancion a cargar
     * @return Metadata editada de la cancion
     */
    public Metadata showAndWait(Metadata metadata){
        PropertiesDialogController controller = loader.getController();
        controller.load(metadata);
        dialog.showAndWait();
        return controller.retrieve();
    }
}
