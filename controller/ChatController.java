package controller;

import animatefx.animation.Flash;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable{

    @FXML
    HBox logoHBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void zoomIn(){
        new Flash(logoHBox).play();
    }
}
