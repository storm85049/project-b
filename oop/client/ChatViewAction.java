package oop.client;

import controller.IController;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import util.ChatViewUtil;

import java.util.HashMap;

public class ChatViewAction implements ActionResolver{



    @Override
    public void resolve(JSONObject jsonObject, IController controller) {

        VBox listBox = (VBox)controller.getPane().lookup("#chatlist");
        listBox = ChatViewUtil.updateChatView(jsonObject,listBox,controller);

    }
}
