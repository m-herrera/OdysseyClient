package org.tec.datosII.OdysseyClient.UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class InitController {
    @FXML
    private AnchorPane inputPane;

    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
    Node loginScene;

    FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("register.fxml"));
    Node registerScene;

    public InitController() throws Exception{
        loginScene = (Node) loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.setInitController(this);

        registerScene = (Node) registerLoader.load();
        RegisterController registerController = registerLoader.getController();
        registerController.setInitController(this);
    }

    public void loadLogin() throws Exception{
        inputPane.getChildren().setAll(loginScene);
    }

    public void loadRegister() throws Exception{
        inputPane.getChildren().setAll(registerScene);
    }

}
