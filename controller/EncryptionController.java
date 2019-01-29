package controller;

import Krypto.AffineCipher;
import Krypto.RSA;
import client.ClientData;
import com.sun.security.ntlm.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.MatrixType;
import org.json.simple.JSONObject;
import util.JSONUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EncryptionController implements IController, Initializable {

    private static final String AFFINE_CHIFFRE = "Affine Chiffre";
    private static final String VIGENERE_CHIFFRE = "Vigenere Chiffre";
    private static final String HILL_CHIFFRE = "Hill Chiffre";
    private static final String RSA = "RSA";
    private static final String ELGAMAL = "ElGamal";
    private static Map <String, String> encryptionMap = new HashMap<>();
    private static JSONObject symmetricEncryptionParameters = new JSONObject();
    private static JSONObject encryptedMessage = new JSONObject();
    private static String asymmetricSelection;
    private static String symmetricSelection;

    @FXML
    private Text test;

    @FXML
    private TextField vigenereKeyField;

    @FXML
    private TextField affinTField;

    @FXML
    private TextField affinKField;

    private MatrixType hillChiffreMatrix;

    @FXML
    private ComboBox asymmetricEncryptionComboBox;

    @FXML
    private ComboBox symmetricEncryptionComboBox;

    @FXML
    private ChoiceBox operationsMode;

    @FXML
    private Button abortButton;

    @FXML
    private Button acceptButton;

    @FXML
    private BorderPane mainPane;

    @FXML
    private HBox centerPane;

    private HBox centerNode;

    public static ObservableList<String> asymmetricEncryptionOptions =
            FXCollections.observableArrayList(
                    RSA,
                    ELGAMAL
            );
    public static ObservableList<String> symmetricEncryptionOptions =
            FXCollections.observableArrayList(
                    AFFINE_CHIFFRE,
                    VIGENERE_CHIFFRE,
                    HILL_CHIFFRE
            );


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
            try{
                centerNode = FXMLLoader.load(getClass().getClassLoader().getResource(encryptionMap.get(symmSelection)));
                mainPane.setCenter(centerNode);
            } catch(IOException e){
                e.printStackTrace();
            }
        });

        abortButton.setOnAction(event -> {
            asymmetricSelection = (String) asymmetricEncryptionComboBox.getSelectionModel().getSelectedItem();
            symmetricSelection = (String) symmetricEncryptionComboBox.getSelectionModel().getSelectedItem();
            JSONObject encryptionParameters = JSONUtil.getEncryptionOptions(asymmetricSelection, symmetricSelection, putSymmetricEncryptionParametersIntoJSONObject(symmetricSelection));
            ClientData.getInstance().setEncryptionData(encryptionParameters);
            centerNode.getScene().getWindow().hide();
        });
    }

    private JSONObject putSymmetricEncryptionParametersIntoJSONObject(String symmetricEncryption) {
        switch (symmetricEncryption) {
            case (AFFINE_CHIFFRE): {
                affinTField = (TextField) this.getPane().lookup("#affinTField");
                affinKField = (TextField) this.getPane().lookup("#affinKField");

                String T = affinTField.getText();
                String K = affinKField.getText();
                symmetricEncryptionParameters.put("K", K);
                symmetricEncryptionParameters.put("T", T);
                break;
            }
            case (VIGENERE_CHIFFRE): {
                vigenereKeyField = (TextField) this.getPane().lookup("#vigenereKeyField");

                String vigenereKey = vigenereKeyField.getText();
                symmetricEncryptionParameters.put("vigenereKey", vigenereKey);
                break;
            }
        }
        return symmetricEncryptionParameters;
    }

    public JSONObject encryptMessage(String message){

        encryptedMessage.put("asymmetricEncryption", asymmetricSelection);
        encryptedMessage.put("symmetricEncryption", symmetricSelection);
        encryptSymmetricKeys(ClientData.getInstance().getEncryptionData());

        switch(symmetricSelection){
            case (AFFINE_CHIFFRE):
                AffineCipher affineCipher = new AffineCipher((String)symmetricEncryptionParameters.get("K"),(String)symmetricEncryptionParameters.get("T"),"ABC");
                String encrypt = affineCipher.encrypt(message);
                System.out.println(encrypt);
                encryptedMessage.put("kryptoMessage",affineCipher.encrypt(message));
                break;
            case(VIGENERE_CHIFFRE):
                break;
        }
        return encryptedMessage;
    }

    private JSONObject encryptSymmetricKeys(JSONObject encryptionData){
        String asymmetricEncryption = (String)encryptionData.get(("asymmetricEncryptionMode"));
        String symmetricEncryption = (String)encryptionData.get(("symmetricEncryptionMode"));

        //Parameter ermitteln, die von der symmetrischen Verschlüsselung abhängig sind
        switch(symmetricEncryption){
            case (AFFINE_CHIFFRE):
                String k = (String)symmetricEncryptionParameters.get("K");
                String t = (String)symmetricEncryptionParameters.get("T");
                encryptedMessage.put("K", encryptAffineKey(asymmetricEncryption, k));
                encryptedMessage.put("T", encryptAffineKey(asymmetricEncryption, t));
                break;
            case (VIGENERE_CHIFFRE):
                JSONObject json = (JSONObject)encryptionData.get("symmetricEncryptionParameters");
                String key = (String)json.get("vigenereKey");
                encryptedMessage.put("encryptedVigenereKey", encryptVigenereKey(asymmetricEncryption, key));
                break;
        }
        return null;
    }

    private String encryptVigenereKey(String asymmetricEncryption, String key) {
        if(asymmetricEncryption.equals(RSA)){
            RSA RSA = ClientData.getInstance().getRSA();
            String encryptedVigenereKey = RSA.encrypt(key, ClientData.getInstance().getAvailableChatById(ClientData.getInstance().getIdFromOpenChat()).getPublicRSAKeyMap());
            System.out.println(RSA.encrypt(key, ClientData.getInstance().getAvailableChatById(ClientData.getInstance().getIdFromOpenChat()).getPublicRSAKeyMap()));
            return encryptedVigenereKey;
        }
        return null;
    }

    private String encryptAffineKey(String asymmetricEncryption, String key) {

        if (asymmetricEncryption.equals(RSA)){
            RSA RSA = ClientData.getInstance().getRSA();
            String encryptedAffineKey = RSA.encrypt(key,ClientData.getInstance().getAvailableChatById(ClientData.getInstance().getIdFromOpenChat()).getPublicRSAKeyMap());
            return  encryptedAffineKey;
        }
        return null;
    }


    public JSONObject decryptMessage(JSONObject encryptedMessage){
        JSONObject json = (JSONObject)encryptedMessage.get("message");
        String toBeDecryptedMessage = (String)json.get("kryptoMessage");
        String asymmetricEncryption = (String)json.get("asymmetricEncryption");
        String symmetricEncryption = (String)json.get("symmetricEncryption");

        switch(symmetricEncryption){
            case(AFFINE_CHIFFRE):
                String k = (String)json.get("K");
                String t = (String)json.get("T");

                k = decryptAffineKey(asymmetricEncryption, k);
                t = decryptAffineKey(asymmetricEncryption, t);
                AffineCipher affineCipher = new AffineCipher(k, t,"ABC");
                String decryptedMessage = affineCipher.decrypt(toBeDecryptedMessage);
                json.put("kryptoMessage", decryptedMessage);
                encryptedMessage.put("message", json);
                return encryptedMessage;
            }
        return null;
        }


        private String decryptAffineKey(String asymmetricSelection, String key){
            switch (asymmetricSelection){
                case(RSA):
                    RSA RSA = ClientData.getInstance().getRSA();
                    return RSA.decrypt(key);
            }
            return null;
        }
    }

