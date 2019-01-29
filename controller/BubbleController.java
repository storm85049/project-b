package controller;

import client.ClientData;
import client.RemoteClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.json.simple.JSONObject;
import util.ChatViewUtil;
import util.ModalUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BubbleController implements Initializable{



public HBox mainPane;
    public static int COUNT_INDEX = 2;
    public static int NAME_INDEX = 1;



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        mainPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleButtonPress(mainPane);
            }
        });
    }


    private void handleButtonPress(HBox box)
    {

        Text name = (Text)box.getChildren().get(NAME_INDEX);
        String requestedID = box.getId();
        String activeID = ClientData.getInstance().getIdFromOpenChat();
        //if chat is open and a click is made on the open chat, ignore the request
        if(activeID!= null) {
            if (activeID.equals(requestedID)) {
                return;
            }
            ChatViewUtil.find(activeID).getStyleClass().remove("active");
        }
        box.getStyleClass().add("active");
        //check if a mode is selected ... wait for max implementation


        this.initChat(box);
        //change out keys and shit



    }


    private void initChat(HBox box)
    {

        String requestedID = box.getId();

        String otherClientsName = ClientData.getInstance().getAvailableChatById(requestedID).getName();
        ClientData.getInstance().getAvailableChatById(requestedID).setUnreadMessages(0);
        Text messageCount = (Text) box.getChildren().get(COUNT_INDEX);
        messageCount.setVisible(false);
        messageCount.setManaged(false);
        ClientData.getInstance().setIdFromOpenChat(requestedID);

        if (ClientData.getInstance().getAvailableChatById(ClientData.getInstance().getIdFromOpenChat()).getChatHistory() == null) {
            ModalUtil.showEncryptionOptions(this.getClass());
        }

        //clear whole view

        //no chat available yet todo: set and send public/private key

        RemoteClient remoteClient = ClientData.getInstance().getAvailableChatById(requestedID);
        Text topName = (Text) ChatViewUtil.find("topName");
        topName.setText(otherClientsName);

        VBox chatBox = (VBox)ChatViewUtil.find("chatBox");
        chatBox.getChildren().clear();

        if(remoteClient.getChatHistory() == null){
            Text welcomeText = new Text("Type to start chatting with " + otherClientsName);
            welcomeText.setId("welcomeText");
            welcomeText.setFont(Font.font("Agency FB",20));

            chatBox.setAlignment(Pos.CENTER);
            chatBox.getChildren().addAll(welcomeText);


        }
        else{
            //load chat into view
            ArrayList<JSONObject> chatHistory = remoteClient.getChatHistory();
            applyChatHistoryToWindow(chatHistory);



        }
    }

    private static void applyChatHistoryToWindow(ArrayList<JSONObject> chatHistory)
    {

        for(JSONObject json : chatHistory){
            JSONObject jsonObject = (JSONObject)json.get("message");
            String message = (String)jsonObject.get("kryptoMessage");
            String fromID  = (String)jsonObject.get("fromID");
            String toID = (String)jsonObject.get("toID");

            Text text=new Text(message);
            text.getStyleClass().add("message");
            TextFlow tempFlow=new TextFlow();
            tempFlow.getChildren().add(text);
            TextFlow flow=new TextFlow(tempFlow);
            HBox hbox=new HBox(12);

            VBox chatBox = (VBox)ChatViewUtil.find("chatBox");
            ScrollPane scrollPane = (ScrollPane)ChatViewUtil.find("scrollPane");

            if(ClientData.getInstance().getId().equals(fromID)){
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
        }
    }

}

