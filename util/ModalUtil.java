package util;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

public class ModalUtil{

    private static ModalUtil instance;

    public static ModalUtil getInstance() {
        if(ModalUtil.instance == null){
            ModalUtil.instance = new ModalUtil();
        }
        return ModalUtil.instance;
    }


    public static void modal(JSONObject json){

        //nur ein beispiel

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("den namen gibt es schon");
        alert.setContentText("......");

        alert.showAndWait();
    }

}
