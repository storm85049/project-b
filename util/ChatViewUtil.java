package util;

import client.ClientData;
import controller.IController;
import controller.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.Map;

public class ChatViewUtil {


    public static VBox updateChatView(HashMap<String,String> users, VBox list, IController controller) {
        VBox tmp = list;
        tmp.getChildren().clear();

        for (Map.Entry<String, String> entry : users.entrySet()) {

            String name = entry.getKey();
            String id = entry.getValue();

            if (id.equals(ClientData.getInstance().getId())) continue;
            Button button = (Button) MainViewController.loadComponent(controller.getClass(),MainViewController.CHATBUBBLE);
            button.setText(name);
            button.setId(id);
            tmp.getChildren().add(button);

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
