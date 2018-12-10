package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import util.JSONUtil;
import util.ModalUtil;

import java.awt.*;
import java.io.IOException;

public class ClientMain extends Application{

    ModalUtil util;

    public static void main(String[] args) {
        System.out.println("Hallo Welt");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/maintemplate.fxml"));

        double inititalWidth = 1460;
        double inititalHeight = 720;
        Scene scene = new Scene(root, inititalWidth, inititalHeight);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Welcome to secure ChatClient");
        primaryStage.show();
    }

    @Override
    public void stop(){
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
    }
}
