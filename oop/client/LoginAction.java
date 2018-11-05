package oop.client;

import client.ClientData;
import client.ObjectIOSingleton;
import controller.IController;
import controller.MainViewController;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;
import util.Actions;
import util.ModalUtil;
import java.util.HashMap;

public class LoginAction implements ActionResolver {
    @Override
    public  void resolve(JSONObject jsonObject, IController controller) {

        String status = jsonObject.get("status").toString();

        switch (status){
            case Actions.ACTION_LOGIN_GRANTED:
                initSuccesfullUserLogin(jsonObject);
                break;
            case Actions.ACTION_LOGIN_FAILED:

                ImageView spinner = (ImageView) controller.getPane().lookup("#spinnerImage");
                TextField textField = (TextField) controller.getPane().lookup("#inputField");
                spinner.setVisible(false);
                textField.setText("");

                break;
        }

    }


    private void initSuccesfullUserLogin(JSONObject jsonObject ) {

        ClientData.getInstance().setName((String)jsonObject.get("name"));
        ClientData.getInstance().setId((String)jsonObject.get("id"));
        ClientData.getInstance().setServerAdress(ObjectIOSingleton.getInstance().socket.getInetAddress());

        MainViewController.getInstance().loadUI(MainViewController.CHAT_VIEW);

    }

}
