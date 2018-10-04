package client;

import org.json.simple.JSONObject;
import server.ServerMain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

public class ObjectIOSingleton extends Observable{

    private static ObjectIOSingleton instance;
    public Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;

    public void init(){
        try{
            socket = new Socket("localhost", ServerMain.PORT);
            this.out = new ObjectOutputStream(socket.getOutputStream());
        new Thread(()->{
            try {
                this.in = new ObjectInputStream(socket.getInputStream());
                while (true){
                    Object o  = in.readObject();
                    notifyControllerObservers(o);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //MainViewController.alert("Der Server ist nicht erreichbar");
            e.printStackTrace();
        }
    }

    public void notifyControllerObservers(Object o){
        JSONObject json = (JSONObject)o;
        setChanged();
        notifyObservers(json);
    }


    public void sendToServer(JSONObject jsonObject){
        try {
            this.out.writeObject(jsonObject);
            this.out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static synchronized ObjectIOSingleton getInstance() {
        if (ObjectIOSingleton.instance == null){
            ObjectIOSingleton.instance = new ObjectIOSingleton();
        }
        return ObjectIOSingleton.instance;
    }
}
