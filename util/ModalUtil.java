package util;

import Krypto.ElGamal;
import Krypto.IAsymmetricEncryption;
import Krypto.RSA;
import controller.MainViewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.transform.MatrixType;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;

public class ModalUtil{

    private static ModalUtil instance;

    public ModalUtil() {
    }

    public static ModalUtil getInstance() {
        if(ModalUtil.instance == null){
            ModalUtil.instance = new ModalUtil();
        }
        return ModalUtil.instance;
    }


    public static void showLoginError(){

        //nur ein beispiel

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("den namen gibt es schon");
        alert.setContentText("......");

        alert.showAndWait();

    }

    public static void showEncryptionOptions(Class callingClass) {
        try {
            Parent root = FXMLLoader.load(callingClass.getClassLoader().getResource(MainViewController.ENCRYPTION_DIALOG_MAIN));
            Stage secondStage = new Stage();
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            secondStage.setTitle("Test");
            secondStage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}