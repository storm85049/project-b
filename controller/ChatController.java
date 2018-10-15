package controller;

import animatefx.animation.Flash;
import animatefx.animation.SlideInLeft;
import client.ClientData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable{

    @FXML
    HBox logoHBox;

    @FXML
    Text status;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InetAddress inetAddress = ClientData.getInstance().getServerAdress();

        String preText = status.getText();
        String text = inetAddress == null ? "not connected" : "connected to " + inetAddress;
        status.setText(preText + text);

    }
    public void zoomIn(){
        new Flash(logoHBox).play();
    }


}
