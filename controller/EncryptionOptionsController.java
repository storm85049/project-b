package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
    private JSONObject symmEncryptionParameters = new JSONObject();

    private Map <String, String> encryptionMap = new HashMap<>();

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
    private static Text test;

    @FXML
    private static TextField vigenereKeyField;

    @FXML
    private static TextField affinTField;

    @FXML
    private static TextField affinKField;

    private static MatrixType hillChiffreMatrix;

    @FXML
    private static ComboBox asymmetricEncryption;

    @FXML
    private static ComboBox symmetricEncryption;

    @FXML
    private static ChoiceBox operationsMode;

    @FXML
    private static Button abortButton;

    @FXML
    private static Button acceptButton;

    @FXML
    private BorderPane mainPane;

    private Parent node;

    @Override
    public Pane getPane() {
        return mainPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        encryptionMap.put(AFFINE_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_AFFIN);
        encryptionMap.put(VIGENERE_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_VIGENERE);
        //encryptionMap.put(HILL_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_HILL);   ---> fxml fehlt noch

        symmetricEncryption = (ComboBox) this.getPane().lookup("#symmetricEncryptionComboBox");
        asymmetricEncryption = (ComboBox) this.getPane().lookup("#asymmetricEncryptionComboBox");
        acceptButton = (Button) this.getPane().lookup("#acceptButton");
        abortButton = (Button) this.getPane().lookup("#abortButton");

        symmetricEncryption.setItems(symmetricEncryptionOptions);
        asymmetricEncryption.setItems(asymmetricEncryptionOptions);
        symmetricEncryption.setOnAction(event -> {
            System.out.println(event);
            String selection = (String) symmetricEncryption.getSelectionModel().getSelectedItem();
            System.out.println(selection);
            System.out.println(encryptionMap.get(selection));
            try{
                node = FXMLLoader.load(getClass().getClassLoader().getResource(encryptionMap.get(selection)));
                mainPane.setCenter(node);
            } catch(IOException e){
                e.printStackTrace();
            }
        });

        //abortButton deshalb, weil der acceptButton nicht klickbar ist. Natürlich sollte eigentlich hier der acceptButton diese Funktion ausführen.
        abortButton.setOnAction(event -> {
            String asymmSelection = (String) asymmetricEncryption.getSelectionModel().getSelectedItem();
            String symmSelection = (String) symmetricEncryption.getSelectionModel().getSelectedItem();
            JSONUtil.setEncryptionOptions(asymmSelection, symmSelection, resolveSymmetricEncryptionParameters(symmSelection));
            node.getScene().getWindow().hide();
        });

        //TODO: Wenn der Accept Button geklickt wird, werden die ausgewählten Modi in der ClientData Klasse gespeichert. Dafür Getter und Setter definieren.
        //TODO: Außerdem eine Klasse schreiben, die, wenn man absenden klickt, den eingegeben Text auf einem Verschlüsselungsobjekt (zB Vigenere) verschlüsselt und das JSON Objekt erstellt.



    }

    private JSONObject resolveSymmetricEncryptionParameters(String symmEncryptionMode) {
        switch (symmEncryptionMode) {
            case (AFFINE_CHIFFRE): {
                affinTField = (TextField) this.getPane().lookup("#affinTField");
                affinKField = (TextField) this.getPane().lookup("#affinKField");

                String T = affinTField.getText();
                String K = affinKField.getText();
                symmEncryptionParameters.put("T", T);
                symmEncryptionParameters.put("K", K);
                break;
            }
            case (VIGENERE_CHIFFRE): {
                vigenereKeyField = (TextField) this.getPane().lookup("#vigenereKeyField");

                String vigenereKey = vigenereKeyField.getText();
                symmEncryptionParameters.put("vigenereKey", vigenereKey);
                break;
            }
        }

        return symmEncryptionParameters;
    }

    /*private void buildJSONAndSendToEncryptionInterface(){
        Button acceptButton = (Button) this.getPane().lookup("#acceptButton");
        Button abortButton = (Button) this.getPane().lookup("#abortButton");

        abortButton.setOnAction((event -> {
            JSONObject startChatWithSelectedEncryptionOptions = new JSONObject();
            startChatWithSelectedEncryptionOptions.put("asymEncrypt", asymmetricEncryption.getValue());
            startChatWithSelectedEncryptionOptions.put("symEncrypt", symmetricEncryption.getValue());

        }));
    }*/
}
