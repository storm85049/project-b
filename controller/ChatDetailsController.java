package controller;

import client.ClientData;
import client.ObjectIOSingleton;
import client.RemoteClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.json.simple.JSONObject;
import util.Actions;
import util.JSONUtil;
import util.ModeMapper;

import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static util.Actions.MODE_DES;

public class ChatDetailsController implements Initializable, IController {


    @FXML
    private BorderPane mainPane;

    @FXML
    private Button okBtn;
    @FXML
    private Label heading;
    @FXML
    private Label onlineInfo;
    @FXML
    private Label messageCountInfo;
    @FXML
    private Label asymInfo;
    @FXML
    private Label symInfo;
    @FXML
    private Label decryptedInfo;
    @FXML
    private Label encryptedInfo;
    @FXML
    private Tooltip tooltip;
    @FXML
    private Tooltip tooltipDecrypted;

    @FXML
    private ImageView tooltipImageDecrypted;
    @FXML
    private ImageView tooltipImageEncrypted;








    @Override
    public Pane getPane() {
        return this.mainPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientFromOpenChat();

        String symMode = ModeMapper.map(remoteClient.getSymEncryptionString());
        String asymMode = ModeMapper.map(remoteClient.getAsymEncryptionString());
        String remoteClientName = remoteClient.getName();
        String onlineSince = remoteClient.getOnlineSince();
        String encryptedKey = remoteClient.getAsymKeyReadable();
        String decryptedKey;
        if(remoteClient.getSymEncryptionString().equals(Actions.MODE_DES)){
            decryptedKey = remoteClient.getDesKeyMap().get("key");
        }else{
            decryptedKey = remoteClient.getSymEncryption().getModeSpecificKey();
        }


        String messageCount;
        ArrayList<JSONObject> list = remoteClient.getChatHistory();
        if(list == null){
            messageCount = "0";
        }else{
            messageCount = String.valueOf(list.size());
        }


        heading.setText("Chat with " +remoteClientName);
        onlineInfo.setText("Online since : " +onlineSince);
        asymInfo.setText("Initial Asymmetric Encryption : " + asymMode);
        symInfo.setText("Symmetric Encryption : " +symMode);


        if(encryptedKey.length() >= 20){
            encryptedInfo.setText("Symmetric key encrypted with " + asymMode + " : " + encryptedKey.substring(0,19) + "... ");
        }else{
            encryptedInfo.setText("Symmetric key encrypted with " + asymMode + " : " + encryptedKey);
        }if(decryptedKey.length() >= 20){
            decryptedInfo.setText("Symmetric decrypted key : " + decryptedKey.substring(0,19) + "... ");
        }else{
            decryptedInfo.setText("Symmetric decrypted key : " + decryptedKey);
        }
        messageCountInfo.setText("Messages sent: " + messageCount);


        if(decryptedKey.length() >= 20){
            tooltipImageDecrypted.setVisible(true);
            tooltipDecrypted.setText(decryptedKey);
        }

        if(encryptedKey.length() >= 20){
            tooltipImageEncrypted.setVisible(true);
            tooltip.setText(encryptedKey);
        }



        okBtn.setOnAction(event -> {
            this.getPane().getScene().getWindow().hide();
        });










    }
}
