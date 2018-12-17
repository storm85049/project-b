package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BubbleController implements Initializable, IController{


    public Pane mainPane;


    @Override
    public Pane getPane() {
        return this.mainPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       Button button = (Button)(this.getPane().getChildren().get(0));




       button.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent e) {
                    System.out.println(button.getId());
                    //tryto init chat ?

                }
            });
    }

    public void sendInitChatRequest()
    {

    }
}

