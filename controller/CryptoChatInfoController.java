package controller;

import client.ClientData;
import controller.helper.ToggleSwitch;
import controller.logger.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.omg.PortableInterceptor.ORBIdHelper;
import util.ChatViewUtil;


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
    @FXML
    private Text logOrientationInfo;

    private String url = "www.github.com/storm85049/project-b";

    private URI uri;

    private SplitPane splitPane;

    private static final String LOG_HORIZONTAL =" log horizontal";
    private static final String LOG_VERTICAL   =" log vertical";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        splitPane = (SplitPane) ChatViewUtil.find("splitPane");


        ToggleSwitch toggleSwitch = new ToggleSwitch();

        Orientation o = ClientData.getInstance().getPaneOrientation();

        toggleSwitch.setSwitchedOn(o == Orientation.VERTICAL);


        logOrientationInfo.textProperty().bind(Bindings.when(toggleSwitch.switchedOnProperty()).then(LOG_HORIZONTAL).otherwise(LOG_VERTICAL));


        toggleSwitch.getStyleClass().add("button-with-margin");

        hboxDown.getChildren().add(0,toggleSwitch);



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

            this.setOrientationOfSplitPane();
            this.getPane().getScene().getWindow().hide();
        });


    }




    @Override
    public Pane getPane() {
        return mainPane;
    }

    private void  setOrientationOfSplitPane()
    {
        try{
            Orientation o ;
            if(logOrientationInfo.getText().equals(LOG_HORIZONTAL))
                o = Orientation.VERTICAL;
            else
                o  = Orientation.HORIZONTAL;

            splitPane.setOrientation(o);
            ClientData.getInstance().setPaneOrientation(o);

        }catch (Exception e){
            System.out.println(e);
        }


    }

}
