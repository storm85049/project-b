package controller;

import animatefx.animation.Flash;
import animatefx.animation.SlideInLeft;
import client.ClientData;
import client.ClientMain;
import client.ObjectIOSingleton;
import javafx.application.Platform;
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
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

    ActionManager actionManager;

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
                        sendRequest();
                    }
                }
            });

    }
    public void zoomIn(){
        new Flash(logoHBox).play();
    }


    int counter = 0;

    public void sendRequest(){
    counter ++;
        Text text=new Text(textInput.getText());
        text.getStyleClass().add("message");

        TextFlow tempFlow=new TextFlow();
        tempFlow.getChildren().add(text);
        TextFlow flow=new TextFlow(tempFlow);
        HBox hbox=new HBox(12);

        if(counter % 2== 0  ){
            tempFlow.getStyleClass().add("tempFlow");
            flow.getStyleClass().add("textFlow");
            hbox.setAlignment(Pos.BOTTOM_RIGHT);
            hbox.getChildren().add(flow);
        }
        else{
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

        textInput.setText("");
        textInput.requestFocus();
        System.out.println("send message request");

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
            }
            if(execute.get())
                actionManager.resolve(json,this);
        });

    }


    @Override
    public Pane getPane() {
        return this.mainPane;
    }
}
