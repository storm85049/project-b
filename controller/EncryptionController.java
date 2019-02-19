package controller;

import Krypto.AffineCipher;
import Krypto.IAsymmetricEncryption;
import Krypto.RSA;
import animatefx.animation.Flash;
import client.ClientData;
import client.ObjectIOSingleton;
import client.RemoteClient;
import com.sun.security.ntlm.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.json.simple.JSONObject;
import util.Actions;
import util.JSONUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EncryptionController implements IController, Initializable {

    public static final String AFFINE_CHIFFRE = "Affine Chiffre";
    public static final String VIGENERE_CHIFFRE = "Vigenere Chiffre";
    public static final String HILL_CHIFFRE = "Hill Chiffre";
    public static final String RSA = "RSA";
    public static final String ELGAMAL = "ElGamal";


    public static final String DEFAULT_ALPHABET_MODE = "ABC";
    private static Map <String, String> encryptionMap = new HashMap<>();
    private static JSONObject encryptedMessage = new JSONObject();
    private String asymmetricSelection;
    private String symmetricSelection;


    @FXML
    private Label symWarning;

    @FXML
    private Label asymWarning;

    @FXML
    private ComboBox asymmetricEncryptionComboBox;

    @FXML
    private ComboBox symmetricEncryptionComboBox;

    @FXML
    private ChoiceBox operationsMode;

    @FXML
    private Button abortButton;

    //todo:was passiert wenn der geklickt wird ? modus muss ja zwangsweise ausgewählt werden.
    @FXML
    private Button acceptButton;

    @FXML
    private BorderPane mainPane;

    private static ObservableList<String> asymmetricEncryptionOptions =
            FXCollections.observableArrayList(
                    RSA,
                    ELGAMAL
            );

    private static ObservableList<String> symmetricEncryptionOptions =
            FXCollections.observableArrayList(
                    AFFINE_CHIFFRE,
                    VIGENERE_CHIFFRE,
                    HILL_CHIFFRE
            );

    private Node find(String selector)
    {
        String searchfor = selector;
        if(!searchfor.startsWith("#")){
            searchfor = "#" + searchfor;
        }
        return this.getPane().lookup(searchfor);
    }

    public String getAsymmetricSelection() {
        return asymmetricSelection;
    }

    public void setAsymmetricSelection(String asymmetricSelection) {
        this.asymmetricSelection = asymmetricSelection;
    }

    public String getSymmetricSelection() {
        return symmetricSelection;
    }

    public void setSymmetricSelection(String symmetricSelection) {
        this.symmetricSelection = symmetricSelection;
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        encryptionMap.put(AFFINE_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_AFFIN);
        encryptionMap.put(VIGENERE_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_VIGENERE);
        //encryptionMap.put(HILL_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_HILL);   ---> fxml fehlt noch
        symmetricEncryptionComboBox.setItems(symmetricEncryptionOptions);
        asymmetricEncryptionComboBox.setItems(asymmetricEncryptionOptions);
        symmetricEncryptionComboBox.setOnAction(event -> {
            String symmSelection = (String) symmetricEncryptionComboBox.getSelectionModel().getSelectedItem();
            try {
                Node center = MainViewController.loadComponent(getClass(), encryptionMap.get(symmSelection));
                mainPane.setCenter(center);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        acceptButton.setOnAction(event -> {
            this.setAsymmetricSelection((String) asymmetricEncryptionComboBox.getSelectionModel().getSelectedItem()) ;
            this.setSymmetricSelection((String) symmetricEncryptionComboBox.getSelectionModel().getSelectedItem());

            if (this.getValidSelection() == null) return;

            RemoteClient remoteClient = ClientData.getInstance().getRemoteClientFromOpenChat();
            remoteClient.setRequestedEncryptionData(this.getValidSelection());

            String from = ClientData.getInstance().getId();
            String to = remoteClient.getId();
            JSONObject jsonToSend = JSONUtil.initEncryptedChatRequest(from, to, remoteClient.getEncryptedEncryptionData());
            ObjectIOSingleton.getInstance().sendToServer(jsonToSend);
            this.getPane().getScene().getWindow().hide();
        });
    }

    private String convertModes(String s)
    {
         if(s.equals(RSA)) return Actions.MODE_RSA;
         if(s.equals(ELGAMAL)) return Actions.MODE_ELGAMAL;
         if(s.equals(AFFINE_CHIFFRE)) return Actions.MODE_AFFINE;
         if(s.equals(HILL_CHIFFRE)) return Actions.MODE_HILL;
         if(s.equals(VIGENERE_CHIFFRE)) return Actions.MODE_VIGENERE;
         return "";
    }


    private JSONObject getValidSelection()
    {
        //todo:check einbauen ob der input valid ist zb t != 0923850293458 o.ä.;



        boolean valid = true;
        if(this.getAsymmetricSelection()== null ){
            flashInvalid(asymWarning);
            valid = false;
        }
        if(this.getSymmetricSelection() == null ){
            flashInvalid(symWarning);
            valid = false;
        }

        if(!valid) return null;

        String mode = convertModes(this.getAsymmetricSelection());


        switch (this.getSymmetricSelection()){
            case(AFFINE_CHIFFRE):
                String t= ((TextField) this.find("affinTField")).getText();
                String k = ((TextField) this.find("affinKField")).getText();
                if(!k.isEmpty() && !t.isEmpty()){
                    return JSONUtil.affineKeyJson(mode,t,k);
                }else{
                    flashInvalid((Label)this.find("affinWarning"));
                }
                break;
            case(VIGENERE_CHIFFRE):
                String vigenereKey = ((TextField)this.find("vigenereKeyField")).getText();
                if(!vigenereKey.isEmpty()){
                    return JSONUtil.vigenereKeyJson(mode,vigenereKey);
                }else{
                    flashInvalid((Label) this.find("vigenereWarning"));
                }
                break;
        }
        return null;
    }



    private void flashInvalid (Label which)
    {
        which.setVisible(true);
        new Flash(which).play();
    }

}

