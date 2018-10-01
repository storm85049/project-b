package controller;

import client.ObjectIOSingleton;
import com.sun.deploy.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.json.simple.JSONObject;
import util.JSONUtil;

public class LoginController  {

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

    public void setupKeyListener(){
        inputField.getScene().setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                sendLoginRequest();
            }
        });
    }

}
