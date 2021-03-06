package controller;

import controller.logger.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainViewController  implements Initializable, IController{

    public static  String BASEPATH = "gui/";
    public static  String ENCRYPTION_DIALOG = BASEPATH + "encryption_dialog/";
    public static  String LOGIN_VIEW = BASEPATH + "login.fxml";
    public static  String CHAT_VIEW = BASEPATH + "chatviewv2.fxml";
    public static  String CHATBUBBLE = BASEPATH + "chat_left_new.fxml";
    public static  String ENCRYPTION_DIALOG_MAIN = ENCRYPTION_DIALOG + "main_encryption_template.fxml";
    public static  String ENCRYPTION_DIALOG_INFO_REQUEST = ENCRYPTION_DIALOG + "init_chat_request_popup.fxml";
    public static  String CHAT_INFO = ENCRYPTION_DIALOG + "information.fxml";
    public static  String CRYPTO_CHAT_INFO = BASEPATH + "cryptochatinfo.fxml";
    public static  String ENCRYPTION_WIKI= BASEPATH + "modedescriptions.fxml";


    public static  String ENCRYPTION_DIALOG_AFFIN = ENCRYPTION_DIALOG + "affine.fxml";
    public static  String ENCRYPTION_DIALOG_VIGENERE = ENCRYPTION_DIALOG + "vigenere.fxml";
    public static  String ENCRYPTION_DIALOG_RC4 = ENCRYPTION_DIALOG + "rc4.fxml";
    public static  String ENCRYPTION_DIALOG_DES = ENCRYPTION_DIALOG + "des.fxml";
    public static  String ENCRYPTION_DIALOG_HILL = ENCRYPTION_DIALOG + "hill.fxml";
    public static  String ENCRYPTION_HILL_2x2 = BASEPATH + "2x2Hill.fxml";
    public static  String ENCRYPTION_HILL_3x3 = BASEPATH+ "3x3Hill.fxml";

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

        if(ui.equals(CHAT_VIEW)){
            Platform.runLater(()->
            {
                mainAnchorPane.getScene().getWindow().setWidth(1200);
                mainAnchorPane.getScene().getWindow().setHeight(600);
            });
        }

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
