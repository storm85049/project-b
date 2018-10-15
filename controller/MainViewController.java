package controller;

import client.ObjectIOSingleton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import oop.client.ActionManager;
import oop.client.ChatViewAction;
import oop.client.LoginAction;
import org.json.simple.JSONObject;
import util.Actions;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MainViewController implements Initializable,Observer{

    public static  String BASEPATH = "gui/";
    public static  String LOGIN_VIEW = BASEPATH + "login.fxml";
    public static  String CHAT_VIEW = BASEPATH + "chatview.fxml";
    public static  String CHATBUBBLE = BASEPATH + "chatbubble.fxml";

    ActionManager actionManager = new ActionManager();

    @FXML
    public AnchorPane mainAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObjectIOSingleton.getInstance().addObserver(this);
        loadUI(LOGIN_VIEW);
    }

    public void loadUI(String ui){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(ui));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane.setBottomAnchor(root,0.0);
        AnchorPane.setTopAnchor(root,0.0);
        AnchorPane.setLeftAnchor(root,0.0);
        AnchorPane.setRightAnchor(root,0.0);
        mainAnchorPane.getChildren().clear();
        mainAnchorPane.getChildren().add(root);
    }

    public Parent loadComponent(String path) {
        Parent node = null;
        try {
            node = FXMLLoader.load(getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node ;
    }


    @Override
    public void update(Observable o, Object arg) {
        JSONObject json = (JSONObject)arg;
        String action = (String) json.get("action");

        Platform.runLater(()->{
            switch (action){
                case (Actions.ACTION_LOGIN_RESPONSE):
                    actionManager.setActionResolver(new LoginAction());
                    break;
                case(Actions.ACTION_UPDATE_CHAT_VIEW):
                    actionManager.setActionResolver(new ChatViewAction());
                    break;

            }
            actionManager.resolve(json,this);
        });

    }

}
