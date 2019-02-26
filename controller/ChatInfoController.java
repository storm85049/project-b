package controller;


import client.ClientData;
import client.RemoteClient;
import controller.logger.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.json.simple.JSONObject;
import util.Actions;
import util.ModalUtil;
import util.ModeMapper;

import java.net.URL;
import java.util.ResourceBundle;


public class ChatInfoController implements Initializable, IController {


    @FXML
    private Label originInfo;

    @FXML
    private Label asymInfo;

    @FXML
    private Label symInfo;

    @FXML
    private Label keyInfo;

    @FXML
    private Label encryptedKeyInfo;

    @FXML
    private Tooltip tooltip;
    @FXML
    private Tooltip tooltip2;

    @FXML
    private Label header;

    @FXML
    private Button okBtn;
    private String referer;


    @FXML
    private BorderPane mainPane;

    @FXML
    private ImageView tooltipImage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String from = ClientData.getInstance().getIdFromLastRequest();
        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(from);
        String decryptedKey;
        decryptedKey = remoteClient.getSymEncryption().getModeSpecificKey();
        JSONObject json = remoteClient.getRequestedEncryptionData();
        JSONObject keys = (JSONObject) json.get("encryptionParams");
        String encryptedKey = (String) keys.get("encryptedKey");





        Platform.runLater(()->{
            if(this.getReferer().equals(Actions.REFERRER_CREATE)){
                header.setText("New Chat Request!");
                originInfo.setText("from " +remoteClient.getName());
            }
            else if(this.getReferer().equals(Actions.REFERRER_UPDATE)){
                header.setText(remoteClient.getName() +  " wants to switch keys ");
                originInfo.setText("");
            }
        });

        tooltip.setText(encryptedKey);
        if(encryptedKey.length() >= 30){
            encryptedKeyInfo.setText("Encrypted Key: " + encryptedKey.substring(0,30)+ "...");
        }else{
            encryptedKeyInfo.setText("Encrypted Key: " + encryptedKey);
        }

        if(decryptedKey.length() >= 30){
            keyInfo.setText("Decrypted Key: " + decryptedKey.substring(0,30)+ "...");
            tooltipImage.setVisible(true);
            tooltip2.setText(decryptedKey);
        }else{
            tooltipImage.setVisible(false);
            keyInfo.setText("Decrypted Key: " + decryptedKey);
        }

        symInfo.setText("his symmetric key is encrypted with " + ModeMapper.map(remoteClient.getSymEncryptionString()));
        asymInfo.setText(remoteClient.getName() + " wants to use " + remoteClient.getAsymEncryptionString().toUpperCase() + " as asymmetric encryption mode");

        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            this.getPane().getScene().getWindow().hide();
        });
    }


    private String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }


    @Override
    public Pane getPane() {
        return this.mainPane;
    }


}
