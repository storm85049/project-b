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

    @Override
    public void initialize(URL location, ResourceBundle resources) {



    }


    public void generateDes()
    {

        DES des = ClientData.getInstance().getInternalDES();
        des.generateNew();
        desKey.setText("DES Key : " + des.getKey().toString());

    }
}
