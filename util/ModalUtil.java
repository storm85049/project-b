package util;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

public class ModalUtil{

    @FXML
    private static Text symmetricKey;

    @FXML
    private static ChoiceBox asymmetricEncryption;

    @FXML
    private static ChoiceBox symmetricEncryption;

    @FXML
    private static ChoiceBox operationsMode;


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

    public static void showEncryptionOptions(){
        //openModalBox();
        // hier kommt Code hin, der sich alle ausgewählten Elemente raussucht. Muss erst mal auf Matzes GUI Implementierung warten, damit ich die IDs weiß.


        String symkey = symmetricKey.getText();
        String asymMode = asymmetricEncryption.getValue().toString();
        String smyMode = symmetricEncryption.getValue().toString();
        String opMode = operationsMode.getValue().toString();

        startEncryption(asymMode, smyMode, symkey, opMode);
    }

    private static void startEncryption(String asymMode, String symMode, String symKey, String opMode){

    }

}
