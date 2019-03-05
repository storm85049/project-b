package client;

import Krypto.RSA;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import util.JSONUtil;
import util.ModalUtil;

import java.awt.*;
import java.io.IOException;

public class ClientMain extends Application{



    public static void main(String[] args) {
        System.out.println("Hallo Welt");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/maintemplate.fxml"));

        double inititalWidth = 1460;
        double inititalHeight = 720;
        Scene scene = new Scene(root/*,inititalWidth,inititalHeight*/);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Welcome to secure ChatClient");

        /*kann aus, falls nervig*/
        //primaryStage.setMaximized(true);
        primaryStage.show();



        primaryStage.setOnCloseRequest( evt -> {
            evt.consume();
            closeApp(primaryStage);
        });
    }


    private void closeApp(Stage stage){
        String id = ClientData.getInstance().getId();
        if (id != null){
            JSONObject json = JSONUtil.shutDownConnectionJSON(id);
            ObjectIOSingleton.getInstance().sendToServer(json);
            try {
                ObjectIOSingleton.getInstance().socket.close();
                ObjectIOSingleton.getInstance().stopReading = true;
                ObjectIOSingleton.getInstance().out.close();
                ObjectIOSingleton.getInstance().in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection shut down!");
        }
        stage.close();

    }



}
