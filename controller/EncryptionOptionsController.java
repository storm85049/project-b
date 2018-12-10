package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.MatrixType;
import org.json.simple.JSONObject;

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
    private static Text vigenereKey;

    @FXML
    private static Text affinT;

    @FXML
    private static Text affinK;

    private static MatrixType hillChiffreMatrix;

    @FXML
    private static ComboBox asymmetricEncryption;

    @FXML
    private static ComboBox symmetricEncryption;

    @FXML
    private static ChoiceBox operationsMode;

    @FXML
    private static Button cancelButton;

    @FXML
    private static Button okButton;

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
            }
            catch(IOException e){
                e.printStackTrace();
            }

        });
    }

    private void buildJSONAndSendToEncryptionInterface(){
        Button acceptButton = (Button) this.getPane().lookup("#acceptButton");
        //Button abortButton = (Button) this.getPane().lookup("#abortButton");

        acceptButton.setOnAction((event -> {
            JSONObject startChatWithSelectedEncryptionOptions = new JSONObject();
            startChatWithSelectedEncryptionOptions.put("asymEncrypt", asymmetricEncryption.getValue());
            startChatWithSelectedEncryptionOptions.put("symEncrypt", symmetricEncryption.getValue());
            startChatWithSelectedEncryptionOptions.put("symEncrypt", symmetricEncryption.getValue());

        }));
    }
}
