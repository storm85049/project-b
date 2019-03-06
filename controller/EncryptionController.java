package controller;

import Krypto.*;
import animatefx.animation.Flash;
import client.ClientData;
import client.ObjectIOSingleton;
import client.RemoteClient;
import com.sun.security.ntlm.Client;
import controller.logger.Logger;
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

import org.json.simple.JSONObject;
import util.Actions;
import util.ChatViewUtil;
import util.JSONUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EncryptionController implements IController, Initializable {

    public static final String AFFINE_CHIFFRE = "Affine Cipher";
    public static final String VIGENERE_CHIFFRE = "Vigenere Cipher";
    public static final String HILL_CHIFFRE = "Hill Cipher";
    public static final String RC4_CHIFFRE = "RC4";
    public static final String DES_CHIFFRE = "DES";

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
                    HILL_CHIFFRE,
                    RC4_CHIFFRE,
                    DES_CHIFFRE
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
        encryptionMap.put(RC4_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_RC4);
        encryptionMap.put(DES_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_DES);
        encryptionMap.put(HILL_CHIFFRE, MainViewController.ENCRYPTION_DIALOG_HILL);
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
            String referrer = remoteClient.isEncryptionSet() ? Actions.REFERRER_CREATE : Actions.REFERRER_UPDATE;
            remoteClient.setRequestedEncryptionData(this.getValidSelection());

            String from = ClientData.getInstance().getId();
            String to = remoteClient.getId();
            JSONObject jsonToSend = JSONUtil.initEncryptedChatRequest(from, to, remoteClient.getEncryptedEncryptionData());
            ObjectIOSingleton.getInstance().sendToServer(jsonToSend);


            Platform.runLater(()->{
                Logger.getInstance().setReferrer(referrer);
                Logger.getInstance().log(Actions.LOG_INIT_CHAT, to);
            });



            this.getPane().getScene().getWindow().hide();
        });


        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientFromOpenChat();
        if(remoteClient.getSymEncryptionString() != null){
                String symMode = this.flipModes(remoteClient.getSymEncryptionString());
                symmetricEncryptionComboBox.getSelectionModel().select(symMode);
                this.setSymmetricSelection(symMode);
                String asymMode = this.flipModes(remoteClient.getAsymEncryptionString());
                asymmetricEncryptionComboBox.getSelectionModel().select(asymMode);
                this.setAsymmetricSelection(asymMode);

            Node center = MainViewController.loadComponent(getClass(), encryptionMap.get(this.getSymmetricSelection()));
            mainPane.setCenter(center);

        }



    }

    private String convertModes(String s)
    {
         if(s.equals(RSA)) return Actions.MODE_RSA;
         if(s.equals(ELGAMAL)) return Actions.MODE_ELGAMAL;
         if(s.equals(AFFINE_CHIFFRE)) return Actions.MODE_AFFINE;
         if(s.equals(RC4_CHIFFRE)) return Actions.MODE_RC4;
         if(s.equals(DES_CHIFFRE)) return Actions.MODE_DES;
         if(s.equals(HILL_CHIFFRE)) return Actions.MODE_HILL;
         if(s.equals(VIGENERE_CHIFFRE)) return Actions.MODE_VIGENERE;
         return "";
    }


    private String flipModes(String s)
    {
        if(s.equals( Actions.MODE_RSA)) return RSA;
        if(s.equals( Actions.MODE_ELGAMAL)) return ELGAMAL;
        if(s.equals( Actions.MODE_AFFINE)) return AFFINE_CHIFFRE;
        if(s.equals( Actions.MODE_RC4)) return RC4_CHIFFRE;
        if(s.equals( Actions.MODE_DES)) return DES_CHIFFRE;
        if(s.equals( Actions.MODE_HILL)) return HILL_CHIFFRE;
        if(s.equals( Actions.MODE_VIGENERE)) return VIGENERE_CHIFFRE;
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
                if(!k.isEmpty() && !t.isEmpty() && this.isValidAffineFields(t,k)){
                    return JSONUtil.affineKeyJson(mode,t,k);
                }else{
                    flashInvalid((Label)this.find("affineWarning"));
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
            case(RC4_CHIFFRE):
                String rc4Field = ((TextField)this.find("rc4Field")).getText();
                //if its not 1s and 0s
                if(!rc4Field.isEmpty()){
                    return JSONUtil.rc4KeyJson(mode,rc4Field);
                }else{
                    flashInvalid((Label)this.find("rc4Warning"));
                }break;
            case(DES_CHIFFRE):
                Label possibleDesKey = (Label) this.find("desKey");
                if(possibleDesKey.getText().isEmpty()){// if generate wasnt clicked
                    flashInvalid((Label) this.find("desWarning"));
                }else{
                    Map<String,String> keyMap = ClientData.getInstance().getInternalChosenDESKeyMap();
                    return JSONUtil.desKEYJson(mode,keyMap);
                }break;

            case(HILL_CHIFFRE):

                ComboBox comboBox = (ComboBox) this.find("hillTypeBox");
                String type  = (String) comboBox.getSelectionModel().getSelectedItem();
                String[][] key = delegateHillCheck(type);
                if(key != null){
                    return JSONUtil.hillKeyJson(mode, key);
                }else{
                    flashInvalid((Label) this.find("hillWarning"));
                }

        }
        return null;
    }

        private boolean isValidAffineFields(String t, String k)
        {

            Integer[] validT = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23,25};

            if( !(Integer.valueOf(k) >= 0) || !(Integer.valueOf(k) <= 25)){
                return false;
            }
            if (!(Arrays.asList(validT).contains(Integer.valueOf(t)))){
                return false;
            };
            return true;
        }

    private void flashInvalid (Label which)
    {
        which.setVisible(true);
        new Flash(which).play();
    }



    private String[][] delegateHillCheck(String matrixType) {


        String[][] key = new String[2][2];
        String[][] key2 = new String[3][3];


        switch (matrixType) {
            case HillGuiController.twoTimesTwo:
                key[0][0] = ((TextField) this.find("h0")).getText();
                key[0][1] = ((TextField) this.find("h1")).getText();
                key[1][0] = ((TextField) this.find("h2")).getText();
                key[1][1] = ((TextField) this.find("h3")).getText();
                break;
            case HillGuiController.threeTimesThree:
                key2[0][0] = ((TextField) this.find("h0")).getText();
                key2[0][1] = ((TextField) this.find("h1")).getText();
                key2[0][2] = ((TextField) this.find("h2")).getText();
                key2[1][0] = ((TextField) this.find("h3")).getText();
                key2[1][1] = ((TextField) this.find("h4")).getText();
                key2[1][2] = ((TextField) this.find("h5")).getText();
                key2[2][0] = ((TextField) this.find("h6")).getText();
                key2[2][1] = ((TextField) this.find("h7")).getText();
                key2[2][2] = ((TextField) this.find("h8")).getText();
                break;
        }
        HillCipher hillCipher;
        switch (matrixType) {
            case (HillGuiController.twoTimesTwo):
                try {
                    hillCipher = new HillCipher(key, "ASCII");
                    return key;
                } catch (Exception e) {
                    return null;
                }

            case (HillGuiController.threeTimesThree):
                try {
                    hillCipher = new HillCipher(key2, "ASCII");
                    return key2;
                } catch (Exception e) {
                    return null;
                }


        }
            return null;
    }



}

