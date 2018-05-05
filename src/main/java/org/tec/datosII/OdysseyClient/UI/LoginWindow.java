package org.tec.datosII.OdysseyClient.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginWindow{

    public Scene load(int width, int height) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("init.fxml"));
        Parent root = loader.load();
        Scene loginScene = new Scene(root, width, height);

        InitController controller = loader.getController();
        controller.loadLogin();

        return loginScene;
    }
}
