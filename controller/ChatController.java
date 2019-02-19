package controller;

import animatefx.animation.Flash;
import client.ClientData;
import client.ObjectIOSingleton;
import client.RemoteClient;
import com.sun.deploy.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import oop.client.ActionManager;
import oop.client.ChatViewAction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    private EncryptionController encryptionController = new EncryptionController();

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
    VBox chatBox;

    private ActionManager actionManager;

    @FXML
    Button sendButton;

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

        textInput.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode() == KeyCode.ENTER){
                        sendMessage(textInput.getText());
                    }
                }
            });

        sendButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sendMessage(textInput.getText());
            }
        });
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
        String encrypted = remoteClient.getSymEncryption().encrypt(message);

        JSONObject json = JSONUtil.getMessageSendingJSON(encrypted, from, to);
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


    //todo: ist der chat noch online ? bzw nachricht vom server senden, dass der chat offline gegangen ist und das fenster geschlossen wird, sonst kommt es zu einem bug

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
        String decryptedKey = "";
        switch (asymMode){
            case(Actions.MODE_RSA):
                decryptedKey = ClientData.getInstance().getRSA().decrypt(encryptedKey);
                break;
            case(Actions.MODE_ELGAMAL):
                //todo:die crashen noch weil es den key nicht gibt.
                //ClientData.getInstance().getElGamal().addExternalKeys(remoteClient.getPublicElGamalKeyMap().get("public_key_j"));
                //decryptedKey = ClientData.getInstance().getElGamal().decrypt(encryptedKey);

                break;
        }

        switch (symMode)
        {
            case(Actions.MODE_AFFINE):
                try{
                    JSONParser parser = new JSONParser();
                    JSONObject tAndKJson = (JSONObject) parser.parse(decryptedKey);
                    actualEncryptedKeys.put("t",tAndKJson.get("t"));
                    actualEncryptedKeys.put("k",tAndKJson.get("k"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case(Actions.MODE_VIGENERE):
                actualEncryptedKeys.put("key", decryptedKey);
                break;

        }

        remoteClient.setRequestedEncryptionData(encryptionParams);
        ClientData.getInstance().setIdFromLastRequest(from);



        ModalUtil.showInitChatRequest(this.getClass());


    }


  /*  public String decryptAccordingToMode(String s, String asymMode)
    {
        switch (asymMode){
            case(RSA.class.getName()):
                return ClientData.getInstance().getRSA().decrypt(s);
            case (ElGamal.class.getName()):

        }



    }*/

    private void handleIncomingMessage(JSONObject json)
    {
        //json = encryptionController.decryptMessage(json);
        String openChatID = ClientData.getInstance().getIdFromOpenChat();
        String fromID = (String)json.get("fromID");
        String decrypted = ClientData.getInstance().getRemoteClientById(fromID).getSymEncryption().decrypt((String) json.get("message"));
        json.put("decryptedMessage", decrypted);

        ClientData.getInstance().getRemoteClientById(fromID).addMessageToChatHistory(json);

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

        String decrypted = "";
        if(sendingOrReceiving.equals(Actions.ACTION_RECEIVING)){
            decrypted = ClientData.getInstance().getRemoteClientById(from).getSymEncryption().decrypt(message);
        }
        else{
            decrypted = (String) json.get("decryptedMessage");
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
        Tooltip.install(hbox,tooltip);

        Platform.runLater(() -> chatBox.getChildren().addAll(hbox));


    }
}
