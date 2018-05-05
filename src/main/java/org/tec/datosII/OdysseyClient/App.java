package org.tec.datosII.OdysseyClient;

import javafx.application.Application;
import javafx.stage.Stage;
import org.tec.datosII.OdysseyClient.UI.LoginWindow;

public class App extends Application {
    private static Stage rootStage;
    public static int width = 1280;
    public static int height = 720;
    private static App instance;

    public App(){
        instance = this;
    }

    public static App getInstance(){
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

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
