package controller;

import animatefx.animation.Flash;
import animatefx.animation.SlideInLeft;
import client.ClientData;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import sun.misc.IOUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable{

    @FXML
    HBox logoHBox;

    @FXML
    Text status;

    @FXML
    TextField textInput;

    @FXML
    ListView chat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InetAddress inetAddress = ClientData.getInstance().getServerAdress();

        String preText = status.getText();
        String text = inetAddress == null ? "not connected" : "connected to " + inetAddress;
        status.setText(preText + text);

        textInput.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode() == KeyCode.ENTER){
                        sendRequest();
                    }
                }
            });
    }
    public void zoomIn(){
        new Flash(logoHBox).play();
    }

    public void sendRequest(){
        System.out.println("send message request");
    }





}
