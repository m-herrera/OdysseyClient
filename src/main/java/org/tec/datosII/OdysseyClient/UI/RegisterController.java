package org.tec.datosII.OdysseyClient.UI;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.tec.datosII.OdysseyClient.App;

public class RegisterController {

    private InitController initController;

    public void setInitController(InitController controller){
        this.initController = controller;
    }

    @FXML
    private JFXTextField firstNameTextfield;

    @FXML
    private JFXTextField lastNameTextfield;

    @FXML
    private JFXTextField userTextfield;

    @FXML
    private JFXDatePicker birthdayPicker;

    @FXML
    private JFXPasswordField passwordTextfield;

    @FXML
    private JFXPasswordField repeatPasswordTextfield;

    @FXML
    private JFXCheckBox classicCheck;

    @FXML
    private JFXCheckBox reggaetonCheck;

    @FXML
    private JFXCheckBox popCheck;

    @FXML
    private JFXCheckBox electronicCheck;

    @FXML
    private JFXCheckBox indieCheck;

    @FXML
    private JFXCheckBox jazzCheck;

    @FXML
    private JFXCheckBox rockCheck;

    @FXML
    private JFXCheckBox metalCheck;

    @FXML
    private JFXCheckBox countryCheck;

    @FXML
    void changeToLogin(ActionEvent event) throws Exception{
        initController.loadLogin();
    }

    @FXML
    void register(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent page = loader.load();
        Scene mainScene = new Scene(page, App.width, App.height);

        App.getRootStage().setScene(mainScene);
        App.getRootStage().sizeToScene();
    }

}
