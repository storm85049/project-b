package controller;

import client.ObjectIOSingleton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import oop.client.ActionManager;
import oop.client.ChatViewAction;
import oop.client.LoginAction;
import org.json.simple.JSONObject;
import util.Actions;

import javax.sound.midi.ControllerEventListener;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MainViewController  implements Initializable, IController{

    public static  String BASEPATH = "gui/";
    public static  String LOGIN_VIEW = BASEPATH + "login.fxml";
    public static  String CHAT_VIEW = BASEPATH + "chatview.fxml";
    public static  String CHATBUBBLE = BASEPATH + "chatbubble.fxml";
    public static  String BUBBLE = BASEPATH + "bubble.fxml";
    public static  String TEST = BASEPATH + "test.fxml";


    @FXML
    public AnchorPane mainAnchorPane;

    public static MainViewController instance;
    public static MainViewController getInstance(){
        if(MainViewController.instance == null){
            MainViewController.instance = new MainViewController();
        }
        return MainViewController.instance;
    }

    public MainViewController(){
        if(MainViewController.instance == null){
            MainViewController.instance = this;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public static Parent loadComponent(Class calledClass,String path) {
        Parent node = null;
        try {
            node = FXMLLoader.load(calledClass.getClassLoader().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node ;
    }


    @Override
    public Pane getPane() {
        return this.mainAnchorPane;
    }
}
