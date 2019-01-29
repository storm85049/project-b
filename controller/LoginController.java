package controller;

import Krypto.ElGamal;
import Krypto.RSA;
import client.ClientData;
import client.ObjectIOSingleton;
import com.sun.deploy.util.StringUtils;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginController  implements Initializable, Observer, IController {

    @FXML
    private TextField inputField;
    @FXML
    private ImageView spinnerImage;
    @FXML
    private VBox mainPane;

    private ActionManager actionManager = new ActionManager();

    public void sendLoginRequest(){

        spinnerImage.setVisible(true);
        String name = StringUtils.trimWhitespace(inputField.getText());

        RSA rsa = new RSA();
        ClientData.getInstance().setRsa(rsa);
        ElGamal elGamal = new ElGamal();
        ClientData.getInstance().setElGamal(elGamal);

        JSONObject json = JSONUtil.getLoginRequestJSON(name, rsa.getPublicKeyMap(), elGamal.getPublicKeyMap());
        ObjectIOSingleton io = ObjectIOSingleton.getInstance();
        io.init();
        io.sendToServer(json);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObjectIOSingleton.getInstance().addObserver(this);
        inputField.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode() == KeyCode.ENTER){
                        sendLoginRequest();
                    }
                }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        JSONObject json = (JSONObject)arg;
        String action = (String) json.get("action");
        AtomicBoolean execute = new AtomicBoolean(false);
            Platform.runLater(()->{
                switch (action){
                    case (Actions.ACTION_LOGIN_RESPONSE):
                        actionManager.setActionResolver(new LoginAction());
                        execute.set(true);
                        break;
                }


                if(execute.get())
                    actionManager.resolve(json,this);
            });

    }

    @Override
    public Pane getPane()
    {
        return this.mainPane;
    }
}
