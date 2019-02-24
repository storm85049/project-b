package controller;

import controller.helper.ToggleSwitch;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class CryptoChatInfoController implements Initializable, IController {


    @FXML
    private Button hyperlink;
    @FXML
    private Button okBtn;

    @FXML
    private VBox mainPane;

    @FXML
    private HBox hboxDown;

    private String url = "www.github.com/storm85049/project-b";

    private URI uri;


    @Override
    public void initialize(URL location, ResourceBundle resources) {



       /* ToggleSwitch toggle = new ToggleSwitch();



        logInfo.textProperty().bind(Bindings.when(toggle.switchedOnProperty()).then("Log vertically").otherwise("Log horizontally"));

        Platform.runLater(()->{

            hboxDown.getChildren().add(1,toggle);
            hboxDown.getChildren().get(1).setStyle();
        });
*/


        try{
            uri = new URI(url);
        }catch (URISyntaxException e){
            System.out.println("false uri");
        }





























        hyperlink.addEventHandler(MouseEvent.MOUSE_CLICKED, evt ->{
            try {
                java.awt.Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, evt->{
            this.getPane().getScene().getWindow().hide();
        });

    }

    @Override
    public Pane getPane() {
        return mainPane;
    }
}
