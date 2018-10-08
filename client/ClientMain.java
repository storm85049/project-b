package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import util.JSONUtil;

import java.io.IOException;

public class ClientMain extends Application{

    public static void main(String[] args) {
        System.out.println("Hallo Welt");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/maintemplate.fxml"));

        Scene scene = new Scene(root);
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
                ObjectIOSingleton.getInstance().in.close();
                ObjectIOSingleton.getInstance().out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection shut down!");
        }
    }
}