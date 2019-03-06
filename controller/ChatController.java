package controller;

import animatefx.animation.Flash;
import client.ClientData;
import client.ObjectIOSingleton;
import client.RemoteClient;
import util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.xml.internal.utils.StringBufferPool;
import controller.logger.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import oop.client.ActionManager;
import oop.client.ChatViewAction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.rmi.runtime.Log;
import util.Actions;
import util.ChatViewUtil;
import util.JSONUtil;
import util.ModalUtil;

import java.net.InetAddress;
import java.net.URL;
import java.rmi.Remote;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatController implements Initializable, Observer, IController {


    @FXML
    BorderPane mainPane;

    @FXML
    HBox logoHBox;

    @FXML
    Text status;

    @FXML
    TextField textInput;


    @FXML
    VBox chatlist;

    @FXML
    ScrollPane scrollPane;

    @FXML
    ScrollPane log;

    @FXML
    VBox logPane;

    @FXML
    VBox chatBox;

    private ActionManager actionManager;

    @FXML
    Button sendButton;

    @FXML
    private Text welcomeText;

    @FXML
    private HBox chatInputs;
    @FXML
    private Button infoBtn;

    @FXML
    private Button encryptionOptionsOpen;

    @FXML
    private Button clearLogBtn;

    @FXML
    private SplitPane splitPane;

    public ChatController(){

        ObjectIOSingleton.getInstance().addObserver(this);
        actionManager = new ActionManager();
        JSONObject json = new JSONObject();
        json.put("action",Actions.ACTION_REQUEST_CHAT_STATUS);
        ObjectIOSingleton.getInstance().sendToServer(json);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InetAddress inetAddress = ClientData.getInstance().getServerAdress();

        String preText = status.getText();
        String text = inetAddress == null ? "not connected" : "connected to " + inetAddress;
        status.setText(preText + text);

        scrollPane.vvalueProperty().bind(chatBox.heightProperty());
        log.vvalueProperty().bind(logPane.heightProperty());

        textInput.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode() == KeyCode.ENTER){
                        sendMessage(textInput.getText());
                    }
                }
            });
        textInput.setPromptText("type your message");


        chatInputs.visibleProperty().bind(Bindings.isNotNull(ClientData.getInstance().getIdFromOpenChatProperty()));

        logoHBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
            ModalUtil.showCryptoChatInfo(this.getClass());
        });

        sendButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendMessage(textInput.getText()));
        encryptionOptionsOpen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            ModalUtil.showEncryptionOptions(this.getClass());
        });

        infoBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            ModalUtil.showChatInfo(this.getClass());
        });


        clearLogBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, evt->{
            Logger.getInstance().clearLog();
        });

        Logger.getInstance().resolveQueue();

    }


    private void sendMessage(String message){

        if(StringUtils.trimWhitespace(message).equals("")){
            return;
        }


        //if no chat is selected throw an exception --> show err message in popup

        String from = ClientData.getInstance().getId();
        String to =  ClientData.getInstance().getIdFromOpenChat();

        //encrypt message here

        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(to);
        String encrypted  = remoteClient.getSymEncryption().encrypt(message);
        String symMode = remoteClient.getSymEncryptionString();

        JSONObject json = JSONUtil.getMessageSendingJSON(encrypted, from, to, symMode);
        ObjectIOSingleton.getInstance().sendToServer(json);

        json.put("decryptedMessage", message);
        ClientData.getInstance().getRemoteClientById(to).addMessageToChatHistory(json);
        this.addMessageToCheckBox(json,Actions.ACTION_SENDING);



    }


    public void update(Observable o, Object arg) {
        JSONObject json = (JSONObject)arg;
        String action = (String) json.get("action");
        AtomicBoolean execute = new AtomicBoolean(false);

        Platform.runLater(()->{
            switch (action){
                case (Actions.ACTION_UPDATE_CHAT_VIEW):
                    actionManager.setActionResolver(new ChatViewAction());
                    execute.set(true);
                    break;
                case (Actions.ACTION_SEND_MESSAGE):
                    this.handleIncomingMessage(json);
                    break;
                case(Actions.ACTION_INIT_ENCRYPTED_CHAT_REQUEST):
                    this.decrypt(json);break;

            }
            if(execute.get())
                actionManager.resolve(json,this);
        });
    }



    @Override
    public Pane getPane() {
        return this.mainPane;
    }



    private void decrypt(JSONObject json)
    {
        String from  = (String) json.get("from");
        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(from);
        JSONObject encryptionParams = (JSONObject) json.get("encryptionParams");
        String asymMode = (String) encryptionParams.get("asymMode");
        String symMode = (String) encryptionParams.get("symMode");
        JSONObject actualEncryptedKeys = (JSONObject) encryptionParams.get("encryptionParams");
        String encryptedKey = (String)actualEncryptedKeys.get("encryptedKey");
        remoteClient.setAsymKeyReadable(encryptedKey);

        String decryptedKey = "";
        switch (asymMode){
            case(Actions.MODE_RSA):
                decryptedKey = ClientData.getInstance().getRSA().decrypt(encryptedKey);
                break;
            case(Actions.MODE_ELGAMAL):
                String publicKeyJ = (String) encryptionParams.get("public_key_j");
                ClientData.getInstance().getElGamal().addExternalKeys(publicKeyJ);
                decryptedKey = ClientData.getInstance().getElGamal().decrypt(encryptedKey);
                break;
        }

        switch (symMode) {
            case (Actions.MODE_AFFINE):
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject tAndKJson = (JSONObject) parser.parse(decryptedKey);
                    actualEncryptedKeys.put("t", tAndKJson.get("t"));
                    actualEncryptedKeys.put("k", tAndKJson.get("k"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case (Actions.MODE_VIGENERE):
                actualEncryptedKeys.put("key", decryptedKey);
                break;
            case (Actions.MODE_RC4):
                actualEncryptedKeys.put("key", decryptedKey);
                break;
            case (Actions.MODE_DES):
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject tAndKJson = (JSONObject) parser.parse(decryptedKey);
                    actualEncryptedKeys.put("keymap", tAndKJson.get("keymap"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case(Actions.MODE_HILL):
                JSONParser parser = new JSONParser();
                try {
                    JSONObject hillJson = (JSONObject) parser.parse(decryptedKey);
                    actualEncryptedKeys.put("key", JSONUtil.objectifyHillString(hillJson));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


        }

        String referrer =  (remoteClient.getRequestedEncryptionData() == null) ?  Actions.REFERRER_CREATE : Actions.REFERRER_UPDATE;

        remoteClient.setRequestedEncryptionData(encryptionParams);
        ClientData.getInstance().setIdFromLastRequest(from);

        ModalUtil.showInitChatRequest(this.getClass(),referrer);
        Platform.runLater(()->{
            Logger.getInstance().setReferrer(referrer);
            Logger.getInstance().log(Actions.LOG_INIT_CHAT, from);
        });


    }


    private void handleIncomingMessage(JSONObject json)
    {

        String openChatID = ClientData.getInstance().getIdFromOpenChat();
        String fromID = (String)json.get("fromID");
        String decrypted;
        String key;

        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(fromID);
        decrypted = remoteClient.getSymEncryption().decrypt((String) json.get("message"));
        key = remoteClient.getSymEncryption().getModeSpecificKey();

        json.put("decryptedMessage", decrypted);

        remoteClient.addMessageToChatHistory(json);



        Logger.getInstance().setLastDecryptedMessage(decrypted);
        Logger.getInstance().setLastEncryptedMessage((String) json.get("message"));
        Logger.getInstance().setLastSymMode((String) json.get("symMode"));
        Logger.getInstance().setLastKey(key);
        Logger.getInstance().log(Actions.LOG_REMOTE_MESSAGE, remoteClient.getName());


        int unreadMessages = ClientData.getInstance().getRemoteClientById(fromID).getUnreadMessages();
        HBox box = (HBox) ChatViewUtil.find(fromID);
        Text t = (Text) box.getChildren().get(BubbleController.COUNT_INDEX);
        if(openChatID == null || !openChatID.equals(fromID)){
            if(unreadMessages == 0 ){
                new Flash(box).play();
            }
            t.setVisible(true);
            t.setManaged(true);
            String unreadMessagesString = String.valueOf(++unreadMessages);
            t.setText(unreadMessagesString);
            ClientData.getInstance().getRemoteClientById(fromID).setUnreadMessages(unreadMessages);
        }
        else{
            this.addMessageToCheckBox(json, Actions.ACTION_RECEIVING);
        }


    }



    private void addMessageToCheckBox(JSONObject json, String sendingOrReceiving)
    {
        String message = (String) json.get("message");
        String from = (String) json.get("fromID");
        String to = (String) json.get("toID");

        String decrypted = "";
        String key = "";
        if(sendingOrReceiving.equals(Actions.ACTION_RECEIVING)){
            decrypted = ClientData.getInstance().getRemoteClientById(from).getSymEncryption().decrypt(message);
            key = ClientData.getInstance().getRemoteClientById(from).getSymEncryption().getModeSpecificKey();
        }
        else{
            decrypted = (String) json.get("decryptedMessage");
            key = ClientData.getInstance().getRemoteClientById(to).getSymEncryption().getModeSpecificKey();

        }

        Logger.getInstance().setLastDecryptedMessage(decrypted);
        Logger.getInstance().setLastEncryptedMessage(message);
        Logger.getInstance().setLastSymMode((String) json.get("symMode"));
        Logger.getInstance().setLastKey(key);
        if(sendingOrReceiving.equals(Actions.ACTION_SENDING)) {
            String name = ClientData.getInstance().getRemoteClientById(to).getName();
            Logger.getInstance().log(Actions.LOG_SELF_MESSAGE, name);
        }




        Text t = (Text)(ChatViewUtil.find("welcomeText"));

        if(t!= null){
            t.setManaged(false);
            t.setVisible(false);
            chatBox.getChildren().clear();
        }
        Text text=new Text(decrypted);
        text.getStyleClass().add("message");

        TextFlow tempFlow=new TextFlow();
        tempFlow.getChildren().add(text);
        TextFlow flow=new TextFlow(tempFlow);
        HBox hbox=new HBox(12);


        Tooltip tooltip = new Tooltip();
        tooltip.setText("Encrypted -> " + message);


        if(sendingOrReceiving.equals(Actions.ACTION_SENDING)){
            tempFlow.getStyleClass().add("tempFlow");
            flow.getStyleClass().add("textFlow");
            hbox.setAlignment(Pos.BOTTOM_RIGHT);
            hbox.getChildren().add(flow);
            chatBox.setAlignment(Pos.TOP_RIGHT);
            textInput.setText("");
            textInput.requestFocus();
        }
        else if(sendingOrReceiving.equals(Actions.ACTION_RECEIVING)){
            tempFlow.getStyleClass().add("tempFlowFlipped");
            flow.getStyleClass().add("textFlowFlipped");
            chatBox.setAlignment(Pos.TOP_LEFT);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().add(flow);
        }

        tempFlow.maxWidthProperty().bind(scrollPane.widthProperty().divide(2));
        flow.maxWidthProperty().bind(scrollPane.widthProperty().divide(2));

        hbox.getStyleClass().add("hbox");

        Tooltip.install(tempFlow,tooltip);


        Platform.runLater(()->{
            chatBox.getChildren().addAll(hbox);

        });


    }
}
