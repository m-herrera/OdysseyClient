package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.Authenticator;

public class LoginController {

    private InitController initController;

    public void setInitController(InitController controller){
        this.initController = controller;
    }

    @FXML
    private JFXTextField userTextfield;

    @FXML
    private JFXPasswordField passwordTextfield;

    @FXML
    private Label errorLabel;

    @FXML
    void changeToRegister(ActionEvent event) throws Exception {
        initController.loadRegister();
    }

    @FXML
    void login(ActionEvent event) throws Exception{
        String user = userTextfield.getText();
        String password = passwordTextfield.getText();

        Authenticator auth = new Authenticator();

        if(user.isEmpty() || password.isEmpty()){
            errorLabel.setText("All fields are required.");
        }else if(auth.login(user, password)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
            Parent page = loader.load();
            Scene mainScene = new Scene(page, App.width, App.height);

            App.getRootStage().setScene(mainScene);
            App.getRootStage().sizeToScene();
        }else{
            errorLabel.setText("Username or password is incorrect.");
        }
    }

}
