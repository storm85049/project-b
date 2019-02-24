package util;

import Krypto.ElGamal;
import Krypto.IAsymmetricEncryption;
import Krypto.RSA;
import client.ClientData;
import client.RemoteClient;
import controller.ChatInfoController;
import controller.EncryptionController;
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
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.transform.MatrixType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class ModalUtil{



    public static void showLoginError(){

        //nur ein beispiel

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("den namen gibt es schon");
        alert.setContentText("......");

        alert.showAndWait();

    }



    public static void showEncryptionOptions(Class callingClass)
    {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(callingClass.getClassLoader().getResource(MainViewController.ENCRYPTION_DIALOG_MAIN)));
            Stage secondStage = new Stage();
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            secondStage.setTitle("Please choose your encryption mode");




            secondStage.setOnCloseRequest( event -> {
                String requestedChatID = ClientData.getInstance().getIdFromLastRequest();
                RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(requestedChatID);

                if(!remoteClient.isEncryptionSet()){
                    event.consume();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Stop!");
                    alert.setContentText("you have to choose a mode before being able to text " + remoteClient.getName());
                    alert.showAndWait();

                }
            });


            secondStage.initModality(Modality.APPLICATION_MODAL);
            secondStage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
        }

    }



    public static void showInitChatRequest(Class callingClass, String referrer) {
        try {

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(callingClass.getClassLoader().getResource(MainViewController.ENCRYPTION_DIALOG_INFO_REQUEST)));
            Parent root = loader.load();

            ChatInfoController ctrl = loader.getController();
            ctrl.setReferer(referrer);


            Stage secondStage = new Stage();
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            secondStage.setTitle("New chat request");


            secondStage.initModality(Modality.APPLICATION_MODAL);
            secondStage.showAndWait();


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void showCryptoChatInfo(Class callingClass)
    {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(callingClass.getClassLoader().getResource(MainViewController.CRYPTO_CHAT_INFO)));
            Stage secondStage = new Stage();
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            secondStage.setTitle("Crypto Chat");


            secondStage.initModality(Modality.APPLICATION_MODAL);
            secondStage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
        }

    }






    public static void showChatInfo(Class callingClass)
    {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(callingClass.getClassLoader().getResource(MainViewController.CHAT_INFO)));
            Stage secondStage = new Stage();
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            secondStage.setTitle("Chatdetails with " + ClientData.getInstance().getRemoteClientFromOpenChat().getName());




            secondStage.initModality(Modality.APPLICATION_MODAL);
            secondStage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
        }

    }


}



