package controller.helper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RC4ValidationController implements Initializable {

    @FXML
    private TextField rc4Field;

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            String text = change.getControlNewText();

            char last;
            try{
                last = text.charAt(text.length()-1);
            }catch (StringIndexOutOfBoundsException e){
                return change;
            }

            if (isValid(last)) { // your validation logic
                return null;
            }
            return change;
        });

        rc4Field.setTextFormatter(textFormatter);
    }

    private boolean isValid(char s)
    {
        return !(s == '1' || s == '0');
    }
}
