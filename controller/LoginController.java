package controller;

import client.ObjectIOSingleton;
import com.sun.deploy.util.StringUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.simple.JSONObject;
import util.JSONUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField inputField;
    @FXML
    private ImageView spinnerImage;

    public void sendLoginRequest(){

        spinnerImage.setVisible(true);
        String name = StringUtils.trimWhitespace(inputField.getText());
        JSONObject json = JSONUtil.getLoginRequestJSON(name);
        ObjectIOSingleton io = ObjectIOSingleton.getInstance();
        io.init();
        io.sendToServer(json);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputField.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    sendLoginRequest();
                }
            }
        });
    }
}
