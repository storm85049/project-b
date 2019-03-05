package controller;

import client.ClientData;
import client.RemoteClient;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.json.simple.JSONObject;
import util.Actions;
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


        mainPane.setOnMouseClicked(event -> handleButtonPress(mainPane));
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

        this.initChat(box);




    }


    private void initChat(HBox box)
    {

        String requestedID = box.getId();
        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(requestedID);

        String otherClientsName = remoteClient.getName();
        remoteClient.setUnreadMessages(0);
        Text messageCount = (Text) box.getChildren().get(COUNT_INDEX);
        messageCount.setVisible(false);
        messageCount.setManaged(false);
        ClientData.getInstance().setIdFromOpenChat(requestedID);
        ClientData.getInstance().setIdFromLastRequest(requestedID);

        if (!remoteClient.isEncryptionSet()) {
            ModalUtil.showEncryptionOptions(this.getClass());
        }

        //clear whole view



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
            String encrypted= (String)json.get("message");
            String decrypted = (String)json.get("decryptedMessage");
            String fromID  = (String)json.get("fromID");
            String toID = (String)json.get("toID");

            Text text=new Text(decrypted);
            text.getStyleClass().add("message");
            TextFlow tempFlow=new TextFlow();
            tempFlow.getChildren().add(text);
            TextFlow flow=new TextFlow(tempFlow);
            HBox hbox=new HBox(12);
            Tooltip tooltip = new Tooltip("encrypted -> " + encrypted);

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
            Tooltip.install(hbox,tooltip);
            Platform.runLater(() -> chatBox.getChildren().addAll(hbox));
        }
    }

}

