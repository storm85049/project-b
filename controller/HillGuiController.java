package controller;

import com.sun.org.apache.bcel.internal.generic.ICONST;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HillGuiController implements Initializable, IController {

    public static final String twoTimesTwo = "2x2";
    public static final String threeTimesThree= "3x3";




    @FXML
    private ComboBox hillTypeBox;

    @FXML
    private HBox mainPane;

    private Map<String, String> mappedValues = new HashMap<>();
    private static ObservableList<String> hillOptions =
            FXCollections.observableArrayList(
                    twoTimesTwo,
                    threeTimesThree
            );




    @Override
    public void initialize(URL location, ResourceBundle resources) {


        this.hillTypeBox.setItems(hillOptions);

        this.mappedValues.put(twoTimesTwo, MainViewController.ENCRYPTION_HILL_2x2);
        this.mappedValues.put(threeTimesThree, MainViewController.ENCRYPTION_HILL_3x3);


        this.hillTypeBox.setOnAction(event -> {
            String selectedOption = (String) hillTypeBox.getSelectionModel().getSelectedItem();
            try {
                Node matrixType = MainViewController.loadComponent(getClass(), this.mappedValues.get(selectedOption));
                if(this.getPane().getChildren().size()>1){
                    this.getPane().getChildren().remove(1);
                }
                this.getPane().getChildren().add(1,matrixType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




    @Override
    public Pane getPane() {
        return this.mainPane;
    }
}
