package util;

import javafx.scene.control.Dialog;
import org.json.simple.JSONObject;

public class ModalUtil {

    private static ModalUtil instance;

    public static ModalUtil getInstance() {
        if(ModalUtil.instance == null){
            ModalUtil.instance = new ModalUtil();
        }
        return ModalUtil.instance;
    }


    public static void modal(JSONObject json){
/*
        Dialog d  = new Dialog();
        d.showAndWait();*/

    }

}
