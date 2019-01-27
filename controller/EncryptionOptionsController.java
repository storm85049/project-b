package controller;

import Krypto.RSA;
import client.ClientData;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EncryptionOptionsController implements IController, Initializable {

    private static final String AFFINE_CHIFFRE = "Affine Chiffre";
    private static final String VIGENERE_CHIFFRE = "Vigenere Chiffre";
    private static final String HILL_CHIFFRE = "Hill Chiffre";
    private static final String RSA = "RSA";
    private static final String ELGAMAL = "ElGamal";

    private Map <String, String> encryptionMap = new HashMap<>();
    private JSONObject symmetricEncryptionParameters = new JSONObject();

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
            String asymmSelection = (String) asymmetricEncryptionComboBox.getSelectionModel().getSelectedItem();
            String symmSelection = (String) symmetricEncryptionComboBox.getSelectionModel().getSelectedItem();
            JSONObject encryptionParameters = JSONUtil.getEncryptionOptions(asymmSelection, symmSelection, putSymmetricEncryptionParametersIntoJSONObject(symmSelection));
            ClientData.getInstance().setEncryptionData(encryptionParameters);
            centerNode.getScene().getWindow().hide();
        });

        //TODO: Wenn der Accept Button geklickt wird, werden die ausgewählten Modi in der ClientData Klasse gespeichert. Dafür Getter und Setter definieren.
        //TODO: Außerdem eine Klasse schreiben, die, wenn man absenden klickt, den eingegeben Text auf einem Verschlüsselungsobjekt (zB Vigenere) verschlüsselt und das JSON Objekt erstellt.

    }

    private JSONObject putSymmetricEncryptionParametersIntoJSONObject(String symmetricEncryption) {
        switch (symmetricEncryption) {
            case (AFFINE_CHIFFRE): {
                affinTField = (TextField) this.getPane().lookup("#affinTField");
                affinKField = (TextField) this.getPane().lookup("#affinKField");

                String T = affinTField.getText();
                String K = affinKField.getText();
                symmetricEncryptionParameters.put("T", T);
                symmetricEncryptionParameters.put("K", K);
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

    public void encryptKeys(JSONObject encryptionData){
        String asymmetricEncryption = (String)encryptionData.get(("asymmetricEncryptionMode"));
        String symmetricEncryption = (String)encryptionData.get(("symmetricEncryptionMode"));

        //Parameter ermitteln, die von der symmetrischen Verschlüsselung abhängig sind
        switch(symmetricEncryption){
            case (AFFINE_CHIFFRE):
                String t = (String)symmetricEncryptionParameters.get("T");
                String k = (String)symmetricEncryptionParameters.get("k");
                encryptAffineKey(asymmetricEncryption);
                break;
            case (VIGENERE_CHIFFRE):
                JSONObject json = (JSONObject)encryptionData.get("symmetricEncryptionParameters");
                String key = (String)json.get("vigenereKey");
                encryptVigenereKey(asymmetricEncryption, key);
                break;
        }

    }

    private void encryptVigenereKey(String asymmetricEncryption, String key) {
        if(asymmetricEncryption.equals("RSA")){
            Krypto.RSA RSA = new RSA();
            RSA.encrypt(key, ClientData.getInstance().getAvailableChatById(ClientData.getInstance().getIdFromOpenChat()).getPublicRSAKeyMap());
            System.out.println(RSA.encrypt(key, ClientData.getInstance().getAvailableChatById(ClientData.getInstance().getIdFromOpenChat()).getPublicRSAKeyMap()));
        }

    }

    private void encryptAffineKey(String asymmetricEncryption) {

        if (asymmetricEncryption.equals("RSA")){
            Krypto.RSA rsa = new RSA();
       //     rsa.
        }

    }
}
