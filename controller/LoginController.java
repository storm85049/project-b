package controller;

import Krypto.DES;
import Krypto.ElGamal;
import Krypto.RSA;
import client.ClientData;
import client.ObjectIOSingleton;
import com.sun.deploy.util.StringUtils;
import com.sun.security.ntlm.Client;
import controller.logger.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import oop.client.ActionManager;
import oop.client.LoginAction;
import org.json.simple.JSONObject;
import util.Actions;
import util.JSONUtil;
import util.ModalUtil;

import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginController  implements Initializable, Observer, IController {

    @FXML
    private TextField inputField;
    @FXML
    private ImageView spinnerImage;
    @FXML
    private VBox mainPane;
    @FXML
    private Label keyStatus;
    @FXML
    private Label privateKey;
    @FXML
    private Button loginBtn;


    private int maxVisibleLengthOfKeys = 25;
    private ActionManager actionManager = new ActionManager();

    public void initLogin() {

        spinnerImage.setVisible(true);
        loginBtn.setDisable(true);
        inputField.setDisable(true);

        String name = StringUtils.trimWhitespace(inputField.getText());

        Task<Void> initAsymm = new Task<Void>() {
            @Override
            protected Void call() throws Exception {



                boolean testUmgebung = false;

                JSONObject json = new JSONObject();
                updateMessage("Generating EL Gamal Key");
                if (!testUmgebung) {
                    ElGamal elGamal = new ElGamal();
                    ClientData.getInstance().setElGamal(elGamal);
                    String privateEG = elGamal.getPrivateKey().substring(0, maxVisibleLengthOfKeys) + "...";
                    String publicEG = elGamal.getPublicKey().substring(0, maxVisibleLengthOfKeys) + "...";
                    updateMessage("EL Gamal Private Key: " + privateEG);
                    updateTitle("EL Gamal Public Key: " + publicEG);
                    updateMessage("Generating RSA Key");
                    updateTitle("");
                    RSA rsa = new RSA();
                    ClientData.getInstance().setRSA(rsa);
                    String publicrsa = rsa.getPublicKey().substring(0, maxVisibleLengthOfKeys) + "...";
                    String privatersa = rsa.getPrivateKey().substring(0, maxVisibleLengthOfKeys) + "...";

                    updateMessage("RSA Private Key: " + privatersa);
                    updateTitle("RSA Public Key: " + publicrsa);
                    //Thread.sleep(1000);
                    updateMessage("Launching App...");
                    updateTitle("");
                    json = JSONUtil.getLoginRequestJSON(name, rsa.getPublicKeyMap(), elGamal.getPublicKeyMap());

                }
                //Thread.sleep(1000);


                if (testUmgebung) {

                    Map<String, String> m2 = new HashMap<>();
                    Map<String, String> m1 = new HashMap<>();
                    json = JSONUtil.getLoginRequestJSON(name, m1, m2);
                }

                ObjectIOSingleton io = ObjectIOSingleton.getInstance();
                io.init();
                io.sendToServer(json);

                return null;
            }

        };
        keyStatus.textProperty().bind(initAsymm.messageProperty());
        privateKey.textProperty().bind(initAsymm.titleProperty());

        new Thread(initAsymm).start();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObjectIOSingleton.getInstance().addObserver(this);
        inputField.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER && !inputField.getText().isEmpty()) {
                    sendLoginRequestName();
                }
            }
        });
    }


    public void sendLoginRequestName() {

        spinnerImage.setVisible(true);
        String name = StringUtils.trimWhitespace(inputField.getText());
        JSONObject json = JSONUtil.getLoginRequestNameJSON(name);
        ObjectIOSingleton.getInstance().init();
        ObjectIOSingleton.getInstance().sendToServer(json);
    }


    @Override
    public void update(Observable o, Object arg) {
        JSONObject json = (JSONObject) arg;
        String action = (String) json.get("action");
        AtomicBoolean execute = new AtomicBoolean(false);
        Platform.runLater(() -> {
            switch (action) {
                case (Actions.ACTION_LOGIN_FAILED):
                    this.showLoginFailed();
                    break;
                case (Actions.ACTION_LOGIN_GRANTED):
                    initLogin();
                    break;
                case (Actions.ACTION_INIT_LOGIN_RESPONSE):
                    actionManager.setActionResolver(new LoginAction());
                    execute.set(true);
                    break;
            }
            if (execute.get())
                actionManager.resolve(json, this);
        });

    }


    private void showLoginFailed() {
        ModalUtil.showLoginError();
        spinnerImage.setVisible(false);
        inputField.setText("");
        loginBtn.setDisable(false);
        inputField.setDisable(false);
    }


    @Override
    public Pane getPane() {
        return this.mainPane;
    }
}


