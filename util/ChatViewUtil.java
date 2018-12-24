package util;

import client.ClientData;
import client.RemoteClient;
import controller.ChatController;
import controller.IController;
import controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatViewUtil {



    public static Node find(String selector)
    {
        String searchfor = selector;
        if(!searchfor.startsWith("#")){
            searchfor = "#" + searchfor;
        }
        return MainViewController.getInstance().getPane().lookup(searchfor);
    }






    public static VBox updateChatView(HashMap<String,String> users, VBox list, IController controller) {
        VBox tmp = list;
        tmp.getChildren().clear();

        for (Map.Entry<String, String> entry : users.entrySet()) {

            String name = entry.getKey();
            String id = entry.getValue();
            if (id.equals(ClientData.getInstance().getId())) continue;
            HBox buttonBox = (HBox) MainViewController.loadComponent(controller.getClass(),MainViewController.CHATBUBBLE).getChildrenUnmodifiable().get(0);
            Button button = (Button)buttonBox.getChildren().get(1);
            button.setText(name);
            button.setId(id);
            String possibleOpenChat = ClientData.getInstance().getIdFromOpenChat();
            if(possibleOpenChat != null && possibleOpenChat.equals(id)){
                button.getStyleClass().addAll("active");
            }
            tmp.getChildren().add(button);


            RemoteClient remoteClient = new RemoteClient(id,name);
            ClientData.getInstance().addToAvailableChats(remoteClient);

        }
        if(tmp.getChildren().size() <= 0){
            Text t = new Text("no one is online");
            t.setManaged(true);
            t.setFont(new Font("Agency FB",20));
            t.setFill(Color.GRAY);
            tmp.setMargin(t,new Insets(100,0,0,0));
            tmp.getChildren().add(t);
        }
        return tmp;
    }




}
