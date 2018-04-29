package org.tec.datosII.OdysseyClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tec.datosII.OdysseyClient.Login.LoginWindow;

public class App extends Application {
    private static Stage rootStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        int width = 1280;
        int height = 720;


//        Parent root = FXMLLoader.load(getClass().getResource("../Login/loginWindow.fxml"));
        setRootStage(primaryStage);
        primaryStage.setTitle("Odissey");

        LoginWindow loginWindow = new LoginWindow();

        primaryStage.setScene(loginWindow.load(width, height));
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
