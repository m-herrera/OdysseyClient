package org.tec.datosII.OdysseyClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage rootStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        setRootStage(primaryStage);
        primaryStage.setTitle("Odissey");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    public static Stage getRootStage() {
        return rootStage;
    }

    public static void setRootStage(Stage rootStage) {
        App.rootStage = rootStage;
    }

    public static void main(String[] args){
        launch(args);
    }
}
