package oop.client;

import controller.MainViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import util.ChatViewUtil;

import java.util.HashMap;

public class ChatViewAction implements ActionResolver{



    @Override
    public void resolve(JSONObject jsonObject, MainViewController mainViewController) {

        HashMap<String,String> activeUsers = (HashMap<String, String>) jsonObject.get("activeUsers");
        VBox listBox = (VBox)mainViewController.mainAnchorPane.lookup("#chatlist");
        listBox = ChatViewUtil.updateChatView(activeUsers,listBox,mainViewController);
    }
}
