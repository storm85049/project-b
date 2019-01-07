package controller;

import animatefx.animation.Flash;
import animatefx.animation.SlideInLeft;
import client.ClientData;
import client.ClientMain;
import client.ObjectIOSingleton;
import com.sun.deploy.util.StringUtils;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import oop.client.ActionManager;
import oop.client.ChatViewAction;
import oop.client.LoginAction;
import org.json.simple.JSONObject;
import sun.misc.IOUtils;
import util.Actions;
import util.ChatViewUtil;
import util.JSONUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.security.Key;
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
    ListView chat;

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
        JSONObject json = JSONUtil.getMessageSendingJSON(message, from, to);
        ObjectIOSingleton.getInstance().sendToServer(json);
        ClientData.getInstance().getAvailableChatById(to).addMessageToChatHistory(json);
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


    private void handleIncomingMessage(JSONObject json)
    {
        String openChatID = ClientData.getInstance().getIdFromOpenChat();
        String fromID = (String)json.get("fromID");

        ClientData.getInstance().getAvailableChatById(fromID).addMessageToChatHistory(json);

        int unreadMessages = ClientData.getInstance().getAvailableChatById(fromID).getUnreadMessages();
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
            ClientData.getInstance().getAvailableChatById(fromID).setUnreadMessages(unreadMessages);
        }
        else{
            this.addMessageToCheckBox(json, Actions.ACTION_RECEIVING);
        }


    }



    private void addMessageToCheckBox(JSONObject json, String sendingOrReceiving)
    {

        String message = (String) json.get("message");

        Text t = (Text)(ChatViewUtil.find("welcomeText"));
        if(t!= null){
            t.setManaged(false);
            t.setVisible(false);
            chatBox.getChildren().clear();
        }
        Text text=new Text(message);
        text.getStyleClass().add("message");

        TextFlow tempFlow=new TextFlow();
        tempFlow.getChildren().add(text);
        TextFlow flow=new TextFlow(tempFlow);
        HBox hbox=new HBox(12);

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

        Platform.runLater(() -> chatBox.getChildren().addAll(hbox));


    }






}
