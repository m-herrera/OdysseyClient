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
import javafx.scene.control.Label;
import org.tec.datosII.OdysseyClient.App;
import org.tec.datosII.OdysseyClient.Authenticator;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class RegisterController {

    private InitController initController;

    public void setInitController(InitController controller){
        this.initController = controller;
    }

    @FXML
    private Label errorLabel;

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
        String fname = firstNameTextfield.getText();
        String lname = lastNameTextfield.getText();
        String user = userTextfield.getText();
        LocalDate birthday = birthdayPicker.getValue();
        String password = passwordTextfield.getText();
        String confirmPass = repeatPasswordTextfield.getText();

        JFXCheckBox[] genres = {classicCheck, reggaetonCheck, popCheck, electronicCheck, indieCheck, jazzCheck, rockCheck, metalCheck, countryCheck};

        LinkedList<String> selectedGenres = new LinkedList<String>();

        for (JFXCheckBox genre : genres) {
            if(genre.isSelected()){
                selectedGenres.add(genre.getText());
            }
        }

        Authenticator auth = new Authenticator();

        if(fname.isEmpty() || lname.isEmpty() || user.isEmpty() || password.isEmpty() || selectedGenres.isEmpty() || confirmPass.isEmpty()) {
            errorLabel.setText("All fields are required.");
        }else if(!password.equals(confirmPass)){
            errorLabel.setText("Passwords don't match");
        }else if(auth.register(fname, lname, user, password, birthday, selectedGenres.toArray(new String[selectedGenres.size()]))){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
            Parent page = loader.load();
            Scene mainScene = new Scene(page, App.width, App.height);

            App.getRootStage().setScene(mainScene);
            App.getRootStage().sizeToScene();
        }else{
            errorLabel.setText("Username is already taken.");
        }
    }

}
