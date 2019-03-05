package oop.client;

import Krypto.ElGamal;
import Krypto.RSA;
import client.ClientData;
import client.ObjectIOSingleton;
import controller.IController;
import controller.MainViewController;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;
import util.Actions;
import util.JSONUtil;
import util.ModalUtil;

import java.net.InetAddress;

public class LoginAction implements ActionResolver {
    @Override
    public  void resolve(JSONObject jsonObject, IController controller) {


        String name = (String)jsonObject.get("name");
        String id = (String)jsonObject.get("id");

        ClientData.getInstance().setName(name);
        ClientData.getInstance().setId(id);
        InetAddress remoteAdress = ObjectIOSingleton.getInstance().socket.getInetAddress();
        ClientData.getInstance().setServerAdress(remoteAdress);
        MainViewController.getInstance().loadUI(MainViewController.CHAT_VIEW);


    }


}
