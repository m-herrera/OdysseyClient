package org.tec.datosII.OdysseyClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class MainWindowController {
    @FXML
    private Button button;

    @FXML
    private ToolBar toolBar;

    @FXML
    protected void buttonAction(ActionEvent event){
        System.out.println("Boton presionado");
    }
}
