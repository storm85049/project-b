package controller.helper;

import Krypto.DES;
import client.ClientData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DESFactory implements Initializable {




    @FXML
    private Label desKey;

    private DES des;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        des = new DES();
    }


    public void generateDes()
    {
        des.generateNew();
        desKey.setText("DES Key : " + des.getKey().toString());
        ClientData.getInstance().setInternalChosenDESKeyMap(des.getKeyMap());
    }
}
