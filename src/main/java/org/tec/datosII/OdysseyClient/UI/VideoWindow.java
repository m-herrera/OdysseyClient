package org.tec.datosII.OdysseyClient.UI;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.Metadata;
import org.tec.datosII.OdysseyClient.UI.PropertiesDialogController;

import java.io.IOException;

public class VideoWindow {
    /**
     * Stage de la ventana
     */
    private Stage videoPlayer;

    private VideoPlayerController controller;

    /**
     * Loader de la ventana
     */
    private FXMLLoader loader;

    /**
     * Constructor que carga la ventana
     * @throws IOException
     */
    VideoWindow() throws IOException {
        loader = new FXMLLoader(getClass().getResource("videoPlayer.fxml"));

        videoPlayer = new Stage();
        videoPlayer.setOnCloseRequest(event -> controller.stop());

        Parent window = loader.load();

        Scene scene = new Scene(window, 1280, 808);
        videoPlayer.setTitle("Video Player");
        videoPlayer.setScene(scene);
        videoPlayer.setResizable(true);
    }

    /**
     * Abre la ventana y espera
     * @param metadata Metadata del video a cargar
     */
    public void showAndWait(Metadata metadata){
        controller = loader.getController();
        controller.load(metadata);
        controller.setStage(videoPlayer);
        videoPlayer.showAndWait();
    }
}
