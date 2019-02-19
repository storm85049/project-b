package controller;


import client.ClientData;
import client.RemoteClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;


public class ChatInfoController implements Initializable {


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String from = ClientData.getInstance().getIdFromLastRequest();
        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(from);
        String decryptedKey = remoteClient.getSymEncryption().getModeSpecificKey();
        JSONObject json = remoteClient.getRequestedEncryptionData();
        JSONObject keys = (JSONObject) json.get("encryptionParams");
        String encryptedKey = (String) keys.get("encryptedKey");


//todo: elgamal simon fragen, nd noch vieles mehr

        tooltip.setText(encryptedKey);
        encryptedKeyInfo.setText("Encrypted Key: " + encryptedKey.substring(0,30)+ "...");
        keyInfo.setText("Decrypted Key: " + decryptedKey );
        symInfo.setText("his symmetric key is encrypted with " + remoteClient.getSymEncryptionString());
        asymInfo.setText(remoteClient.getName() + " wants to use " + remoteClient.getAsymEncryptionString().toUpperCase() + " as asymmetric encryption mode");
        originInfo.setText("from " +remoteClient.getName());

    }
}
