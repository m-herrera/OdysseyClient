package org.tec.datosII.OdysseyClient.Login;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class InitController {
    @FXML
    private AnchorPane inputPane;

    public void loadLogin() throws Exception{
        inputPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("login.fxml")));
    }


    @FXML
    protected void login(){

    }

    @FXML
    protected void changeToRegister(){

    }
}
