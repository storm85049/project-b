package oop.client;

import client.ClientData;
import client.ObjectIOSingleton;
import controller.MainViewController;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;
import util.Actions;
import util.ModalUtil;
import java.util.HashMap;

public class LoginAction implements ActionResolver {
    @Override
    public void resolve(JSONObject jsonObject, MainViewController mainViewController) {

        String status = jsonObject.get("status").toString();

        switch (status){
            case Actions.ACTION_LOGIN_GRANTED:
                initSuccesfullUserLogin(jsonObject,mainViewController);
                break;
            case Actions.ACTION_LOGIN_FAILED:
                ModalUtil.modal(jsonObject);

                ImageView spinner = (ImageView) mainViewController.mainAnchorPane.lookup("#spinnerImage");
                TextField textField = (TextField) mainViewController.mainAnchorPane.lookup("#inputField");
                spinner.setVisible(false);
                textField.setText("");

                break;
        }

    }


    private void initSuccesfullUserLogin(JSONObject jsonObject, MainViewController mainViewController) {

        ClientData.getInstance().setName((String)jsonObject.get("name"));
        ClientData.getInstance().setId((String)jsonObject.get("id"));
        ClientData.getInstance().setServerAdress(ObjectIOSingleton.getInstance().socket.getInetAddress());


        HashMap<String,String> activeUsers = (HashMap<String, String>) jsonObject.get("activeUsers");
        mainViewController.loadUI(MainViewController.CHAT_VIEW);

    }

}
