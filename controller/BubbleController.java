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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.json.simple.JSONObject;
import util.ChatViewUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BubbleController implements Initializable, IController{


    public Pane mainPane;
    public Button button;


    @Override
    public Pane getPane() {
        return this.mainPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

       button.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent e) {
               handleButtonPress(button);
           }
       });
    }


    private void handleButtonPress(Button button)
    {
        String requestedID = button.getId();
        String activeID = ClientData.getInstance().getIdFromOpenChat();
        //if chat is open and a click is made on the open chat, ignore the request
        if(activeID!= null) {
            if (activeID.equals(requestedID)) {
                return;
            }
            ChatViewUtil.find(activeID).getStyleClass().remove("active");
        }
        button.getStyleClass().add("active");
        //check if a mode is selected ... wait for max implementation


        this.initChat(button);
        //change out keys and shit



    }


    private void initChat(Button button)
    {
        String requestedID = button.getId();

        /**
         * die nächsten 3 zeilen sind ein bisschen rumgehacke, bis ich die messageCount bubble schön hinbekomme
         */
        ClientData.getInstance().getAvailableChatById(requestedID).setUnreadMessages(0);
        String otherClientsName = ClientData.getInstance().getAvailableChatById(requestedID).getName();
        button.setText(otherClientsName);
        ClientData.getInstance().setIdFromOpenChat(requestedID);


        //clear whole view

        //no chat available yet todo: set and send public/private key

        RemoteClient remoteClient = ClientData.getInstance().getAvailableChatById(requestedID);


        VBox chatBox = (VBox)ChatViewUtil.find("chatBox");
        chatBox.getChildren().clear();

        if(remoteClient.getChatHistory() == null){
            Text welcomeText = new Text("Type to start chatting with " + otherClientsName);
            welcomeText.setId("welcomeText");
            welcomeText.setFont(Font.font("Agency FB",20));
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(welcomeText);
            chatBox.getChildren().addAll(vbox);
            Text topName = (Text) ChatViewUtil.find("topName");
            topName.setText(otherClientsName);
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

            String message = (String)json.get("message");
            String fromID  = (String)json.get("fromID");
            String toID = (String)json.get("toID");

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

